param(
    [switch]$DeployTestBackend,
    [switch]$DeployProductionBackend,
    [switch]$RunTestLoadTests,
    [switch]$RunLoadTests,
    [switch]$PortForward,
    [switch]$RecreateBackend
)

$ErrorActionPreference = 'Stop'

function Assert-Success {
    param(
        [string]$Step
    )

    if ($LASTEXITCODE -ne 0) {
        throw "$Step failed with exit code $LASTEXITCODE"
    }
}

function Invoke-Kubectl {
    param(
        [Parameter(Mandatory = $true)]
        [string[]]$Arguments,
        [Parameter(Mandatory = $true)]
        [string]$Step
    )

    & kubectl @Arguments
    Assert-Success $Step
}

function Wait-ForDeployment {
    param(
        [Parameter(Mandatory = $true)]
        [string]$DeploymentName,
        [Parameter(Mandatory = $true)]
        [string]$Step
    )

    & kubectl rollout status "deployment/$DeploymentName" -n petpal --timeout=300s
    Assert-Success $Step
}

function Apply-Manifests {
    param(
        [Parameter(Mandatory = $true)]
        [string[]]$Files
    )

    foreach ($file in $Files) {
        Invoke-Kubectl -Arguments @('apply', '-f', $file) -Step "apply $file"
    }
}

function Invoke-LoadTestJob {
    param(
        [Parameter(Mandatory = $true)]
        [string]$JobName,
        [Parameter(Mandatory = $true)]
        [string]$ManifestPath
    )

    Invoke-Kubectl -Arguments @('delete', 'job', $JobName, '-n', 'petpal', '--ignore-not-found') -Step "delete $JobName"
    Invoke-Kubectl -Arguments @('apply', '-f', $ManifestPath) -Step "apply $JobName"

    try {
        & kubectl wait --for=condition=complete "job/$JobName" -n petpal --timeout=900s
        Assert-Success "wait $JobName"
    } catch {
        Write-Host "Load test job $JobName failed; collecting logs..."
        & kubectl logs -n petpal "job/$JobName" --tail=200
        throw
    }

    & kubectl logs -n petpal "job/$JobName"
    Assert-Success "logs $JobName"
}

function Deploy-TestBackend {
    Write-Host 'Applying test Kubernetes manifests...'
    Apply-Manifests -Files @(
        'k8s/namespace.yaml',
        'k8s/secret.yaml',
        'k8s/configmap.yaml',
        'k8s/mysql-test.yaml'
    )

    Wait-ForDeployment -DeploymentName 'mysql-test' -Step 'mysql-test rollout'

    if ($RecreateBackend) {
        Write-Host 'Recreating the test backend deployment...'
        Invoke-Kubectl -Arguments @('delete', 'deployment/petpal-backend-test', '-n', 'petpal', '--ignore-not-found', '--wait=true') -Step 'delete test backend deployment'
    }

    Invoke-Kubectl -Arguments @('apply', '-f', 'k8s/petpal-backend-test.yaml') -Step 'apply test backend deployment'

    if (-not $RecreateBackend) {
        Write-Host 'Restarting the test backend deployment so it picks up the fresh image...'
        & kubectl rollout restart deployment/petpal-backend-test -n petpal
        Assert-Success 'restart test backend'
    }

    Wait-ForDeployment -DeploymentName 'petpal-backend-test' -Step 'test backend rollout'
}

function Deploy-ProductionBackend {
    Write-Host 'Applying production Kubernetes manifests...'
    Apply-Manifests -Files @(
        'k8s/namespace.yaml',
        'k8s/secret.yaml',
        'k8s/configmap.yaml',
        'k8s/mysql-pvc.yaml',
        'k8s/mysql-service.yaml',
        'k8s/mysql-deployment.yaml',
        'k8s/backend-service.yaml',
        'k8s/backend-deployment.yaml',
        'k8s/ingress.yaml'
    )

    Wait-ForDeployment -DeploymentName 'mysql' -Step 'mysql rollout'

    if ($RecreateBackend) {
        Write-Host 'Recreating the production backend deployment...'
        Invoke-Kubectl -Arguments @('delete', 'deployment/petpal-backend', '-n', 'petpal', '--ignore-not-found', '--wait=true') -Step 'delete production backend deployment'
        Invoke-Kubectl -Arguments @('apply', '-f', 'k8s/backend-deployment.yaml') -Step 'reapply production backend deployment'
    } else {
        Write-Host 'Restarting the production backend deployment so it picks up the fresh image...'
        & kubectl rollout restart deployment/petpal-backend -n petpal
        Assert-Success 'restart production backend'
    }

    Wait-ForDeployment -DeploymentName 'petpal-backend' -Step 'production backend rollout'
}

if ($DeployTestBackend -and $DeployProductionBackend) {
    throw 'Choose either test backend deployment or production backend deployment, not both.'
}

if ($DeployTestBackend -and $RunLoadTests) {
    throw 'Test backend deployment cannot be combined with production load tests.'
}

if ($DeployProductionBackend -and $RunTestLoadTests) {
    throw 'Production backend deployment cannot be combined with test load tests.'
}

$ShouldDeployProduction = $DeployProductionBackend -or $RunLoadTests -or (-not $DeployTestBackend -and -not $RunTestLoadTests)
$ShouldBuildImage = $DeployTestBackend -or $ShouldDeployProduction

if ($ShouldBuildImage) {
    Write-Host 'Pointing Docker at the Minikube daemon...'
    & minikube -p minikube docker-env --shell powershell | Invoke-Expression
    Assert-Success 'minikube docker-env'

    Write-Host 'Building backend image...'
    docker build -t petpal-backend:latest .
    Assert-Success 'docker build'
}

if ($DeployTestBackend) {
    Deploy-TestBackend
}
elseif ($ShouldDeployProduction) {
    Deploy-ProductionBackend
}

if ($RunTestLoadTests) {
    Write-Host 'Running test Kubernetes load tests...'
    Wait-ForDeployment -DeploymentName 'petpal-backend-test' -Step 'test backend ready for load tests'
    Invoke-Kubectl -Arguments @('apply', '-f', 'k8s/loadtest-script-configmap.yaml') -Step 'apply loadtest configmap'
    Invoke-LoadTestJob -JobName 'petpal-loadtest-catalog' -ManifestPath 'k8s/loadtest-job-catalog.yaml'
    Invoke-LoadTestJob -JobName 'petpal-loadtest-pet-health' -ManifestPath 'k8s/loadtest-job-pet-health.yaml'
}

if ($RunLoadTests) {
    Write-Host 'Running production Kubernetes load tests...'
    Wait-ForDeployment -DeploymentName 'petpal-backend' -Step 'production backend ready for load tests'
    Invoke-Kubectl -Arguments @('apply', '-f', 'k8s/loadtest-script-configmap.yaml') -Step 'apply loadtest configmap'
    Invoke-LoadTestJob -JobName 'petpal-loadtest-catalog' -ManifestPath 'k8s/loadtest-job-prod-catalog.yaml'
    Invoke-LoadTestJob -JobName 'petpal-loadtest-pet-health' -ManifestPath 'k8s/loadtest-job-prod-pet-health.yaml'
}

if ($PortForward) {
    if ($DeployTestBackend -and -not $ShouldDeployProduction) {
        & kubectl port-forward svc/petpal-backend-test-service 8080:8080 -n petpal
    } else {
        & kubectl port-forward svc/petpal-backend-service 8080:8080 -n petpal
    }
}

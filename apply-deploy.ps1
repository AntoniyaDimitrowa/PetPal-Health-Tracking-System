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

function Get-ResourcePodName {
    param(
        [Parameter(Mandatory = $true)]
        [string]$LabelSelector,
        [Parameter(Mandatory = $true)]
        [string]$Step
    )

    $podName = & kubectl get pods -n petpal -l $LabelSelector -o jsonpath='{.items[0].metadata.name}'
    Assert-Success $Step

    if ([string]::IsNullOrWhiteSpace($podName)) {
        throw "No pod was found for selector '$LabelSelector'."
    }

    return $podName.Trim()
}

function Invoke-MySqlScript {
    param(
        [Parameter(Mandatory = $true)]
        [string]$ScriptPath,
        [Parameter(Mandatory = $true)]
        [string]$Step
    )

    if (-not (Test-Path $ScriptPath)) {
        throw "Script '$ScriptPath' was not found."
    }

    $mysqlPod = Get-ResourcePodName -LabelSelector 'app=mysql-test' -Step 'get mysql-test pod'
    $scriptContent = Get-Content -Raw -Encoding UTF8 $ScriptPath

    $scriptContent | & kubectl exec -i -n petpal $mysqlPod -- env MYSQL_PWD=changeme-test mysql --protocol=tcp -h 127.0.0.1 -uroot petpal-test-db
    Assert-Success $Step
}

function Reset-TestDatabase {
    Invoke-MySqlScript -ScriptPath 'loadtests/test-db-cleanup.sql' -Step 'reset test database'
}

function Seed-TestDatabase {
    Invoke-MySqlScript -ScriptPath 'loadtests/test-db-seed.sql' -Step 'seed test database'
}

function Wait-ForTestSchema {
    $mysqlPod = Get-ResourcePodName -LabelSelector 'app=mysql-test' -Step 'get mysql-test pod'
    $requiredTables = @('mood', 'breed', 'breed_health_info', 'vaccination')

    foreach ($table in $requiredTables) {
        $tableReady = $false

        for ($attempt = 1; $attempt -le 30; $attempt++) {
            $tableCount = & kubectl exec -n petpal $mysqlPod -- env MYSQL_PWD=changeme-test mysql --protocol=tcp -h 127.0.0.1 -uroot --silent --skip-column-names -e "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='petpal-test-db' AND table_name='$table';"
            if ($LASTEXITCODE -eq 0 -and -not [string]::IsNullOrWhiteSpace($tableCount) -and $tableCount.Trim() -eq '1') {
                $tableReady = $true
                break
            }

            Start-Sleep -Seconds 5
        }

        if (-not $tableReady) {
            throw "Schema table '$table' was not created in time."
        }
    }
}

function Wait-ForJobTerminalState {
    param(
        [Parameter(Mandatory = $true)]
        [string]$JobName,
        [int]$TimeoutSeconds = 900
    )

    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)

    while ((Get-Date) -lt $deadline) {
        $complete = & kubectl get job $JobName -n petpal -o jsonpath='{.status.conditions[?(@.type=="Complete")].status}'
        if ($LASTEXITCODE -ne 0) {
            throw "Job '$JobName' could not be queried."
        }

        $failed = & kubectl get job $JobName -n petpal -o jsonpath='{.status.conditions[?(@.type=="Failed")].status}'
        if ($LASTEXITCODE -ne 0) {
            throw "Job '$JobName' could not be queried."
        }

        if (-not [string]::IsNullOrWhiteSpace($complete) -and $complete.Trim() -eq 'True') {
            return 'Complete'
        }

        if (-not [string]::IsNullOrWhiteSpace($failed) -and $failed.Trim() -eq 'True') {
            return 'Failed'
        }

        Start-Sleep -Seconds 5
    }

    throw "Timed out waiting for job '$JobName' to reach a terminal state."
}

function Write-JobLogs {
    param(
        [Parameter(Mandatory = $true)]
        [string]$JobName
    )

    $podName = Get-ResourcePodName -LabelSelector "job-name=$JobName" -Step "get $JobName pod"
    & kubectl logs -n petpal $podName --tail=200
    Assert-Success "logs $JobName"
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

    $logsCaptured = $false

    try {
        $terminalState = Wait-ForJobTerminalState -JobName $JobName -TimeoutSeconds 900
        Write-JobLogs -JobName $JobName
        $logsCaptured = $true

        if ($terminalState -eq 'Failed') {
            throw "Load test job '$JobName' reported a failed status."
        }
    }
    catch {
        Write-Host "Load test job $JobName failed; collecting logs..."
        if (-not $logsCaptured) {
            try {
                Write-JobLogs -JobName $JobName
            } catch {
                Write-Host "Unable to collect logs for $JobName."
            }
        }

        throw
    }
    finally {
        Invoke-Kubectl -Arguments @('delete', 'job', $JobName, '-n', 'petpal', '--ignore-not-found') -Step "delete $JobName"
    }
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
    Wait-ForDeployment -DeploymentName 'mysql-test' -Step 'test mysql ready for load tests'
    Wait-ForDeployment -DeploymentName 'petpal-backend-test' -Step 'test backend ready for load tests'
    Wait-ForTestSchema
    Invoke-Kubectl -Arguments @('apply', '-f', 'k8s/loadtest-script-configmap.yaml') -Step 'apply loadtest configmap'
    try {
        Seed-TestDatabase
        Invoke-LoadTestJob -JobName 'petpal-loadtest-catalog' -ManifestPath 'k8s/loadtest-job-catalog.yaml'
        Invoke-LoadTestJob -JobName 'petpal-loadtest-pet-health' -ManifestPath 'k8s/loadtest-job-pet-health.yaml'
    }
    finally {
        Write-Host 'Cleaning test database after load tests...'
        try {
            Reset-TestDatabase
        } catch {
            Write-Host 'Test database cleanup failed.'
        }
    }
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

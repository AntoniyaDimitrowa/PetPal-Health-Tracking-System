param(
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

Write-Host 'Pointing Docker at the Minikube daemon...'
& minikube -p minikube docker-env --shell powershell | Invoke-Expression
Assert-Success 'minikube docker-env'

Write-Host 'Building backend image...'
docker build -t petpal-backend:latest .
Assert-Success 'docker build'

Write-Host 'Applying core Kubernetes manifests...'
kubectl apply -f k8s/namespace.yaml
Assert-Success 'kubectl apply namespace'

kubectl apply -f k8s/secret.yaml
Assert-Success 'kubectl apply secret'

kubectl apply -f k8s/configmap.yaml
Assert-Success 'kubectl apply configmap'

kubectl apply -f k8s/mysql-pvc.yaml
Assert-Success 'kubectl apply mysql pvc'

kubectl apply -f k8s/mysql-service.yaml
Assert-Success 'kubectl apply mysql service'

kubectl apply -f k8s/mysql-deployment.yaml
Assert-Success 'kubectl apply mysql deployment'

kubectl rollout status deployment/mysql -n petpal --timeout=300s
Assert-Success 'mysql rollout'

kubectl apply -f k8s/backend-service.yaml
Assert-Success 'kubectl apply backend service'

kubectl apply -f k8s/backend-deployment.yaml
Assert-Success 'kubectl apply backend deployment'

kubectl apply -f k8s/ingress.yaml
Assert-Success 'kubectl apply ingress'

if ($RecreateBackend) {
    Write-Host 'Recreating the backend deployment...'
    kubectl delete deployment/petpal-backend -n petpal --ignore-not-found --wait=true
    Assert-Success 'kubectl delete backend deployment'

    kubectl apply -f k8s/backend-deployment.yaml
    Assert-Success 'reapply backend deployment'
}
else {
    Write-Host 'Restarting the backend deployment so it picks up the fresh image...'
    kubectl rollout restart deployment/petpal-backend -n petpal
    Assert-Success 'kubectl rollout restart backend'
}

kubectl rollout status deployment/petpal-backend -n petpal --timeout=300s
Assert-Success 'backend rollout'

if ($RunLoadTests) {
    Write-Host 'Running Kubernetes load tests...'

    kubectl apply -f k8s/loadtest-script-configmap.yaml
    Assert-Success 'kubectl apply loadtest configmap'

    kubectl delete job petpal-loadtest-catalog -n petpal --ignore-not-found
    Assert-Success 'delete catalog loadtest job'

    kubectl apply -f k8s/loadtest-job-prod-catalog.yaml
    Assert-Success 'apply catalog loadtest job'

    kubectl wait --for=condition=complete job/petpal-loadtest-catalog -n petpal --timeout=900s
    Assert-Success 'wait catalog loadtest job'

    kubectl logs -n petpal job/petpal-loadtest-catalog
    Assert-Success 'catalog loadtest logs'

    kubectl delete job petpal-loadtest-pet-health -n petpal --ignore-not-found
    Assert-Success 'delete pet health loadtest job'

    kubectl apply -f k8s/loadtest-job-prod-pet-health.yaml
    Assert-Success 'apply pet health loadtest job'

    kubectl wait --for=condition=complete job/petpal-loadtest-pet-health -n petpal --timeout=900s
    Assert-Success 'wait pet health loadtest job'

    kubectl logs -n petpal job/petpal-loadtest-pet-health
    Assert-Success 'pet health loadtest logs'
}

if ($PortForward) {
    kubectl port-forward svc/petpal-backend-service 8080:8080 -n petpal
}

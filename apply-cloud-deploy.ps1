param(
    [string]$ConfigPath = (Join-Path $PSScriptRoot 'cloud-deploy.config.psd1'),

    [ValidateNotNullOrEmpty()]
    [string]$BackendImage,

    [ValidateNotNullOrEmpty()]
    [string]$FrontendImage,

    [Parameter(Mandatory = $true)]
    [ValidateNotNullOrEmpty()]
    [string]$DbPassword,

    [Parameter(Mandatory = $true)]
    [ValidateNotNullOrEmpty()]
    [string]$JwtSecret,

    [Parameter(Mandatory = $true)]
    [ValidateNotNullOrEmpty()]
    [string]$WeatherApiKey,

    [Parameter(Mandatory = $true)]
    [ValidateNotNullOrEmpty()]
    [string]$OpenRouterApiKey,

    [string]$Namespace = 'petpal',
    [string]$MysqlImage = 'mysql:8.0',
    [string]$CorsAllowedOrigins = '*',
    [string]$DatasourceUrl = 'jdbc:mysql://mysql-service:3306/petpal-db',
    [string]$DatasourceUsername = 'root',
    [string]$WeatherApiUrl = 'https://api.openweathermap.org/data/2.5/weather',
    [string]$OpenRouterApiUrl = 'https://openrouter.ai/api/v1/completions',
    [string]$OpenRouterModel = 'meta-llama/llama-3.2-1b-instruct:free',

    [string]$ProjectId,
    [string]$Region,
    [string]$ArtifactRegistryRepository,
    [string]$BackendImageName,
    [string]$FrontendImageName,
    [string]$ImageTag = 'latest'
)

$ErrorActionPreference = 'Stop'
$ScriptRoot = if ($PSScriptRoot) { $PSScriptRoot } else { (Get-Location).Path }

function Get-ConfigValue {
    param(
        [Parameter(Mandatory = $true)]
        [hashtable]$Config,
        [Parameter(Mandatory = $true)]
        [string]$Key
    )

    if (-not $Config.ContainsKey($Key)) {
        return $null
    }

    $value = $Config[$Key]
    if ($null -eq $value) {
        return $null
    }

    $text = $value.ToString().Trim()
    if ([string]::IsNullOrWhiteSpace($text)) {
        return $null
    }

    return $text
}

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

    & kubectl rollout status "deployment/$DeploymentName" -n $Namespace --timeout=300s
    Assert-Success $Step
}

function Set-DeploymentImage {
    param(
        [Parameter(Mandatory = $true)]
        [string]$DeploymentName,
        [Parameter(Mandatory = $true)]
        [string]$ContainerName,
        [Parameter(Mandatory = $true)]
        [string]$Image
    )

    & kubectl set image "deployment/$DeploymentName" "$ContainerName=$Image" -n $Namespace
    Assert-Success "set $DeploymentName image"
}

function Format-ArtifactRegistryImage {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Region,
        [Parameter(Mandatory = $true)]
        [string]$ProjectId,
        [Parameter(Mandatory = $true)]
        [string]$Repository,
        [Parameter(Mandatory = $true)]
        [string]$ImageName,
        [Parameter(Mandatory = $true)]
        [string]$Tag
    )

    return "$Region-docker.pkg.dev/$ProjectId/$Repository/$ImageName`:$Tag"
}

function Apply-GeneratedSecret {
    & kubectl create secret generic petpal-secret `
        --namespace $Namespace `
        --from-literal="DB_PASSWORD=$DbPassword" `
        --from-literal="JWT_SECRET=$JwtSecret" `
        --from-literal="WEATHER_API_KEY=$WeatherApiKey" `
        --from-literal="OPENROUTER_API_KEY=$OpenRouterApiKey" `
        --dry-run=client -o yaml | & kubectl apply -f -
    Assert-Success 'apply cloud secret'
}

function Apply-GeneratedConfigMap {
    & kubectl create configmap petpal-config `
        --namespace $Namespace `
        --from-literal="SPRING_DATASOURCE_URL=$DatasourceUrl" `
        --from-literal="SPRING_DATASOURCE_USERNAME=$DatasourceUsername" `
        --from-literal="WEATHER_API_URL=$WeatherApiUrl" `
        --from-literal="OPENROUTER_API_URL=$OpenRouterApiUrl" `
        --from-literal="OPENROUTER_MODEL=$OpenRouterModel" `
        --from-literal="CORS_ALLOWED_ORIGINS=$CorsAllowedOrigins" `
        --dry-run=client -o yaml | & kubectl apply -f -
    Assert-Success 'apply cloud configmap'
}

if (Test-Path -LiteralPath $ConfigPath) {
    $config = Import-PowerShellDataFile -Path $ConfigPath

    if (-not $ProjectId) {
        $ProjectId = Get-ConfigValue -Config $config -Key 'ProjectId'
    }

    if (-not $Region) {
        $Region = Get-ConfigValue -Config $config -Key 'Region'
    }

    if (-not $ArtifactRegistryRepository) {
        $ArtifactRegistryRepository = Get-ConfigValue -Config $config -Key 'ArtifactRegistryRepository'
    }

    if (-not $BackendImageName) {
        $BackendImageName = Get-ConfigValue -Config $config -Key 'BackendImageName'
    }

    if (-not $FrontendImageName) {
        $FrontendImageName = Get-ConfigValue -Config $config -Key 'FrontendImageName'
    }

    if (-not $ImageTag) {
        $ImageTag = Get-ConfigValue -Config $config -Key 'ImageTag'
    }
}

if (-not $ImageTag) {
    $ImageTag = 'latest'
}

if (-not $BackendImage) {
    if ([string]::IsNullOrWhiteSpace($ProjectId) -or [string]::IsNullOrWhiteSpace($Region) -or [string]::IsNullOrWhiteSpace($ArtifactRegistryRepository)) {
        throw 'Provide -BackendImage/-FrontendImage explicitly or define ProjectId, Region, and ArtifactRegistryRepository in cloud-deploy.config.psd1.'
    }

    if ([string]::IsNullOrWhiteSpace($BackendImageName)) {
        $BackendImageName = 'petpal-backend'
    }

    $BackendImage = Format-ArtifactRegistryImage -Region $Region -ProjectId $ProjectId -Repository $ArtifactRegistryRepository -ImageName $BackendImageName -Tag $ImageTag
}

if (-not $FrontendImage) {
    if ([string]::IsNullOrWhiteSpace($ProjectId) -or [string]::IsNullOrWhiteSpace($Region) -or [string]::IsNullOrWhiteSpace($ArtifactRegistryRepository)) {
        throw 'Provide -BackendImage/-FrontendImage explicitly or define ProjectId, Region, and ArtifactRegistryRepository in cloud-deploy.config.psd1.'
    }

    if ([string]::IsNullOrWhiteSpace($FrontendImageName)) {
        $FrontendImageName = 'petpal-frontend'
    }

    $FrontendImage = Format-ArtifactRegistryImage -Region $Region -ProjectId $ProjectId -Repository $ArtifactRegistryRepository -ImageName $FrontendImageName -Tag $ImageTag
}

Write-Host "Deploying PetPal to namespace '$Namespace'..."
Write-Host "Backend image: $BackendImage"
Write-Host "Frontend image: $FrontendImage"

Invoke-Kubectl -Arguments @('apply', '-f', (Join-Path $ScriptRoot 'k8s/namespace.yaml')) -Step 'apply namespace'
Apply-GeneratedSecret
Apply-GeneratedConfigMap

Invoke-Kubectl -Arguments @('apply', '-f', (Join-Path $ScriptRoot 'k8s/mysql-pvc.yaml')) -Step 'apply mysql pvc'
Invoke-Kubectl -Arguments @('apply', '-f', (Join-Path $ScriptRoot 'k8s/mysql-service.yaml')) -Step 'apply mysql service'
Invoke-Kubectl -Arguments @('apply', '-f', (Join-Path $ScriptRoot 'k8s/mysql-deployment.yaml')) -Step 'apply mysql deployment'
Invoke-Kubectl -Arguments @('apply', '-f', (Join-Path $ScriptRoot 'k8s/backend-service.yaml')) -Step 'apply backend service'
Invoke-Kubectl -Arguments @('apply', '-f', (Join-Path $ScriptRoot 'k8s/backend-deployment.yaml')) -Step 'apply backend deployment'
Invoke-Kubectl -Arguments @('apply', '-f', (Join-Path $ScriptRoot 'k8s/frontend-service.yaml')) -Step 'apply frontend service'
Invoke-Kubectl -Arguments @('apply', '-f', (Join-Path $ScriptRoot 'k8s/frontend-deployment.yaml')) -Step 'apply frontend deployment'
Invoke-Kubectl -Arguments @('apply', '-f', (Join-Path $ScriptRoot 'k8s/cloud/ingress.yaml')) -Step 'apply cloud ingress'

Set-DeploymentImage -DeploymentName 'mysql' -ContainerName 'mysql' -Image $MysqlImage
Set-DeploymentImage -DeploymentName 'petpal-backend' -ContainerName 'petpal-backend' -Image $BackendImage
Set-DeploymentImage -DeploymentName 'petpal-frontend' -ContainerName 'petpal-frontend' -Image $FrontendImage

Wait-ForDeployment -DeploymentName 'mysql' -Step 'mysql rollout'
Wait-ForDeployment -DeploymentName 'petpal-backend' -Step 'backend rollout'
Wait-ForDeployment -DeploymentName 'petpal-frontend' -Step 'frontend rollout'

Write-Host ''
Write-Host 'Cloud deployment finished.'
Write-Host "Check the ingress address with: kubectl get ingress petpal-ingress -n $Namespace"

# Cloud Deployment Runbook

This branch is set up for a cloud Kubernetes deployment of the monolith stack.
The app now reads runtime configuration from environment variables, and the
cloud helper script applies the stack against a GKE cluster.

In practice, this migrated PetPal from a local Kubernetes setup to GKE on
Google Cloud. The pipeline now builds backend and frontend images, pushes them
to Artifact Registry, and deploys the full runtime stack with MySQL, backend,
frontend, and ingress through GitHub Actions authenticated with Workload
Identity Federation.

## What Changed In The Repo

1. `WebSecurityConfig` and `WebSocketConfig` now read allowed origins from
   `cors.allowed-origins` instead of hardcoding `petpal.local` and localhost.
2. `application.properties`, `application-dev.properties`,
   `application-staging.properties`, and `application-loadtest.properties`
   now use environment-backed datasource, JWT, weather, and OpenRouter values.
3. `k8s/configmap.yaml` now exposes `CORS_ALLOWED_ORIGINS` for the backend.
4. `k8s/backend-deployment.yaml` now passes `CORS_ALLOWED_ORIGINS` into the
   container.
5. `k8s/frontend-deployment.yaml`, `k8s/mysql-deployment.yaml`, and
   `k8s/mysql-test.yaml` now use cloud-safe image defaults.
6. `apply-cloud-deploy.ps1` was added to apply the cloud stack in one run.
7. `k8s/cloud/ingress.yaml` was added so cloud ingress does not depend on the
   local-only `petpal.local` host.

## What You Still Need

The non-secret cloud values now live in [cloud-deploy.config.psd1](../cloud-deploy.config.psd1).
That file includes:

- project ID
- region
- GKE cluster name
- Workload Identity Provider
- Google Cloud service account email
- Artifact Registry repository
- backend and frontend image names
- frontend repo URL and branch

You still need the secrets that the application expects. Put them in GitHub
Actions under the `cloud` environment:

- database password
- JWT secret
- Weather API key
- OpenRouter API key

The frontend source is not inside this repository, but the workflow now clones
the public frontend repo automatically.

The cloud workflow uses Workload Identity Federation instead of a downloaded
Google Cloud service account key. Create a workload identity pool/provider in
Google Cloud and add its full resource name plus the impersonated service
account email to [cloud-deploy.config.psd1](../cloud-deploy.config.psd1).
The old `GCP_SA_KEY` secret is not used by this workflow.
The GitHub workflow also installs the `gke-gcloud-auth-plugin` component so
`kubectl` can authenticate to the GKE cluster.

For local work, use the checked-in [`.env.example`](../.env.example) as the
template and the ignored root [`.env`](../.env) file as the working copy. The
local PowerShell deploy script reads `.env` automatically.

## Build And Push Images

The shared registry settings live in [cloud-deploy.config.psd1](../cloud-deploy.config.psd1).
The script reads that file automatically, so the image names stay short:

- backend: `petpal-backend`
- frontend: `petpal-frontend`

```powershell
gcloud auth configure-docker europe-west4-docker.pkg.dev

docker build -t europe-west4-docker.pkg.dev/project-2467249b-6a0f-4637-aeb/petpal/petpal-backend:latest .
docker push europe-west4-docker.pkg.dev/project-2467249b-6a0f-4637-aeb/petpal/petpal-backend:latest

# Run this from the separate frontend repo
docker build -t europe-west4-docker.pkg.dev/project-2467249b-6a0f-4637-aeb/petpal/petpal-frontend:latest .
docker push europe-west4-docker.pkg.dev/project-2467249b-6a0f-4637-aeb/petpal/petpal-frontend:latest
```

## Deploy To Cloud

1. Fill in `ClusterName` in `cloud-deploy.config.psd1` with your real GKE
   cluster name.
2. Create a GitHub Actions environment named `cloud` and add the four app
   secrets there.
3. Create a Workload Identity pool/provider in Google Cloud for GitHub Actions
   and grant the impersonated service account Artifact Registry write access
   plus GKE access for the target cluster.
4. Fill in `WorkloadIdentityProvider`, `ServiceAccountEmail`, and `ClusterName`
   in `cloud-deploy.config.psd1`.
5. Run the cloud workflow or the cloud helper script.
6. Wait for the backend, frontend, and MySQL rollouts to finish.
7. Open the ingress address in the browser.

## Live URL

The deployed site is **not** `petpal.local`. In the cloud, the frontend is
exposed through the GKE ingress address assigned to `petpal-ingress`.

Use one of these after deployment:

```powershell
kubectl get ingress petpal-ingress -n petpal
```

If you want just the address, use:

```powershell
kubectl get ingress petpal-ingress -n petpal -o jsonpath='{.status.loadBalancer.ingress[0].ip}{.status.loadBalancer.ingress[0].hostname}'
```

The frontend is served at:

```text
http://INGRESS_ADDRESS/
```

The backend is reachable through the same ingress at:

```text
http://INGRESS_ADDRESS/backend
```

If the frontend repo still contains a hardcoded local API base URL, update that
repo to point at the cloud ingress address before testing frontend-to-backend
requests.

Example:

```powershell
.\apply-cloud-deploy.ps1 `
  -DbPassword 'your-db-password' `
  -JwtSecret 'your-jwt-secret' `
  -WeatherApiKey 'your-weather-api-key' `
  -OpenRouterApiKey 'your-openrouter-api-key' `
  -CorsAllowedOrigins '*'
```

## Notes

- The repository settings are stored in `cloud-deploy.config.psd1`, which the
  deploy script reads automatically.
- `CORS_ALLOWED_ORIGINS='*'` is the fastest way to get the cloud deployment
  working. Narrow it later if you want a stricter browser policy.
- If you want to use a single exact frontend origin instead, pass that value to
  `-CorsAllowedOrigins`.
- The local Minikube workflow still exists in `apply-deploy.ps1`.
- MySQL uses `Recreate` in cloud because a rolling update can hit GKE Autopilot
  quota/capacity limits when the database pod is replaced.
- Backend and frontend also use `Recreate` in cloud for the same reason: this
  project runs with one replica per service, so rolling updates can stall if
  the cluster cannot fit old and new pods at once.
- If image pulls fail with `403 Forbidden`, grant `Artifact Registry Reader` to
  the GKE node service account that is pulling `petpal/*` images.


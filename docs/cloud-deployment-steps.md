# PetPal Cloud Deployment Documentation

## Summary

PetPal was migrated from a local Kubernetes deployment to a cloud deployment on
Google Kubernetes Engine (GKE). The final setup builds Docker images for both
the backend and frontend, pushes them to Google Artifact Registry, and deploys
the application stack to a GKE Autopilot cluster through GitHub Actions. The
cloud environment now runs MySQL, the Spring Boot backend, the frontend, and an
NGINX ingress entry point. After the final fixes, the deployed frontend loads
from the cloud address and successfully sends requests to the deployed backend.

## Final Result

- Cloud provider: Google Cloud Platform
- Kubernetes platform: GKE Autopilot
- GCP project: `project-2467249b-6a0f-4637-aeb`
- Region: `europe-west4`
- Cluster: `petpal-cluster-1`
- Artifact Registry repository: `petpal`
- Backend image: `petpal-backend`
- Frontend image: `petpal-frontend`
- Kubernetes namespace: `petpal`
- Public entry point: NGINX ingress controller external IP

The application is no longer accessed through `petpal.local`. That address was
only useful for the local Kubernetes setup. In the cloud, the site is opened
through the external IP assigned to the ingress controller.

Useful checks:

```bash
kubectl get pods -n petpal
kubectl get pods -n ingress-nginx
kubectl get ingress petpal-ingress -n petpal
kubectl get svc ingress-nginx-controller -n ingress-nginx
```

Expected application URLs:

```text
http://INGRESS_EXTERNAL_IP/
http://INGRESS_EXTERNAL_IP/backend
```

## Architecture

The cloud deployment keeps the monolith architecture because it was the most
stable and realistic option for the project deadline. The microservice branch
can still be demonstrated locally, but the cloud deployment focuses on the
working monolith stack.

The deployed runtime contains:

- `mysql`: MySQL 8 database with a persistent volume claim
- `petpal-backend`: Spring Boot backend running on port `8080`
- `petpal-frontend`: frontend served through an NGINX container on port `80`
- `petpal-ingress`: Kubernetes ingress routing `/` to the frontend and
  `/backend` to the backend
- `ingress-nginx-controller`: external load balancer entry point for the app

The local-only test workloads, such as `backend-test` and `mysql-test`, are not
part of the cloud runtime deployment. The cloud deployment focuses on the
production-like application stack.

## Repository Changes

The cloud deployment required several repo changes:

1. Added [cloud-deploy.config.psd1](../cloud-deploy.config.psd1) to keep
   non-secret cloud configuration in one place.
2. Added [apply-cloud-deploy.ps1](../apply-cloud-deploy.ps1) to apply the cloud
   Kubernetes stack consistently.
3. Added [ci-cloud.yml](../.github/workflows/ci-cloud.yml) for GitHub Actions
   cloud CI/CD.
4. Added [k8s/cloud/ingress.yaml](../k8s/cloud/ingress.yaml) for the cloud
   ingress route.
5. Updated the backend configuration so runtime values are read from
   environment variables instead of hardcoded local values.
6. Moved application secrets out of committed Kubernetes YAML and into GitHub
   Actions environment secrets.
7. Added `.env.example` for local development and kept the real `.env` ignored.
8. Updated Kubernetes deployments to use cloud image paths and Autopilot-safe
   rollout behavior.

## Secret And Configuration Handling

Normal developers do not commit real passwords, API keys, or tokens to GitHub.
For this deployment, secrets were moved into the GitHub Actions `cloud`
environment.

GitHub environment secrets used by the workflow:

- `DB_PASSWORD`
- `JWT_SECRET`
- `WEATHER_API_KEY`
- `OPENROUTER_API_KEY`

Non-secret deployment values are stored in
[cloud-deploy.config.psd1](../cloud-deploy.config.psd1), including:

- GCP project ID
- GCP region
- GKE cluster name
- Workload Identity Provider
- Google Cloud service account email
- Artifact Registry repository
- backend and frontend image names
- frontend repository URL and branch

The old service account key approach was not used. Google Cloud blocked service
account key creation because of organization policy, so the workflow was changed
to use Workload Identity Federation instead.

## Google Cloud Setup

The following Google Cloud resources were prepared:

1. Enabled the required APIs, including Kubernetes Engine and Artifact Registry.
2. Created an Artifact Registry Docker repository named `petpal`.
3. Created the GKE Autopilot cluster `petpal-cluster-1` in `europe-west4`.
4. Created the service account
   `petpal-github-actions@project-2467249b-6a0f-4637-aeb.iam.gserviceaccount.com`.
5. Granted the deployment service account access to push images and deploy to
   GKE.
6. Created a Workload Identity Federation pool and provider for GitHub Actions.
7. Allowed the backend GitHub repository to impersonate the Google Cloud service
   account.
8. Granted Artifact Registry read access to the GKE node service account so pods
   could pull private images.

## CI/CD Pipeline

The cloud pipeline is defined in
[.github/workflows/ci-cloud.yml](../.github/workflows/ci-cloud.yml).

The workflow runs on pushes to the cloud deployment branch and can also be run
manually from GitHub Actions.

Pipeline steps:

1. Check out the backend repository.
2. Load shared values from [cloud-deploy.config.psd1](../cloud-deploy.config.psd1).
3. Authenticate to Google Cloud with Workload Identity Federation.
4. Install the GKE auth plugin so `kubectl` can connect to the cluster.
5. Configure Docker authentication for Artifact Registry.
6. Build and push the backend image.
7. Clone the public frontend repository.
8. Build and push the frontend image.
9. Get GKE credentials for the target cluster.
10. Apply the Kubernetes stack through [apply-cloud-deploy.ps1](../apply-cloud-deploy.ps1).
11. Print the deployed image names and live address in the GitHub Actions
    summary.

The backend and frontend images are tagged with the Git commit SHA. This makes
each deployment traceable to the exact commit that produced it.

## Kubernetes Deployment

The cloud deployment script applies these resources:

- namespace
- generated Kubernetes secret
- generated Kubernetes config map
- MySQL persistent volume claim
- MySQL service and deployment
- backend service and deployment
- frontend service and deployment
- cloud ingress

MySQL uses the public `mysql:8.0` image from the container registry, not a local
Docker image. The backend and frontend use images built by GitHub Actions and
pushed to Artifact Registry.

The script waits for MySQL, backend, and frontend rollouts. If a rollout fails,
it prints diagnostics such as pod status, deployment description, pod
description, and recent logs.

## Ingress And Public Access

The repo creates a Kubernetes ingress named `petpal-ingress` with
`ingressClassName: nginx`. A plain ingress resource is not enough by itself; the
cluster also needs an ingress controller.

An NGINX ingress controller was installed in the cluster. It created a Google
Cloud load balancer and exposed the app through an external IP.

The route setup is:

- `/` routes to `petpal-frontend-service`
- `/backend` routes to `petpal-backend-service`

The frontend was also updated in the frontend repository so its backend base URL
points to the cloud backend instead of `petpal.local`.

## Challenges And Fixes

### Service account key creation was blocked

Google Cloud did not allow creating a downloadable service account key because
of organization policy.

Fix: replaced `GCP_SA_KEY` authentication with Workload Identity Federation.
This is also a better security practice because GitHub Actions does not need a
long-lived JSON key.

### `kubectl` could not authenticate to GKE

The first cloud workflow failed because the GitHub runner did not have
`gke-gcloud-auth-plugin`.

Fix: updated the workflow to install the GKE auth plugin through
`google-github-actions/setup-gcloud`.

### MySQL rollout timed out

The MySQL pod initially took too long to become ready, and rolling updates caused
extra pods to be scheduled. On GKE Autopilot this created capacity and quota
problems.

Fix: added cloud-safe MySQL settings and changed MySQL to `Recreate` strategy so
only one database pod is scheduled at a time.

### PVC wait happened too early

The first deployment script waited for the MySQL persistent volume claim before
the database pod consumed it. With cloud storage classes, volume binding can
happen only after a pod is scheduled.

Fix: removed the premature PVC wait and let Kubernetes bind the volume during
pod scheduling.

### Backend and frontend rollouts hit capacity limits

Rolling updates for backend and frontend could temporarily require both the old
and new pods at the same time. The small Autopilot cluster did not always have
enough available quota for that.

Fix: changed backend and frontend deployments to `Recreate` strategy for this
project. Since each service runs one replica, this is simpler and avoids
unnecessary extra pod scheduling.

### Pods could not pull images from Artifact Registry

The backend pod failed with `403 Forbidden` while pulling the backend image from
Artifact Registry.

Fix: granted `Artifact Registry Reader` to the GKE node service account so GKE
could pull images from the private Artifact Registry repository.

### Ingress had no external address

The `petpal-ingress` resource existed, but it had no address. The cluster did
not have an ingress controller installed, so nothing was processing the ingress.

Fix: installed the NGINX ingress controller and checked the external IP from the
`ingress-nginx-controller` load balancer service.

### Ingress controller setup was blocked by quota

The NGINX admission jobs were temporarily unschedulable because GKE reported
quota and capacity limits. This prevented the admission secret from being
created, so the ingress controller stayed in `ContainerCreating`.

Fix: activated the Google Cloud billing account, temporarily scaled down the
backend and frontend to free cluster resources, allowed the ingress controller
to roll out, and then scaled the application back up.

### Frontend still pointed to the local backend

The deployed frontend initially still used the local URL `petpal.local`.

Fix: updated the frontend repository so the frontend sends API requests to the
cloud backend address. After rerunning the cloud pipeline, the frontend image
was rebuilt and deployed with the updated backend URL.

## Final Validation

The final deployment was validated by checking:

- GitHub Actions cloud workflow completed successfully.
- Backend image was built and pushed to Artifact Registry.
- Frontend image was built and pushed to Artifact Registry.
- MySQL, backend, and frontend pods were running in the `petpal` namespace.
- NGINX ingress controller was running in the `ingress-nginx` namespace.
- The load balancer external IP opened the deployed frontend in the browser.
- Frontend requests reached the backend through `/backend`.
- The application worked from the cloud deployment instead of the local cluster.


## Conclusion

The cloud migration is complete. PetPal now has a repeatable deployment path
from GitHub to Google Cloud: source code is pushed, GitHub Actions builds the
backend and frontend images, images are stored in Artifact Registry, and the
application is deployed to GKE. The final deployed version runs successfully in
the cloud, and the frontend sends requests to the backend without needing the
local Kubernetes environment.

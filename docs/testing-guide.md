# Testing Guide

Use this guide to run the BDD tests and the Kubernetes load tests, then collect the output for a video or report.

## Prerequisites

- The test namespace, MySQL test deployment, and backend test deployment must already be running in Kubernetes.
- The test database must contain the catalog data expected by the tests, especially:
  - `German Shepherd`
  - `Golden Retriever`
  - `Beagle`
  - `Rabies`
  - `Bordetella`
- For local BDD runs, keep the backend test service accessible through port-forwarding.

## BDD Tests

### Run in browser mode

This is the easiest option if you want to record the test execution on screen.

```powershell
kubectl port-forward -n petpal service/petpal-backend-test-service 8081:8080
```

Open a second terminal and run:

```powershell
$env:CYPRESS_API_BASE_URL = "http://127.0.0.1:8081/backend"
npm run bdd:open
```

What you will see:

- The Cypress runner with each `.feature` file.
- Each `Given`, `When`, and `Then` step passing or failing live.
- Any failed assertions with the exact step that failed.

### Run headless

Use this if you want a clean terminal log and saved artifacts.

```powershell
$env:CYPRESS_API_BASE_URL = "http://127.0.0.1:8081/backend"
npm run bdd:test
```

Results are saved in:

- `cypress/videos/`
- `cypress/screenshots/`

If you want the terminal output saved as well:

```powershell
npm run bdd:test | Tee-Object -FilePath bdd-results.log
```

## Load Tests

Run the load test inside Kubernetes:

```powershell
kubectl apply -f k8s/loadtest-script-configmap.yaml
kubectl apply -f k8s/loadtest-job-catalog.yaml
kubectl logs -f job/petpal-loadtest-catalog -n petpal
```

To run the write-heavy version:

```powershell
kubectl apply -f k8s/loadtest-script-configmap.yaml
kubectl apply -f k8s/loadtest-job-pet-health.yaml
kubectl logs -f job/petpal-loadtest-pet-health -n petpal
```

What you will see:

- k6 progress while the scenario is running.
- A summary with request duration, failure rate, and threshold results at the end.

If you want to save the output to a file at the same time:

```powershell
kubectl logs -f job/petpal-loadtest-catalog -n petpal | Tee-Object -FilePath loadtest-catalog-results.log
```

```powershell
kubectl logs -f job/petpal-loadtest-pet-health -n petpal | Tee-Object -FilePath loadtest-pet-health-results.log
```

## What To Put In Your Report

Use this structure for your write-up:

### 1. Goal

Describe what you tested and why.

### 2. Environment

List the Kubernetes namespace, services, test data, browser, and machine used.

### 3. BDD Results

| Scenario | Expected | Actual | Pass/Fail | Evidence |
| --- | --- | --- | --- | --- |
| Login / authorization | User can log in and protected actions are allowed or blocked correctly |  |  | Cypress screenshot/video |
| Pet creation | User can create a pet with a valid breed |  |  | Cypress screenshot/video |
| Health records | User can add and view a health record |  |  | Cypress screenshot/video |
| Vaccinations | User can view and add vaccination data |  |  | Cypress screenshot/video |

### 4. Load Test Results

| Metric | Result |
| --- | --- |
| Peak VUs |  |
| Error rate |  |
| p95 response time |  |
| Threshold status |  |

### 5. Conclusion

State whether the application passed the functional tests and whether the backend handled the tested load without violating the thresholds.

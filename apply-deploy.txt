& minikube -p minikube docker-env | Invoke-Expression
docker build -t semester3-individualproject-petpal:latest .
kubectl rollout restart deployment petpal-backend -n petpal
kubectl port-forward svc/petpal-backend-service 8080:8080 -n petpal
pause
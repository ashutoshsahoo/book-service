#!/bin/bash

echo "Removing application deployment"
kubectl config set-context --current --namespace=book-svc
kubectl delete -f service/deployment.yaml
kubectl delete -f authentication/deployment.yaml
kubectl delete -f pgadmin4-deployment.yaml
kubectl delete -f postgres-deployment.yaml
kubectl delete -f secrets.yaml

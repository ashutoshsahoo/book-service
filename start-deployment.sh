#!/bin/bash

echo "Building application"
mvn clean package -DskipTests
printf "Finished application building\nStarting deployment\n"
docker pull postgres:12
kubectl create namespace book-svc
kubectl config set-context --current --namespace=book-svc
kubectl apply -f kubernetes-rbac.yaml
kubectl apply -f secrets.yaml
kubectl apply -f postgres-deployment.yaml
kubectl apply -f authentication/deployment.yaml
kubectl apply -f service/deployment.yaml

printf "Finished application deployment\nChecking status\n"
kubectl get po,svc

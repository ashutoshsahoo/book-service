#!/bin/bash
echo "Building application"
mvn clean package -DskipTests
printf "Finished application\nStarting deployment\n"
kubectl apply -f secrets.yaml
kubectl apply -f postgres-deployment.yaml
kubectl apply -f authentication/deployment.yaml
kubectl apply -f service/deployment.yaml

printf "Finished application deployment\nChecking status\n"
kubectl get po,svc

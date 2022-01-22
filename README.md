# Book Service

Sample project to use spring boot with

- Spring security and JWT
- Spring data jpa
- flyway
- postgresql
- h2
- hazelcast
- jib maven plugin
- kubernetes

## Build the project

Ensure kubernetes and docker environment variables set.

```shell

mvn clean package -DskipTests

```

## Enable Hazelcast Kubernetes discovery

```shell

kubectl apply -f kubernetes-rbac.yaml
```

## Deploy application

```shell

kubectl apply -f secrets.yaml
kubectl apply -f postgres-deployment.yaml
kubectl apply -f authentication/deployment.yaml
kubectl apply -f service/deployment.yaml

```

It will take some time to start the application, To check application status:

```shell

kubectl get po,svc

```

The output should be like:

```shell

NAME                                READY   STATUS    RESTARTS   AGE
pod/auth-service-7459958db-kd58f    1/1     Running   0          6m25s
pod/auth-service-7459958db-sjvsb    1/1     Running   0          6m25s
pod/book-service-776bdbf7bb-f9jnq   1/1     Running   0          5m59s
pod/book-service-776bdbf7bb-hcqx2   1/1     Running   0          5m59s
pod/postgres-779fdfc844-bxt8n       1/1     Running   0          71m

NAME                             TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)        AGE
service/auth-service             NodePort    10.102.102.133   <none>        80:31001/TCP   6m25s
service/book-service             NodePort    10.105.109.185   <none>        80:31002/TCP   5m59s
service/hazelcast-auth-service   ClusterIP   10.105.22.147    <none>        5701/TCP       6m25s
service/hazelcast-book-service   ClusterIP   10.103.55.1      <none>        5701/TCP       5m59s
service/kubernetes               ClusterIP   10.96.0.1        <none>        443/TCP        35d
service/postgres-service         ClusterIP   10.105.241.167   <none>        5432/TCP       71m

```

## Test application

Use the provided postman collection to test the application.

## Check database setup with pgAdmin4

```shell

kubectl apply -f pgadmin4-deployment.yaml

```

Open [http://KUBERNETES_NODE_IP:31000](http://localhost:31000) in browser and login with `pgadmin@example.org`
and `changeme`. Select servers (on left pane) and input password as `changeme`.

## Clean up system

```shell

kubectl delete -f service/deployment.yaml
kubectl delete -f authentication/deployment.yaml
kubectl delete -f pgadmin4-deployment.yaml
kubectl delete -f postgres-deployment.yaml
kubectl delete -f secrets.yaml

```

## Script to start and stop the deployment

Use the provided scripts `start-deployment.sh` and `stop-deployment.sh` to start and stop the deployment respectively.
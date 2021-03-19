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

```sh

mvn clean package -DskipTests

```

## Deploy application

```sh

kubectl apply -f secrets.yaml
kubectl apply -f postgres-deployment.yaml
kubectl apply -f authentication/deployment.yaml
kubectl apply -f service/deployment.yaml

```

Check application status:

```sh

kubectl get po,svc

```

The output should be like:

```sh

NAME                                READY   STATUS    RESTARTS   AGE
pod/auth-service-7b64f79ccb-9pgz9   1/1     Running   0          47s
pod/auth-service-7b64f79ccb-w4852   1/1     Running   0          47s
pod/book-service-77bbf9d674-4wl85   1/1     Running   0          35s
pod/book-service-77bbf9d674-sf6ql   1/1     Running   0          36s
pod/postgres-65bd5b7547-85dcf       1/1     Running   0          95s

NAME                             TYPE           CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
service/auth-service             NodePort       10.96.8.95      <none>        80:31001/TCP     47s
service/book-service             NodePort       10.104.33.104   <none>        80:31002/TCP     36s
service/hazelcast-auth-service   LoadBalancer   10.105.34.134   localhost     5701:30835/TCP   47s
service/hazelcast-book-service   LoadBalancer   10.96.153.174   localhost     5701:30728/TCP   36s
service/postgres-service         ClusterIP      10.110.35.193   <none>        5432/TCP         95s

```

## Test application

Use the provided postman collection to test the application.

## Check database setup with pgAdmin4

```sh

kubectl apply -f pgadmin4-deployment.yaml

```

Open [http://<kubernetes-node-ip>:31000](http://localhost:31000) in browser and login with `pgadmin@example.org` and `changeme`. Select servers and input password as `changeme`.

## Clean up system

```sh

kubectl delete -f service/deployment.yaml
kubectl delete -f authentication/deployment.yaml
kubectl delete -f pgadmin4-deployment.yaml
kubectl delete -f postgres-deployment.yaml
kubectl delete -f secrets.yaml

```

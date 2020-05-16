## Spring Boot JPA practice

Sample project to use spring boot with 
* spring data jpa
* flyway
* postgresql
* h2
* jib maven plugin

### Build the maven project (ensure docker environment is set):

```sh

mvn clean package -DskipTests

```

### Run the docker image :

```sh

docker run -p 8080:8080 ashutoshsahoo/spring-boot-app

```
Check application status:
```sh

curl 'localhost:8080/api/v1/books' \
--header 'Accept: application/json' \
--header 'Content-Type: application/json'

```
The response should be 

```json

[{"id":1000,"isbn":"9780596520688","name":"Physics Book name","author":"Ashutosh Sahoo"}]

```


To run with postgresql(update url, username and password ) :

```sh

docker run -p 8080:8080 \
-e spring_datasource_url=jdbc:postgresql://localhost/booksdb \
-e spring_datasource_username=springuser \
-e spring_datasource_password=springuser \
-e spring_flyway_locations=classpath:db/migration/postgresql \
ashutoshsahoo/spring-boot-app

```
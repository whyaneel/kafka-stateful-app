# Use Case
For an e-Banking Portal, implement a REST API for returning list of transactions created in an arbitrary calendar month for a given customer who is logged-on in the portal. The list of transactions should be consumed from a Kafka topic. 

Build a Docker image out of the application and prepare the configuration for deploying it to Kubernetes / OpenShift.


# Setup

Just **follow all the steps in sequence** to complete the exercise.

### Startup script 

This will compile spring-boot application and converts as Docker Image.

As well as brings up the whole env as per docker-compose.yml (detach mode), which includes single-node Kafka Cluster.

```shell script
cd kafka-stateful-app

./local/startup.sh
```

### Kafka Topics Creation (Included in Startup)
```
docker exec demo-kafka kafka-topics --zookeeper demo-zookeeper:2182 --create --topic ACCOUNT_TRANSACTIONS --partitions 1 --replication-factor 1 --if-not-exists

docker exec demo-kafka kafka-topics --zookeeper demo-zookeeper:2182 --create --topic ACCOUNT_TRANSACTIONS_TABLE --partitions 1 --replication-factor 1 --if-not-exists

docker exec demo-kafka kafka-topics --zookeeper demo-zookeeper:2182 --create --topic MONTHLY_ACCOUNT_TRANSACTIONS_TABLE --partitions 1 --replication-factor 1 --if-not-exists
```

### Logs

As the docker-compose in detach mode, Please have a look at logs if you're curious what's happening behind the scenes

```shell script
docker-compose logs -f
```

### Docker Containers
```shell script
whyaneel@aneel-mbp kafka-stateful-app % cd local 
whyaneel@aneel-mbp local % docker ps -a  
CONTAINER ID   IMAGE                                 COMMAND                  CREATED          STATUS          PORTS                                                                                  NAMES
dc5663e5cfcb   kafka-statements-service-app:latest   "java -cp /app/resou…"   24 minutes ago   Up 23 minutes   0.0.0.0:1025->1025/tcp, :::1025->1025/tcp, 0.0.0.0:5005->5005/tcp, :::5005->5005/tcp   kafka-statements-service
960d26e0084a   confluentinc/ksqldb-cli:0.12.0        "/bin/sh"                24 minutes ago   Up 24 minutes                                                                                          demo-ksqldb-cli
8913707f6c9a   confluentinc/ksqldb-server:0.12.0     "/usr/bin/docker/run"    24 minutes ago   Up 24 minutes   0.0.0.0:8089->8089/tcp, :::8089->8089/tcp                                              demo-ksqldb-server
ddc9d42b4a95   confluentinc/cp-kafka-rest:5.5.1      "/etc/confluent/dock…"   24 minutes ago   Up 24 minutes   0.0.0.0:8082->8082/tcp, :::8082->8082/tcp                                              demo-restproxy
51915af92da3   confluentinc/cp-kafka:5.5.1           "/etc/confluent/dock…"   24 minutes ago   Up 24 minutes   9092/tcp, 0.0.0.0:9093->9093/tcp, :::9093->9093/tcp                                    demo-kafka
6d0c85d65b52   confluentinc/cp-zookeeper:5.5.1       "/etc/confluent/dock…"   24 minutes ago   Up 24 minutes   2181/tcp, 2888/tcp, 3888/tcp, 0.0.0.0:2182->2182/tcp, :::2182->2182/tcp                demo-zookeeper
7a4d1f6f3cfb   rodolpheche/wiremock                  "/docker-entrypoint.…"   24 minutes ago   Up 24 minutes   8443/tcp, 0.0.0.0:10001->8080/tcp, :::10001->8080/tcp                                  exchange-rate-service-mock
```

### Mock Data Ingestion For Account Transactions (Included in Startup)
```shell script
./kafka-stateful-app/local/mock-data/ACCOUNT_TRANSACTIONS.sh
```


### Debug with ksqlDB CLI

This brings up ksqlDB CLI

```shell script
docker exec -it demo-ksqldb-cli ksql http://ksqldb-server:8089
```

Let's see whether the produced mock data (raw messages) can be seen in the Topic `ACCOUNT_TRANSACTIONS`

Ideally messages should be present, otherwise debug and ensure you get the messages to the Topic

```shell script
ksql> list topics;

ksql> print  ACCOUNT_TRANSACTIONS from beginning;
```

### Shutdown script 

This will bring down the whole env as per docker-compose.yml, including volumes used.

```shell script
./local/shutdown.sh
```


# Stateful Implementation
### Understand Topology

A topology is an acyclic graph of sources, processors, and sinks.

### State Store

state.dir can be changed to any path on disk. Default is as below.
```shell script
state.dir = /tmp/kafka-streams
```

State Store is created per application instance. Meaning if your topic has 3 partitions and you've 3 application instances and say you produced 300,000 transactions (assume these are equally spread across 3 partitions), then each state store will have only 100,000 transactions.

### Query State Store via REST

As we build REST API to query Kafka Streams State Store, you can fetch data from the Materialised View (stateful data)

You will get list of transactions info as per mock data.
- /api/statements/v1/authenticate
- /api/statements/v1/transactions
- /api/statements/v1/transactions/month/072022



### Secured REST APIs
All Resources are secured except `/authenticate` API

Username/ Password Authentication is in place and generates a JWT Token with User's UniqueID

Subsequent calls will be protected by JWT Token

# Testing & Mappings

### Mapping

| Username  | Password      | UniqueID      | ACCOUNT                    |
| :---      | :---          | :---          | :---                       |
| Anil      | bar           | P-0123456789  | CH93-0000-0000-0000-0000-1 |
| Kumar     | bar           | P-9876543210  | CH93-0000-0000-0000-0000-0 |
| Other     | bar           | P-0000000000  | NO ACCOUNT MAPPED          |
| Unknown   | fail          | NA            | NA                         |

### Testing

Anil's account has 18 transactions where 5 transaction for month 062022, 13 transactions for month 072022

#### 1. Get JWT Token (Token will have Anil's UniqueID)
```shell script
curl -X POST 'localhost:1025/api/statements/v1/authenticate' \
-H 'Content-Type: application/json' \
-H 'Content-Type: text/plain' \
--data-raw '{
	"username": "Anil",
	"password": "bar"
}'
```

#### 2. Get Monthly Transactions for month 072022 with Bearer Token with Anil's UniqueID (with which we can fetch Account #)
```shell script
curl -X GET 'localhost:1025/api/statements/v1/transactions/month/072022' \
-H 'Content-Type: application/json' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBbmlsIiwiZXhwIjoxNjU1NTcxMTMzLCJpYXQiOjE2NTU1NzEwNzMsImp0aSI6IlAtMDEyMzQ1Njc4OSJ9.rxd-LjvDk8GdhG5ys9cDU5w-UFrHU0fW3KndHYAiU5s'
```

#### 3. If we pass month 062022, API returns 5 transactions

#### 4. If we pass any month than 072022, 062022 should give nothing (empty array), e.g. 052022

#### 5. Get All Transactions with Bearer Token with Anil's UniqueID (with which we can fetch Account #)
```shell script
curl -X GET 'localhost:1025/api/statements/v1/transactions' \
-H 'Content-Type: application/json' \
-H 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmb28iLCJleHAiOjE2NTU1NjkwMjYsImlhdCI6MTY1NTU2ODk2NiwianRpIjoiUC0wMTIzNDU2Nzg5In0.EwiAn0kKOiKXXHns1gvsb9ATuEY3NzTyw3mjSRLAGN0'
```

#### 6. If JWT Expired or It's Integrity can't be verified will output 401 Unauthorised

# Deploying On Kubernetes

Push Image to Docker Hub
```shell script
docker tag kafka-statements-service-app:latest whyaneel/kafka-statements-service-app:1.0.0

docker push kafka-statements-service-app:latest
```

- [ ] Setup on Kubernetes


# Bonus
### Debug Spring Boot App  with Docker Instance in IntelliJ
```
IntelliJ IDE 

    > Run/ Debug Configurations 
    
    > Add "Remote" 
    
    > And Paste below JAVA_TOOL_OPTIONS under section "Command line arguments for remote JVM:"  
```
```
 -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -Xms256m -Xmx512m
```

Now click Debug Icon and  you'll be able to debug the code with the breakpoints



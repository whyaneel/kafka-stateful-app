#!/bin/sh

printStep() {
    # Colors.
    YELLOW='\033[1;33m'
    END='\033[0m'

    echo "\n==================================================================="
    echo "${YELLOW}STEP: ${END}$1"
    echo "==================================================================="
}


sh kafka-statements-service/compile.sh

cd local
docker-compose up -d

echo "\nWaiting for rest proxy to be online (i.e. kafka will also be online) ...\n"
response=0
while [ $response -ne 200 ]; do
    response=$(curl --write-out %{http_code} --silent --output /dev/null http://0.0.0.0:8082/topics/)
    sleep 1
done

echo "\nCreating Topics ...\n"
docker exec demo-kafka kafka-topics --zookeeper demo-zookeeper:2182 --create --topic ACCOUNT_TRANSACTIONS --partitions 1 --replication-factor 1 --if-not-exists
docker exec demo-kafka kafka-topics --zookeeper demo-zookeeper:2182 --create --topic ACCOUNT_TRANSACTIONS_TABLE --partitions 1 --replication-factor 1 --if-not-exists
docker exec demo-kafka kafka-topics --zookeeper demo-zookeeper:2182 --create --topic MONTHLY_ACCOUNT_TRANSACTIONS_TABLE --partitions 1 --replication-factor 1 --if-not-exists

echo "\nCreating Mock Data ...\n"
sh mock-data/ACCOUNT_TRANSACTIONS.sh

# Deployment completed beep ;)
echo -e "\007"

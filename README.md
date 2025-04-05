export FEATKEY=$(base64 -i ~/evaluation-features.conf)

docker run -d --name aerospike -p 3000-3002:3000-3002 aerospike/aerospike-server-enterprise
docker run -it aerospike/aerospike-tools aql -h 192.168.1.105 aerospike


SHOW NAMESPACES;

here for aerospike server and aerospike client i have used docker images for aerospike server and client using below command

docker server will downloaded and install image in docker
for docker server
1.docker run -d --name aerospike -p 3000-3002:3000-3002 aerospike/aerospike-server-enterprise

for docker client (here i have passed ip address to connect with aerospike client)
docker run -it aerospike/aerospike-tools aql -h 192.168.1.105 aerospike

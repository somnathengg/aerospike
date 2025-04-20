docker run -d --name aerospike -p 3000-3002:3000-3002 aerospike/aerospike-server-enterprise docker run -it aerospike/aerospike-tools aql -h 192.168.1.105 aerospike
here for aerospike server and aerospike client i have used docker images for aerospike server and client using below command

-------------------------------------------------------connect Server-------------------------------------------------------------------- docker server will downloaded and install image in docker for docker server 1.docker run -d --name aerospike -p 3000-3002:3000-3002 aerospike/aerospike-server-enterprise

---------------------------------------------------------connect AQL--------------------------------------------------------------------- for docker client (here i have passed ip address to connect with aerospike client) docker run -it aerospike/aerospike-tools aql -h 192.168.1.105 aerospike

to export data from aerospike use below command

docker run -it aerospike/aerospike-tools aql -h 192.168.1.100 aerospike -c "SELECT * FROM test.user_master LIMIT 100" > E:\aerospike-exported-data\aero-user-master.json

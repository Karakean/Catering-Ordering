# use this script only if you'd like to run gateway server, command, queries and email services uncontainerized
docker stop rabbitmq
docker stop write_database
docker stop read_database

docker rm rabbitmq
docker rm write_database
docker rm read_database

docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 -v C:/Users/mipig/Desktop/KSR-Project/Rabbitmq/rabbitmq.conf:/etc/rabbitmq/rabbitmq.conf rabbitmq:management
docker run -d --name write_database -p 5432:5432 -e POSTGRES_USER=username_write -e POSTGRES_PASSWORD=password_write -e POSTGRES_DB=write_db -d postgres
docker run -d --name read_database -p 5433:5432 -e POSTGRES_USER=username_read -e POSTGRES_PASSWORD=password_read -e POSTGRES_DB=read_db -d postgres
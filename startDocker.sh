echo Executing commands...
mvn clean install
docker build -t springio/gs-spring-boot-docker .
docker run -d --name spring-app -p 8080:8080 springio/gs-spring-boot-docker
echo --------The container is ready--------
read -p "Press enter to stop container and remove image"
echo Stopping docker container...
docker stop spring-app
docker rm spring-app
docker image rm --force springio/gs-spring-boot-docker
echo Done!

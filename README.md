# Song Translation API

Song Translation API is a Dockerized app to manage songs and their translations.
It exposes RESTful HTTP endpoints to do basic operations on songs and their translations.

Song Translation API can be deployed on multiple hosts.

## Deployment

Create a swarm manager
```
docker swarm init
```

Deploy the application
```
docker stack deploy song-translation-api -c docker-compose.yml
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [MySQL](https://www.mysql.com/) - Relational Database Management System
* [Docker](https://www.docker.com/)- Software Containerization Platform

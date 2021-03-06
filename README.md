# Song Translation API

Song Translation API is a microservice to manage songs and their translations.
It exposes RESTful HTTP endpoints to do basic operations on songs and their translations.

Song Translation API can be deployed on multiple hosts as a regular war or as a Dockerized app!


*Note: This README will be soon completed.*

## TODO:
- [ ] Finish API endpoints docs
- [ ] Improve deployment and installation docs

## Endpoints 

1) *Find a song by ID:* 
``` 
GET /songs/{songId}
```

Response:

|Success | Song Not Found | Errors |
| ------------- | ------------- | ------------- |
| Code: <br>``` HTTP 200 OK ```  <br><br>Payload:<br>``` {```<br><pre>``` "id":1,``` <br> ``` "title":"Midnight in Chelsea", ```<br> "artist":"Jon Bon Jovi", <br> "lyrics":"Sha la la la sha la la", <br> "language":"en"<br>```}``` | ``` HTTP 404 Not Found ``` | ``` HTTP 500 Internal Server Error ```| 

2) *Create a new song:*
```
POST /songs
```
3) *Update an existing song*
```
UPDATE /songs
```
4) *Remove a song by ID*
```
DELETE /songs/{songId}
```
5) *Get a collection of songs*
```
GET /songs
```
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

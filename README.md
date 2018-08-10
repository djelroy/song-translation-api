# Song Translation API

Song Translation API is a web service to manage songs and their translations.
It exposes RESTful HTTP endpoints to do basic operations on songs and their translations.

Song Translation API can be deployed on multiple hosts as a regular war or as a Dockerized app!


## Endpoints 

1) Find a song by ID
```
GET /songs/{songId}
```
* Success

```
HTTP 200 OK
```
* Payload
```
  {
    "id":1,
    "title":"Midnight in Chelsea",
    "artist":"Jon Bon Jovi",
    "lyrics":"Sha la la la sha la la",
    "language":"en"
   }   
```
* Failure
```
HTTP 404 Not Found
```
* Errors
```
HTTP 500 Internal Server Error
```

2) Create a new song
```
POST /songs
```
3) Update an existing song
```
UPDATE /songs
```
4) Remove a song by ID
```
DELETE /songs/{songId}
```
5) Get a collection of songs
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

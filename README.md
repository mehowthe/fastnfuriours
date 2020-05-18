# Backend Coding Challenge - The Fast and the Furious franchise
---
### Intro
This is a solution for a Backend Coding Challenge [details](https://gist.github.com/swistaczek/850979644d6df87231ee154958712a07)
A small cinema, which only plays movies from the Fast & Furious franchise, wish to support some functionalities as:
* providing information about movie times
* providing details about movies (that are fetched from [OMDB](http://www.omdbapi.com/))
* basic rating system
* allowing cinema owners to update show times and prices for their movie catalog

### Guides

#### How to run application
#### Prerequisites
* [OMDB](http://www.omdbapi.com/) api key - needs to be provided as a enviroment variable `OMDB_API_KEY`
(see: [env.sh](env.sh))
* Java 8 runtime

### Run fast locally with Gradle
`OMDB_API_KEY=${YOUR_KEY} ./gradlew bootRun`
    - application should start handling requests on port 8080 ([localhost:8080](http://localhost:8080/))

### Build and run in a Docker container
`docker build -t fastnfuriours:v1 .` - to build an image
`docker run -p 8080:8080 fastnfuriours:v1 -e OMDB_API_KEY=${YOUR_KEY}`

#### Exploring API

Along with application, there is also available Swagger UI - by default accessible on [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
API-docs in a json format is accessible on [/v2/api-docs](http://localhost:8080/v2/api-docs)

![Swagger](/docs/swagger.png)

For `/protected` path there is available default user with MANAGER role:
```
spring.security.user.name=manager1
spring.security.user.password=15ef839a-8dc4-409b-9015-bfa25fc6ccd6
```
As per MVP, `/protected` is using only [Basic](https://en.wikipedia.org/wiki/Basic_access_authentication) authentication method

#### Accesing database
For MVP and fast development appliction is also exposing H2 console on [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
You can access database using information below:
```
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=admin
spring.datasource.password=admin
```


#### Todos and notes
(Things that should happen to make it really production ready)

1. Setup a proper database - for demo purpose application is using in-memory H2 database
    * split application properties to *prod* and *dev* (can still use in memory H2)
2. User management - add groups of users that are allowed to make changes to their cinema movie repertoire
3. Add proper authentication - for demo purpose it's using Basic Authentication
4. Error handling and validation (there's only validation on adding new Rate (0-5 stars) right now)
5. This is not a fully RESTful API - it's only covering required functionality
#### Explanation of choices

Spring Boot + H2 in-memory database + Spring Security + Spring REST + Spring Data - for a fast development

---
Challenge was meant to be completed in a limited time frame, so there are some trade-offs
(like allowing authenticated users to update data on all cinemas).
The key point here is to show code style and best practices:
- a lot of tests on different levels - starting from simple unit tests to more advanced - for instance tests making a "real" HTTP requests.
- project structure, managing dependencies (DI), separation of concerns,

---
Author: <Michal Durkalec madurkalec@gmail.com>

# Movie WishList API

This will be the API that will provide the capabilities for managing
movie wishlists for people.

It will be able to:
1. Login and create accounts
2. Manage a list of movies associated with an account
3. Fetch and fill information about movies from an external API such as IMDB
4. Collect statistics regarding the popularity of each movie on the service.

## Integrating Postgres

### Step 1: Set up postgres on your machine
https://www.postgresql.org/download/

### Step 2: Create a database called testdb
In the postgres shell, run:
```sh
CREATE DATABASE testdb
```

### Step 3: Update application.properties
In `src/main/resources`, edit the application.properties file to have the following lines:

```sh
spring.datasource.username=<YOUR POSTGRES USER NAME HERE>
spring.datasource.url=<POSTGRES URL HERE>
```

Replacing with your username and the proper url. I believe postgres runs on port 5432 by default so the proper URL is usually
`jdbc:postgresql://localhost:5432/testdb`

## Setup for streaming endpoints

### Step 1: Get a watchmode API key
I (Sachin) can dm you mine on Slack or you can sign up for one here https://api.watchmode.com/. There is a monthly quota of 1000 queries

### Step 2: Connect API key to the API
In order for the API to make queries to WatchMode based off of your API key, you need to create a `config.txt` file in the root of the
project and paste your API key into this. It is set up like this so we never have to push API keys to the git repo

### Step 3: Launch the API
run
```sh
mvn clean spring-boot:run
```

in order to launch the app.

### Step 4: Test the streaming endpoints
There are two endpoints where you can test the connection to WatchMode is correct

1. /available
  - Hitting this endpoint queries a hardcoded movie (specifically Skyfall (2012)) and returns the names of all sources where this is available for free
  - I knew a priori that this was available on Amazon Prime so I used it as a sanity check. Sure enough, amazon prime is returned in the list of sources
2. /freeWithSub
  - this takes in a WatchMode id and returns all the streaming services where you can watch the movie for free (with subscription to the streaming service)
  - Notably what this does is filter out all the sources where the movie is available to buy or rent and only returns sources where it's free with subscription
  - Some example IDs and where they can be found for free with subscription:
    - 1586594 -> El Camino (netflix)
    - 1295258 -> Parasite (hulu)
    - 1616666 -> Host (shudder)
    - 1350564 -> Skyfall (amazon prime)
    - 130381 -> Annihilation (Paramount+)

## Removing Database Info from application.properties file
Create a 'secrets.properties' file in the src/main/resources folder. Copy paste the database url, username and password for the local instance in this file.


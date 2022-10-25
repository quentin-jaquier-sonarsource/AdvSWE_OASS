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

### Step 3: Adding environment variables
### For mac:
1. Open a terminal and open the .bash_profile file by running:
```sh
open -e ~/.bash_profile
```

2. Scroll down to the end of the file and add the the following:
```sh
export DB_USER_NAME=<YOUR POSTGRES USER NAME HERE>
export DB_USER_PASSWORD=<YOUR POSTGRES PASSWORD HERE>
export DATABASE_URL=<POSTGRES URL HERE>
```
3. Save the file by pressing command+s and then close the file
4. Execute the file either by opening a new terminal window or by running the command :
```sh
source ~/.bash_profile
```
### For windows:
1. In the control panel, search for environment variable in the search bar
2. Click on "Edit the system environment variables' and then on the Environment Variables button
3. In the system vairiables, add the following variables and their respective values:
```sh
DB_USER_NAME=<YOUR POSTGRES USER NAME HERE>
DB_USER_PASSWORD=<YOUR POSTGRES PASSWORD HERE>
DATABASE_URL=<POSTGRES URL HERE>
```
4. Click OK and apply the changes
5. Open a new terminal for the changes to apply


Replacing with your username, password and the proper url. I believe postgres runs on port 5432 by default so the proper URL is usually
`jdbc:postgresql://localhost:5432/testdb`

## Setup for streaming endpoints

### Step 1: Get a watchmode API key

I (Sachin) can dm you mine on Slack or you can sign up for one here https://api.watchmode.com/. There is a monthly quota of 1000 queries

### Step 2: Connect API key to the API

In order for the API to make queries to WatchMode based off of your API key, you need to add the 'apikey' as a system environment variable in the same way you added the postgres database url, username and password. It is set up like this so we never have to push API keys to the git repo

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

## Running Checkstyle

On the command line run

```sh
mvn site
```

Or, in Intellij, open up the Maven panel, click on `Lifecycle` and then double click `site`.

The generated reports will the appear under `target/site/project-reports.html`.

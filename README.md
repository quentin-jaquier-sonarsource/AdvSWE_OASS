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

### For VS Code

This is the simplest way to set environment variables on your local machine that work for both Windows and Mac
as long as you're using [Visual Studio Code](https://code.visualstudio.com)

Open the "Run and Debug" sidebar. (It's the Play Icon with a Bug on it.)

You should see a big "Run and Debug" button with the following text below:

> To customize Run and Debug **create a launch.json file**.

Click that "create a launch.json file" link. It'll create a new JSON file which will be used to run the Java project from within
VS Code.

The JSON file should look like this:

```json
{
  // Use IntelliSense to learn about possible attributes.
  // Hover to view descriptions of existing attributes.
  // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
  "version": "0.2.0",
  "configurations": [
    {
      "type": "java",
      "name": "Launch Current File",
      "request": "launch",
      "mainClass": "${file}",
      "env": {
        "DB_USER_NAME": "postgres",
        "DATABASE_URL": "jdbc:postgresql://localhost:5432/testdb",
        "API_KEY": "<your_api_key_here>"
      }
    },
    {
      "type": "java",
      "name": "Launch MovieWishlistApplication",
      "request": "launch",
      "mainClass": "coms.w4156.moviewishlist.MovieWishlistApplication",
      "projectName": "MovieWishlist",
      "env": {
        "DB_USER_NAME": "postgres",
        "DATABASE_URL": "jdbc:postgresql://localhost:5432/testdb",
        "API_KEY": "<your_api_key_here>"
      }
    }
  ]
}
```

If you have any problem creating this file, you can create the file manually at `./vscode/launch.json` at the root of the project folder.

Once this is done, you can use the "Run and Debug" section to choose between the two commands and click the Play button.
It will automatically use the environment variables you defined.

If this technique doesn't work, or if you prefer to run commands from the terminal, you can use the instructions below

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

In order for the API to make queries to WatchMode based off of your API key, you need to add the 'API_KEY' as a system environment variable in the same way you added the postgres database url, username and password. It is set up like this so we never have to push API keys to the git repo

### Step 3: Launch the API

run

```sh
mvn clean spring-boot:run
```

in order to launch the app.

## Removing Database Info from application.properties file

Create a 'secrets.properties' file in the src/main/resources folder. Copy paste the database url, username and password for the local instance in this file.

## Running Checkstyle

On the command line run

```sh
mvn site
```

Or, in Intellij, open up the Maven panel, click on `Lifecycle` and then double click `site`.

The generated reports will the appear under `target/site/project-reports.html`.

Here are the results of the latest checkstyle run:

![**SUCCESS**](/Documentation/checkstyle-result-screenshots/latest.png)

## Running Tests

Run

```sh
mvn test
```

Here are the results of the latest test run:

![**SUCCESS**: 14 Tests. 0 Failures. 0 Skipped](/Documentation/Test-Result-Screenshots/latest.png)

## Documentation

Documentation for many of our endpoints can be found in the Documentation folder

All the documentation for the GraphQL endpoint can be found in the GraphiQL IDE.

In order to access this IDE, you need to do a few things.

1. Spin up a local instance of our service
2. Authenticate by following the instructions in the Authentication portion of this README. Hold on to the token
3. Download the ModHeader extension for Google Chrome
4. Hit the /graphiql endpoint. At this point it should NOT work
5. Use the ModHeader plugin to add a `Request Header` with name = `Authorization` and value = `Bearer <JWT>`
   - Where JWT is your token that you saved from before. Note the space after "Bearer"

The header should look like this:

![ModHeader](/Documentation/documentation-screenshots/modHeader.png)

After you have completed all these steps, you should be able to access the GraphiQL IDE
through the /graphiql endpoint. The documentation for this endpoint is visible in the `Docs` pane which
can be opened by clicking on the button in the top right. See the screenshot below.


![Where the docs button is](/Documentation/documentation-screenshots/where-docs.png)

The documentation is interactive. You can click on the types to introspect

![The documentation pane](/Documentation/documentation-screenshots/docs.png)


## SonarCloud
The SonarCloud dashboard for this project is located [here](https://sonarcloud.io/summary/overall?id=omniyyahOS1_AdvSWE_OASS)

## JaCoCo
SonarCloud is very verbose, if all you are interested in is code coverage, then
JaCoCo should suffice. Run
```shell
mvn clean verify
```

and then open `target/site/jacoco/index.html` in a browser to view coverage
reports.

## Authentication
The authentication is handled using JSON Web Tokens; here is how it works:
- a client needs to hit the `/new-client/` endpoint with his email to be added to the database; he will receive his own JWT in the response
- on every subsequent request, the client has to add the `Authorization: Bearer <JWT>` header so that he can be authenticated. If he fails to do so or the JWT is not valid, he will receive a `403` error

## Client
To run the client, read the instructions in `client/README.md`

## Possible 3rd Part Applications

There are a variety of ways a client could utilize our service.

1. An app that uses ML to recommend users movies to watch based on their interests.
   - Such an app might allow users to make watchlists and then based on those 
watchlists it would recommend more movies in that vein for users to watch. 
   - In this use case, the profile ID would correspond to a user ID.
   - The app would use our service to group watchlists with users, take care of
CRUD operations on those watchlists, and get information on the movies in the
watchlists to display to the user (e.g. plot overview)
2. A film news site that serves many listicles
   - A Buzzfeed-like site for movies that groups movies based on some conceit
     (e.g. Scariest Movies of all Time, Best B-movies, Hidden Gems from the
90s, etc.)
   - In this use case, the profile ID would be used as an article ID or perhaps
a theme ID if the site had many listicles under one big theme (e.g. Best movies
per decade)
   - The site would use our service in order to logically group their lists of
movies, perform CRUD on these lists, and to display where these movies are
available to stream.
3. A movie director information website
   - A site dedicated to preserving information about film directors might find
our service useful
   - In this use case, the profile ID would be used as a director ID and each
director would have an associated list of movies. The site moderators would
maintain these lists of movies, using our service to perform CRUD on the lists
of movies and rate the movies.
   - Such a site might use our rating feature in order to determine the highest
rated directors of all time based on the average ratings of their movies.

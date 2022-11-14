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

Documentation for all the endpoints can be found in the `Documentation` folder


## SonarQube - Locally

In order to run SonarQube locally, you need to install SonarQube locally. 
Follow the instructions in the link below. There are two options, to install
from a zip or from a Docker image. On Windows, I found it easier to use the
Docker image as the zip file gave me some issues.

[SonarQube Instructions](https://docs.sonarqube.org/latest/setup/get-started-2-minutes/#:~:text=Install%20SonarQube%20documentation.-,Installing%20a%20local%20instance%20of%20SonarQube,-You%20can%20evaluate)

Once you have SonarQube running go to http://localhost:9000 and login with
admin credentials: login=admin and password=admin.

Follow [these](https://docs.sonarqube.org/latest/setup/get-started-2-minutes/#:~:text=password%3A%20admin-,Analyzing%20a%20Project,-Now%20that%20you%27re)
instructions on how to set up the project to be analyzed locally.

After you have set up the project, it should give you a command that you can
copy-paste in order to run analysis. My command looks like this:

```sh
mvn clean verify sonar:sonar -Dsonar.projectKey=demo -Dsonar.host.url=http://localhost:9000 -Dsonar.login=<GENERATED_KEY>
```

**On Windows, this has to be run in the command shell NOT powershell**

The pom.xml is set up so that this should just work and give coverage reports.
If the tests are running but coverage is somehow 0%, something is wrong with the
Jacoco configuration.

## JaCoCo
SonarQube is very verbose, if all you are interested in is code coverage, then
JaCoCo should suffice. Run 
```shell
mvn clean verify
```

and then open `target/site/jacoco/index.html` in a browser to view coverage
reports.
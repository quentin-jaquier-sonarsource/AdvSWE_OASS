# Movie WishList API

This will be the API that will provide the capabilities for managing
movie wishlists for people.

It will be able to:
1. Login and create accounts
2. Manage a list of movies associated with an account
3. Fetch and fill information about movies from an external API such as IMDB
4. Collect statistics regarding the popularity of each movie on the service.

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
Create a 'secrets.properties' file in the src/main/resource folder. Copy paste the database url, username and password for the local instance in this file.

## Working on this project

### Step 1: Git pull this project
Use Git to pull this project from Github.

Using the `gh` command line tool makes this process easier.

### Step 2: Create a new branch for your changes
Before making any new changes, ensure that you create a new branch
for your work using:

```sh
$ git checkout -b <branch-name>
```

Use a name for your branch, that would describe the work you are
about to do.

### Step 3: Make you code changes and commit them
```sh
git commit -am <commit message>
```

### Step 4: Push the changes up to Github on the same branch name
```shell
git push origin <branch_name>
```

### Step 5: Open a Pull Request
Immediately after you push your changes to Github, you
will see a button that will let you create a "Pull Request"
with your new changes.

Click this button to do so.

## Editor to use

The best editor to work on this project and any Java prject is
IntelliJ IDEA ULTIMATE.

While, this is usually an expensive application, you can get it
for free as a student.

Sign up for [Github Student Developer Pack](https://education.github.com/pack).
This mostly involves filling a form and adding your College Email ID to your
Github Account

Once this is done, you will be able to link your Github Account to your
Jetbrains Account and use that to get Jetbrains IDEs for free.

This is how you can get IntelliJ for free!

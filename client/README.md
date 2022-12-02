# Client Readme
## Overview
The client is itself an API. It has fixed endpoints that are designed to test
the endpoints of our service end-to-end.

It is programmed in Python using the FastAPI library and requires our service to
be up and running in order to work. It runs on localhost and requires an
instance of our service running locally in order to perform tests end to end.

## How to Run

### Requirements
- Make sure you have [Python 3](https://www.python.org/downloads/) installed.
I'm running 3.10 but any Python 3 should be fine

### Steps
1. On the command line, create a virtual environment for all your dependencies
    - From the root of the client folder run the command 
    ```python -m venv client-env```
    - you can name the virtual environment anything you want but `client-env` is
    ignored by git and is what I use going forward.
2. Start the virtual environment
    - On Unix/MacOS in bash: `source client-env/bin/activate`
    - On Unix/MaxOS in csh: `source client-env/bin/activate.csh`
    - On Windows cmd: `client-env\Scripts\activate.bat`
    - On Windows PowerShell: `client-env\Scripts\Activate.ps1`
    - You should see your environment name in the shell (can confirm on Windows)
3. Install the dependencies
    - On the command line run `pip install -r requirements.txt`
4. Run the app
    - On the command line run `uvicorn app.main:app --reload`
    - Make sure that you also have our service up and running locally
    - the `--reload` flag allows you to make changes to the app while it runs
    without having stop the app and restart it in order to see the changes
5. Open the app
    - go to http://localhost:8000/docs in order to see the Swagger for all the
    endpoints
6. Using the app
    - Under the `Client Endpoints` section in the swagger, hit the
    `/clients/signup` to sign up as a client of the service. This should return
    back a token but you don't need to do anything with it. It will be stored in
    a temporary file on localhost
    - Now that you're signed up and you have a token, all the other endpoints
    should work. If you try to access the other endpoints before signing up you should receive an error
7. Testing
    - In order to test, you must have an instance of our service up and running
    locally. To test, run `pytest` in the root directory. Make sure you have
    taken the steps to activate the virtual environment and install all the
    requirements first
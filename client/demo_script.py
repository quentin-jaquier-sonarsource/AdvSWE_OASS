import logging
import random
import string
import threading
import time

import requests
from requests import Response
import json

SERVICE_URL = "http://localhost:8080"
# SERVICE_URL = "https://movie-wishlist-ase.herokuapp.com/"
SIGNUP_URL = SERVICE_URL+"/new-client"
GRAPHQL_URL = SERVICE_URL + "/graphql"

# setup the logging stuff
format = "%(asctime)s: %(message)s"
logging.basicConfig(format=format, level=logging.INFO, datefmt="%H:%M:%S")

def get_rand_email() -> str:
    """
    Can't sign up with same email twice, so use this to generate randomly
    """
    suffix = "@test.com"

    main_part = ''.join(random.choices(string.ascii_lowercase, k=7))

    return main_part + suffix

def create_auth_header(token : str) -> dict:
    """
    Creates an auth header from a token

    Params
    -----------
    token : str
        the JWT returned by the /new-client endpoint

    Returns
    -----------
    auth_header : dict
        the authorization header
    """

    return {"Authorization": f"Bearer {token}"}


def sign_up(client_email : str):
    """
    Signs up the client using the email addr

    Returns
    --------
    auth_header : dict
        Authorization header for the client to use in future interactions
        with the service
    """

    try:

        r : Response = requests.post(url=SIGNUP_URL, params = {"email" : client_email})
        json_data = json.loads(r.text)
        token = json_data["token"]

        return create_auth_header(token)

    except Exception as e:
        return e.__str__()

def query_service(query : str, auth_header : dict, variables : dict = {}):
    r : Response = requests.post(GRAPHQL_URL, json={'query' : query, 'variables' : variables}, headers=auth_header)

    json_data = json.loads(r.text)["data"]

    return json_data

def create_profile(client_id, auth_header : dict, profile_name:str):
    mutation = """mutation ($var_id : ID!, $var_name : String!) {
        createProfile (clientID : $var_id, name : $var_name) {
            id
            name
        }
    }
    """

    variables = {"var_id" : client_id,"var_name" : profile_name}

    query_service(query=mutation, variables=variables, auth_header=auth_header)

def create_profiles(client_id, auth_header, profile_names : list):
    for profile_name in profile_names:
        create_profile(client_id, auth_header=auth_header, profile_name=profile_name)

def fetch_id(auth_header):
    query = """
    query {
        client {
            id
        }
    }
    """

    data = query_service(query=query, auth_header=auth_header)

    print(f"THE DATA : {data}")

    return data["client"]["id"]



def get_profiles(auth_header):
    query = """query {
        profiles {
            id
            name
        }
    }
    """

    data  = query_service(auth_header=auth_header, query=query)

    return data

def use_service(thread_id, client_email : str, profile_names : list):
    logging.info("Thread %s: starting", thread_id)

    # main functionality
    # sign up with email
    logging.info("Thread %s: signing up with email %s", thread_id, client_email)
    auth_header = sign_up(client_email=client_email)

    logging.info("Thread %s: fetching its id", thread_id)
    client_id = fetch_id(auth_header)

    print(f"Using client ID: {client_id}")

    # create some profiles
    logging.info("Thread %s: creating profiles", thread_id)
    create_profiles(client_id = client_id, auth_header=auth_header, profile_names=profile_names)

    # query the profiles
    logging.info("Thread %s: querying profiles", thread_id)
    profile_data = get_profiles(auth_header=auth_header)

    logging.info("Thread %s: returned profiles: %s", thread_id, profile_data)

    logging.info("Thread %s: finishing", thread_id)

def mult_clients():
    """
    Creates 3 threads with three different clients performing similar but
    distinct queries
    """
    logging.info("Main    : before creating threads")

    t1 = threading.Thread(target=use_service, args=(1, get_rand_email(), ["Group A", "Group B"]))
    t1.start()

    t2 = threading.Thread(target=use_service, args=(2, get_rand_email(), ["Scary Movies", "Feel Good Movies"]))
    t2.start()

    t3 = threading.Thread(target=use_service, args=(3, get_rand_email(), ["Jordan Peele", "Robert Eggers"]))
    t3.start()

    # logging.info("Main    : wait for the threads to finish")

    t1.join()
    t2.join()
    t3.join()

    logging.info("Main    : all done")

def unauthenticated_client():
    """
    Simulate a random client trying to use bad creds
    """

    query = """query {
        profiles {
            id
            name
        }
    }
    """

    logging.info("Unauthenticated   : trying %s with no credentials", query)

    auth_header = {}

    r : Response = requests.post(GRAPHQL_URL, json={'query' : query}, headers=auth_header)

    logging.info("Unauthenticated   : result of query for profiles with no creds was %s", r.text)

    data = json.loads(r.text)["data"]
    profiles = data["profiles"]

    if len(profiles) == 0:
        logging.info("Unauthenticated   : [PASS] fetched no profiles")
    else:
        logging.info("Unauthenticated   : [FAIL] fetched profiles")


    logging.info("Unauthenticated   : trying %s with arbitrary bad token 'JWT'", query)

    # Valid format but not actually valid JWT. Can be parsed but shouldn't work
    auth_header = create_auth_header("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNjcwMjYwMjQ4fQ.0VxKpbhWPqQFZLT4Mnb7VKZhAQQHLLy4lOyZIdZ8gRbFGWPKjb2bkm8z0")

    r : Response = requests.post(GRAPHQL_URL, json={'query' : query}, headers=auth_header)

    logging.info("Unauthenticated   : result of query for profiles with bad creds was %s", r.text)




def driver():
    print("MULTI CLIENT DEMO")

    mult_clients()

    print("END MULTI CLIENT DEMO")

    input("Press enter to demo unauthenticated clients using fake credentials\n\n\n\n\n")

    print("UNAUTHENTICATED CLIENTS DEMO")

    unauthenticated_client()

    print("END UNAUTHENTICATED CLIENTS DEMO")



if __name__ == "__main__":
    driver()

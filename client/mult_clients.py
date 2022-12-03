import logging
import random
import string
import threading
import time

import requests
from requests import Response
import json

SERVICE_URL = "http://localhost:8080"
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

        return {"Authorization": f"Bearer {token}"}

    except Exception as e:
        return e.__str__()

def query_service(query : str, auth_header : dict, variables : dict = {}):
    r : Response = requests.post(GRAPHQL_URL, json={'query' : query, 'variables' : variables}, headers=auth_header)

    json_data = json.loads(r.text)["data"]

    return json_data

def create_profile(auth_header : dict, profile_name:str):
    mutation = """mutation ($var_name : String!) {
        createProfile (name : $var_name) {
            id
            name
        }
    }
    """

    variables = {"var_name" : profile_name}

    query_service(query=mutation, variables=variables, auth_header=auth_header)

def create_profiles(auth_header, profile_names : list):
    for profile_name in profile_names:
        create_profile(auth_header=auth_header, profile_name=profile_name)

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

    # create some profiles
    logging.info("Thread %s: creating profiles", thread_id)
    create_profiles(auth_header=auth_header, profile_names=profile_names)
    
    # query the profiles
    logging.info("Thread %s: querying profiles", thread_id)
    profile_data = get_profiles(auth_header=auth_header)

    logging.info("Thread %s: returned profiles: %s", thread_id, profile_data)
    
    logging.info("Thread %s: finishing", thread_id)

def driver():
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


if __name__ == "__main__":
    driver()
    # print(get_rand_email())
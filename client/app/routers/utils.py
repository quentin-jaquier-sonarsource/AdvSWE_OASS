import json

import requests
from requests import Response

from app.constants import GRAPHQL_URL
from app.routers.router_constants import TOKEN_PATH

def get_auth_header() -> dict:
    """
    Fetches the token from the temp file and creates an auth header from it

    Returns
    -------
    On success:
        An auth header with the token
    On failure:
        A dict with key "Error" mapping to an error message
    """
    token_file = open(TOKEN_PATH, 'r')
    token = token_file.read()
    token_file.close() 

    if token == "":
        return {"Error" : "Token was blank"}
    
    auth_header = {"Authorization": f"Bearer {token}"}

    return auth_header



def query_graphql_service(query : str, variables : dict = {}) -> dict:
    """
    Queries our graphql service using the given GraphQL query and variables

    Params
    ------------
    query : str
        A GraphQL query or mutation
    
    variables : dict
        A dictionary mapping variable names to values. If `query` uses no
        variables this should be {}

    Returns
    ------
    json_data : dict
        the JSON data of the returned query, if there was an error, returns the
        error as a JSON
    """

    try:
        auth_header = get_auth_header()

        r : Response = requests.post(GRAPHQL_URL, json={'query' : query, 'variables' : variables}, headers=auth_header)

        json_data = json.loads(r.text)["data"]
    
        return json_data
    
    except Exception as e:

        return {"Exception" : e.__str__()}
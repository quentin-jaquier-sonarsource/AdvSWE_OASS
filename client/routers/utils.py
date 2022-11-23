import json
import requests

from requests import Response
from constants import GRAPHQL_URL

def query_graphql_service(query : str, variables : dict = {}) -> dict:
    """
    Queries our graphql service using the given GraphQL query and variables

    Params
    ------------
    query : str
        A GraphQL query
    
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

        r : Response = requests.post(GRAPHQL_URL, json={'query' : query, 'variables' : variables})

        json_data = json.loads(r.text)["data"]
    
        return json_data
    
    except Exception as e:

        return {"Exception" : e.__str__()}
import json
from fastapi import APIRouter
from requests import Response
from constants import GRAPHQL_URL, CLIENT_TAG

import requests

router = APIRouter(
    prefix="/clients",
    tags=[CLIENT_TAG]
)

@router.get("/all")
async def get_all_clients():
    """
    Returns all clients excluding user data
    """

    query = """query {
        clients {
            id
            email
        }
    }
    """

    r : Response = requests.post(GRAPHQL_URL, json={'query' : query})

    json_data = json.loads(r.text)["data"]
    
    return {"Result" : json_data}

@router.get("/all-verbose")
async def get_all_clients_verbose():
    """
    Returns all clients including user data
    """
    query = """query {
        clients {
            id
            email
            users {
                email
                name
            }
        }
    }
    """

    r : Response = requests.post(GRAPHQL_URL, json={'query' : query})

    json_data = json.loads(r.text)["data"]
    
    return {"Result" : json_data}


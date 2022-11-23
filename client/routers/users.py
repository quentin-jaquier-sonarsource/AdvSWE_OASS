import json
from fastapi import APIRouter
from requests import Response
from constants import GRAPHQL_URL, USER_TAG

import requests

router = APIRouter(
    prefix="/users",
    tags=[USER_TAG]
)

@router.get("/all")
async def get_all_users():
    """
    Returns all users
    """

    query = """query {
        users {
            name
            email
        }
    }
    """

    r : Response = requests.post(GRAPHQL_URL, json={'query' : query})

    json_data = json.loads(r.text)["data"]
    
    return {"Result" : json_data}
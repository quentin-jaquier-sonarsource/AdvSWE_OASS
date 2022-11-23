import json
from fastapi import APIRouter
from requests import Response
from constants import GRAPHQL_URL, TEST_URL, TEST_TAG

import requests

from routers.utils import query_graphql_service

router = APIRouter(
    prefix="/tests",
    tags=[TEST_TAG]
)


@router.get("/")
async def test_connection_to_service():
    """
    This endpoint simply hits the /test endpoint of our service and returns the
    resulting message.
    """
    try:
        # Try to hit the test endpoint and return the results 
        signup_resp: Response = requests.get(url=TEST_URL)
        return {"Response from service": signup_resp.text}

    except Exception as e:

        # If there was an issue hitting the test endpoint, show it to the user
        return {"Exception: ": e.__str__()}

@router.get("/graphql")
async def exercise_arbitrary_graphql_endpoint():
    """
    This endpoint hits a graphql endpoint. Arbitrarily, it queries for movies,
    but the main point of this endpoint is just to hit our graphql endpoint
    and make sure that everything is up and running
    """

    query = """query{
        movies {
            id
        }
    }
    """

    json_data = query_graphql_service(query=query)
    return {
        "Json Data" : json_data
    }

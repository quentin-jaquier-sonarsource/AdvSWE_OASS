from fastapi import APIRouter
from requests import Response
from constants import GRAPHQL_URL, SOURCE_TAG
from .router_constants import HOST_ID

import requests
import json

router = APIRouter(
    prefix="/sources/sub",
    tags=[SOURCE_TAG]
)

@router.get("/")
async def test(id : int = HOST_ID):
    """
    Return the name of the movie associated with the WatchMode id along with
    the name of all the streaming services on which it is available for free
    with subscription

    - **id**: the WatchMode id of the movie in question

    Example response:
    ```json
    {
        "Movie title": "Annihilation",
        "Sources that are free with subscription": [
            "Paramount+",
            "Hoopla"
        ]
    }    
    ```
    """

    query = """query($var : ID!) {
        titleDetail (id : $var ) {
            title
            sources{
                name
                type
            }
        }
    }
    """
    variables = {"var" : str(id)}

    r : Response = requests.post(GRAPHQL_URL, json={'query' : query, 'variables': variables})

    json_data = json.loads(r.text)["data"]
    titleDetail = json_data["titleDetail"]
    title = titleDetail["title"]

    sources = titleDetail["sources"]

    # perform filtering on the sources
    free_source_names = []
    for source in sources:
        if source["type"] == "sub":
            free_source_names.append(source["name"])
    
    return {
        "Movie title" : title,
        "Sources that are free with subscription": free_source_names
        }
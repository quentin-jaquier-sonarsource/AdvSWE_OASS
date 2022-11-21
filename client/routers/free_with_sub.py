from fastapi import APIRouter
from requests import Response
from constants import GRAPHQL_URL, SOURCE_TAG

import requests
import json

router = APIRouter(
    prefix="/sources",
    tags=[SOURCE_TAG]
)

@router.get("/")
async def test():
    query = """query{
        titleDetail (id : 1616666 ) {
            title
            sources{
                name
                type
            }
        }
    }
    """

    r : Response = requests.post(GRAPHQL_URL, json={'query' : query})

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
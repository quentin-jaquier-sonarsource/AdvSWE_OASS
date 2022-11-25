from fastapi import APIRouter

from app.constants import SOURCE_TAG
from app.routers.utils import query_graphql_service

from .router_constants import (BUY, BUY_DESCRIPTION, DEFAULT_ID, RENT,
                               RENT_DESCRIPTION, SUB, SUB_DESCRIPTION)

router = APIRouter(
    prefix="/sources",
    tags=[SOURCE_TAG]
)

def get_sources(id: int, source_type : str = SUB, description : str = SUB_DESCRIPTION):
    """
    Gets all the sources for the movie by the WatchMode ID. Filters to only the
    sources of a certain type

    Params
    -------
    id: the WatchMode id of the movie
    source_type: the type of the movie source. Either buy, rent, or sub
    description: a description of the source type, to be used in the returned JSON
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

    json_data = query_graphql_service(query, variables)
    
    titleDetail = json_data["titleDetail"]
    title = titleDetail["title"]

    sources = titleDetail["sources"]

    # perform filtering on the sources
    free_source_names = []
    for source in sources:
        if source["type"] == source_type:
            free_source_names.append(source["name"])

    # Only return unique source names
    # Need to filter because we are not including source format which
    # is sometimes used to distinguish
    free_source_names = list(set(free_source_names))
    
    return {
        "Movie title" : title,
        description : free_source_names
        }

@router.get("/all")
async def get_all_source_info(id : int = DEFAULT_ID):
    """
    Return the name of the movie associated with the WatchMode id along with all
    the information on all the streaming services on which it is available
    with subscription

    - **id**: the WatchMode id of the movie in question

    Example Response:

    ```json
    {
        "Movie title": "Host",
        "All Sources Info": [
            {
                "name": "iTunes",
                "type": "rent",
                "format": "HD",
                "price": "4.99"
            },
            {
                "name": "Shudder",
                "type": "sub",
                "format": "HD",
                "price": null
            },
            {
                "name": "Amazon",
                "type": "buy",
                "format": "SD",
                "price": "9.99"
            },
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
                format
                price
            }
        }
    }
    """
    variables = {"var" : str(id)}

    json_data = query_graphql_service(query, variables)
    
    titleDetail = json_data["titleDetail"]
    title = titleDetail["title"]

    sources = titleDetail["sources"]

    
    return {
        "Movie title" : title,
        "All Sources Info" : sources
        }

@router.get("/sub")
async def get_sources_that_are_free_with_subscription(id : int = DEFAULT_ID):
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

    return get_sources(id, SUB, SUB_DESCRIPTION)

@router.get("/rent")
async def get_rent_sources(id : int = DEFAULT_ID):
    """
    Return the name of the movie associated with the WatchMode id along with
    the name of all the streaming services on which it is available to rent
    - **id**: the WatchMode id of the movie in question

    Example response:
    ```json
    {
        "Movie title": "Annihilation",
        "Sources where you can rent the given movie": [
            "VUDU",
            "Microsoft Store",
            "Amazon",
            "Google Play",
            "YouTube",
            "iTunes"
        ]
    }    
    ```
    """

    return get_sources(id, RENT, RENT_DESCRIPTION)

@router.get("/buy")
async def get_buy_sources(id : int = DEFAULT_ID):
    """
    Return the name of the movie associated with the WatchMode id along with
    the name of all the streaming services on which it is available to rent

    - **id**: the WatchMode id of the movie in question

    Example response:
    ```json
    {
        "Movie title": "Annihilation",
        "Sources where you can buy the given movie": [
            "VUDU",
            "Microsoft Store",
            "Amazon",
            "Google Play",
            "YouTube",
            "iTunes"
        ]
    }    
    ```
    """

    return get_sources(id, BUY, BUY_DESCRIPTION)
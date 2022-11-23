from fastapi import APIRouter

from constants import SEARCH_TAG
from routers.router_constants import DEFAULT_TITLE
from routers.utils import query_graphql_service

router = APIRouter(
    prefix="/search",
    tags=[SEARCH_TAG],
)

@router.get("/limited")
async def search_movie_by_title(title : str = DEFAULT_TITLE):
    query = """query ($title_var : String!) {
        searchTitles(title : $title_var) {
            id
            name
            details {
                year
                runtimeMinutes
            }
        }
    }
    """

    variables = {"title_var" : title}

    json_data = query_graphql_service(query, variables)

    return {"Search Results" : json_data}

@router.get("/verbose")
async def search_movie_by_title_verbose(title : str = DEFAULT_TITLE):
    query = """query ($title_var : String!) {
        searchTitles(title : $title_var) {
            id
            name
            details {
                year
                runtimeMinutes
                genreNames
                releaseDate
                year
                plotOverview
            }
        }
    }
    """

    variables = {"title_var" : title}

    json_data = query_graphql_service(query, variables)

    return {"Search Results" : json_data}
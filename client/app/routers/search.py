from fastapi import APIRouter

from app.constants import SEARCH_TAG
from app.routers.router_constants import DEFAULT_TITLE
from app.routers.utils import query_graphql_service

router = APIRouter(
    prefix="/search",
    tags=[SEARCH_TAG],
)

@router.get("/limited")
async def search_movie_by_title(title: str = DEFAULT_TITLE):
    """
    Searches for a movie by the title and returns results

    Example response:
    ```json
    {
        "Search Results": {
            "searchTitles": [
                {
                    "id": "168482",
                    "name": "Casino Royale",
                    "details": {
                        "year": 2006,
                        "runtimeMinutes": 144
                    }
                },
            ]
        }
    }
    ```
    """
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

    variables = {"title_var": title}

    json_data = query_graphql_service(query, variables)

    return {"Search Results": json_data}


@router.get("/verbose")
async def search_movie_by_title_verbose(title: str = DEFAULT_TITLE):
    """
    Searches for a movie by title and returns a verbose result

    Example response:

    ```json
    {
        "Search Results": {
            "searchTitles": [
                {
                    "id": "168482",
                    "name": "Casino Royale",
                    "details": {
                        "year": 2006,
                        "runtimeMinutes": 144,
                        "genreNames": [
                            "Action",
                            "Adventure",
                            "Thriller"
                        ],
                        "releaseDate": "2006-11-14",
                        "plotOverview": "Le Chiffre, a banker to the world's terrorists, is scheduled to participate in a high-stakes poker game in Montenegro, where he intends to use his winnings to establish his financial grip on the terrorist market. M sends Bond—on his maiden mission as a 00 Agent—to attend this game and prevent Le Chiffre from winning. With the help of Vesper Lynd and Felix Leiter, Bond enters the most important poker game in his already dangerous career."
                    }
                },
            ]
        }
    }
    ```
    """
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

    variables = {"title_var": title}

    json_data = query_graphql_service(query, variables)

    return {"Search Results": json_data}

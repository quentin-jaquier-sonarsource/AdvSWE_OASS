from fastapi import APIRouter

from app.constants import MOVIES_TAG
from app.routers.utils import query_graphql_service

router = APIRouter(
    prefix="/movies",
    tags=[MOVIES_TAG]
)

@router.get("/all")
async def get_all_movies():
    """
    Returns all movies
    """

    query = """query {
        movies {
            id
            details {
                title
            }
        }
    }
    """

    json_data = query_graphql_service(query)
    
    return {"Result" : json_data}
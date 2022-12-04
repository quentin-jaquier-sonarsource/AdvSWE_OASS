from fastapi import APIRouter

from app.constants import CLIENT_TAG
from app.routers.utils import query_graphql_service

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

    json_data = query_graphql_service(query)
    
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
            profiles {
                id
                name
                wishlists {
                    name
                    movies {
                        details {
                            title
                        }
                    }
                }
            }
        }
    }
    """

    json_data = query_graphql_service(query)
    
    return {"Result" : json_data}


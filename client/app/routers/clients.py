from fastapi import APIRouter

from app.constants import CLIENT_TAG
from app.routers.utils import query_graphql_service

router = APIRouter(
    prefix="/clients",
    tags=[CLIENT_TAG]
)

@router.get("/info")
async def get_client_info():
    """
    Returns client info
    """

    query = """query {
        client {
            id
            email
        }
    }
    """

    json_data = query_graphql_service(query)
    
    return {"Result" : json_data}
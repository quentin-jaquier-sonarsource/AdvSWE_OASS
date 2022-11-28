from fastapi import APIRouter

from app.constants import PROFILE_TAG

from .utils import query_graphql_service

# TODO refactor into Profiles

router = APIRouter(
    prefix="/profiles",
    tags=[PROFILE_TAG]
)

@router.get("/all")
async def get_all_profiles():
    """
    Returns all users
    """

    query = """query {
        profiles {
            name
            id
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
    """

    json_data = query_graphql_service(query)
    
    return {"Result" : json_data}


@router.get("/by-id")
async def get_profile_by_id(id : int):
    """

    TODO: implement this when it works on the service
    """

    return "TODO need this working on the service side first"
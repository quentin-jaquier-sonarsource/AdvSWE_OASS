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
    Returns all profiles
    """

    query = """query {
        profiles {
            name
            id
            wishlists {
                id
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


# @router.get("/by-id")
# async def get_profile_by_id(profile_id : int):
#     """

#     TODO: implement this when it works on the service
#     """

#     return "TODO need this working on the service side first"

@router.get("/create")
async def create_profile(profile_name : str = "Group A"):
    """
    Creates a profile with the given name, associated with the currently 
    authenticated and authorized client.
    """

    mutation = """mutation ($var_name : String!) {
        createProfile (name : $var_name) {
            id
            name
        }
    }
    """

    variables = {"var_name" : profile_name}

    json_data = query_graphql_service(mutation, variables)

    return {"Result" : json_data}


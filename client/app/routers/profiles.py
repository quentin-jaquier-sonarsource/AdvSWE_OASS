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
async def create_profile(client_id : int, profile_name : str = "Group A"):
    """
    Creates a profile with the given name, associated with the client of the
    given id
    """

    mutation = """mutation ($var_id : ID!, $var_name : String!) {
        createProfile (clientID : $var_id, name : $var_name) {
            id
            name
        }
    }
    """

    variables = {"var_id" : client_id,"var_name" : profile_name}

    json_data = query_graphql_service(mutation, variables)

    return {"Result" : json_data}


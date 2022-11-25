from fastapi import APIRouter

from app.constants import USER_TAG

from .utils import query_graphql_service

router = APIRouter(
    prefix="/users",
    tags=[USER_TAG]
)

@router.get("/all")
async def get_all_users():
    """
    Returns all users
    """

    query = """query {
        users {
            name
            email
        }
    }
    """

    json_data = query_graphql_service(query)
    
    return {"Result" : json_data}


@router.get("/by-email")
async def get_user_by_email(email : str = "test@test.com"):
    """
    Returns a user's information based on their email

    **email** the email of the user whose information we want
    """

    query = """query ($email_var : String!) {
        userByEmail(email : $email_var) {
            name
        }
    }
    """

    variables = {"email_var" : email}

    json_data = query_graphql_service(query, variables)
    
    return {"Result" : json_data}
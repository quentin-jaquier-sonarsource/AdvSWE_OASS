"""
This router is responsible for endpoints related to wishlists
"""

from fastapi import APIRouter

from app.constants import WISHLIST_TAG
from app.routers.utils import query_graphql_service

router = APIRouter(
    prefix="/wishlists",
    tags=[WISHLIST_TAG]
)

@router.get("/create")
async def create_wishlist(profileID : int, wishlistName : str = "MyWatchlist"):
    """
    Creates a wishlist with the associated profileID and the given name

    **profileID**: the ID of the profile for whom we want to add the wishlist
    **wishlistName**: the name to give the wishlist
    """

    mutation = """mutation ($var_id : ID!, $var_name : String!) {
        createWishlist(profileID : $var_id, wishlistName : $var_name) {
            id
            name
            movies {
                details {
                    title
                }
            }
        }
    }
    """

    variables = {"var_id" : profileID, "var_name" : wishlistName}

    json_data = query_graphql_service(query=mutation, variables=variables)

    return {"Result" : json_data}


@router.get("/update")
async def update_wishlist(wishlistID : int, newName : str = "MyWatchlist2.0"):
    """
    Updates the wishlist given by the wishlist ID to have the new name

    **wishlistID** the id of the wishlist we want to update
    **newName** the new name for the wishlist
    """

    mutation = """mutation ($var_id : ID!, $var_name : String) {
        updateWishlist(id : $var_id, name  : $var_name) {
            id
            name
            movies {
                details {
                    title
                }
            }
        }
    }
    """

    variables = {"var_id" : wishlistID, "var_name" : newName}

    json_data = query_graphql_service(query=mutation, variables=variables)

    return {"Result" : json_data}

@router.get("/delete")
async def delete_wishlist(wishlistID : int):
    """
    Deletes the given wishlist

    **wishlistID** the id of the wishlist we want to delete
    """

    mutation = """mutation ($var_id : ID!) {
        deleteWishlist (id : $var_id) {
            name
        }
    }
    """

    variables = {"var_id" : wishlistID}

    json_data = query_graphql_service(query=mutation, variables=variables)

    return {"Result" : json_data}
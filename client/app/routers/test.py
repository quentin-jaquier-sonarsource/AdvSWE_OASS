import requests
from fastapi import APIRouter
from requests import Response

from app.constants import TEST_TAG, TEST_URL
from app.routers.utils import get_auth_header, query_graphql_service

router = APIRouter(
    prefix="/tests",
    tags=[TEST_TAG]
)


@router.get("/")
async def test_connection_to_service():
    """
    This endpoint simply hits the /test endpoint of our service and returns the
    resulting message.

    Response if we're properly connected:
    ```json
    {
        "Status Code": 200,
        "Response from service": "All good!"
    }
    ```

    Response if we're not authenticate properly:
    ```json
    {
        "Status Code": 403,
        "Response from service": ""
    }
    ```
    """
    try:
        auth_header = get_auth_header()
        # Try to hit the test endpoint and return the results 
        test_resp: Response = requests.get(url=TEST_URL, headers = auth_header)
        return {
                "Status Code" : test_resp.status_code,
                "Response from service": test_resp.text
            }

    except Exception as e:

        # If there was an issue hitting the test endpoint, show it to the user
        return {"Exception: ": e.__str__()}

@router.get("/graphql")
async def exercise_arbitrary_graphql_endpoint():
    """
    This endpoint hits a graphql endpoint. Arbitrarily, it queries for movies,
    but the main point of this endpoint is just to hit our graphql endpoint
    and make sure that everything is up and running
    """

    query = """query{
        movies {
            id
        }
    }
    """

    json_data = query_graphql_service(query=query)
    return {
        "Json Data" : json_data
    }

from fastapi import APIRouter
from requests import Response
from constants import TEST_URL, TEST_TAG

import requests

router = APIRouter(
    prefix="/tests",
    tags=[TEST_TAG]
)


@router.get("/")
async def test():
    try:
        # Try to hit the test endpoint and return the results 
        signup_resp: Response = requests.get(url=TEST_URL)
        return {"Response from service": signup_resp.text}

    except Exception as e:

        # If there was an issue hitting the test endpoint, show it to the user
        return {"Exception: ": e.__str__()}

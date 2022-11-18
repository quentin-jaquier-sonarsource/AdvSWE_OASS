from fastapi import APIRouter
from requests import Response
from constants import TEST_URL

import requests

router = APIRouter(
    prefix="/tests",
    tags=["Test Endpoints"]
)

@router.get("/")
async def test():
    signup_resp : Response = requests.get(url=TEST_URL)

    return {"Response" : signup_resp.text}
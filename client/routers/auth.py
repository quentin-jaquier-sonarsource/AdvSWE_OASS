import json
import requests
from fastapi import APIRouter
from requests import Response

from constants import CLIENT_TAG
from routers.router_constants import SIGNUP_URL

router = APIRouter(
    prefix="/clients",
    tags=[CLIENT_TAG]
)

@router.get("/signup")
async def sign_up():
    """
    Signs up for the service
    """

    try:
    
        r : Response = requests.post(url=SIGNUP_URL, params = {"email" : "test@test2.com"})
        json_data = json.loads(r.text)
        
        return json_data
    
    except Exception as e:
        return {"Exception ocurred" : e.__str__()}

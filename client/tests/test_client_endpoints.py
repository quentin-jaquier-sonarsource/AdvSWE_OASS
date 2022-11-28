from tests.test_constants import CLIENT_EMAIL
from tests.utils import setup_end_to_end

from app.main import app
from fastapi.testclient import TestClient


client = TestClient(app)

def test_get_clients():
    """
    TODO may have to be refactored after authorization is added
    """
    setup_end_to_end()

    token = client.get("/clients/signup/", params={"email": CLIENT_EMAIL})

    response = client.get("/clients/all/")

    json_response = response.json()

    print(json_response)

    clients = json_response["Result"]["clients"]


    assert response.status_code == 200
    assert len(clients) == 1
    assert clients[0]["email"] == CLIENT_EMAIL
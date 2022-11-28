import pytest
from fastapi.testclient import TestClient

from app.main import app
from tests.test_constants import CLIENT_EMAIL
from tests.utils import setup_end_to_end

client = TestClient(app)

@pytest.fixture(autouse=True)
def setUp():
    """
    Sets DB to a known state and signs up a client

    Apparently it is frowned up in pytest to have a setup/teardown method like
    this, but I don't think it's good practice to keep calling the same function
    before every test function
    """

    # Run before tests
    setup_end_to_end()
    token = client.get("/clients/signup/", params={"email": CLIENT_EMAIL})

    # Run the tests
    yield

    # Run after tests

def test_get_clients():

    response = client.get("/clients/all/")

    json_response = response.json()

    print(json_response)

    clients = json_response["Result"]["clients"]


    assert response.status_code == 200
    assert len(clients) == 1
    assert clients[0]["email"] == CLIENT_EMAIL
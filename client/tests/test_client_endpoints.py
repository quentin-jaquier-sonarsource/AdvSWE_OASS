import pytest
from fastapi.testclient import TestClient

from app.main import app
from tests.test_constants import CLIENT_EMAIL, PROFILE_NAME
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


def test_get_clients_verbose1():
    """
    Tests /clients/all-verbose

    Barebones test, just for first layer of the structure
    """

    response = client.get("/clients/all-verbose")

    json_response = response.json()

    clients = json_response["Result"]["clients"]
    client0 = clients[0]

    # Assert stuff about the first level of the response
    assert response.status_code == 200
    assert len(clients) == 1
    assert client0["email"] == CLIENT_EMAIL
    assert "profiles" in client0
    assert "id" in client0

def test_get_clients_verbose2():
    """
    Tests /clients/all-verbose
    Also tests /profiles/create

    Tests deeper in the response structure, looking at the profiles of the
    client. Does not go as deep as wishlists
    """
    response = client.get("/clients/all-verbose")

    json_response = response.json()
    id = int(json_response["Result"]["clients"][0]["id"])

    print(f"The fetched id: {id}")

    # Create a profile for the client
    client.get("/profiles/create", params={"client_id" : id, "profile_name" : PROFILE_NAME})

    # now fetch the updated client again
    response = client.get("/clients/all-verbose")
    json_response = response.json()
    profile = json_response["Result"]["clients"][0]["profiles"][0]

    assert "id" in profile
    assert profile["name"] == PROFILE_NAME
    assert "wishlists" in profile
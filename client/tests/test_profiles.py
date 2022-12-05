"""
This file tests the /profile endpoints
"""

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

def test_all():
    """
    Tests /profiles/all
    """

    response = client.get("/profiles/all")
    json_response = response.json()
    result = json_response["Result"]
    profiles = result["profiles"]

    assert len(profiles) == 0


def test_profile_create():
    """
    Tests /profiles/create
    """
    # get client id in order to create the profiles
    response = client.get("/clients/info")
    client_id = response.json()["Result"]["client"]["id"]

    # Create a profile for the client
    params = {
            "profile_name" : PROFILE_NAME,
            "client_id" : client_id,
        }
    creation_response = client.get("/profiles/create", params=params)
    creation_json = creation_response.json()
    result = creation_json["Result"]

    # Assert structure of creation response
    assert "createProfile" in result
    assert "id" in result["createProfile"]
    assert "name" in result["createProfile"]
    assert result["createProfile"]["name"] == PROFILE_NAME


    # now fetch the updated profiles again to see it has changed
    response = client.get("/profiles/all")
    json_response = response.json()
    result = json_response["Result"]
    profiles = result["profiles"]

    assert len(profiles) == 1
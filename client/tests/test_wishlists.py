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

    # create client
    token = client.get("/clients/signup/", params={"email": CLIENT_EMAIL})

    # create profile

    # Run the tests
    yield

    # Run after tests

# def test_create():
#     """
#     Tests /watchlists/create
#     """

#     response = client.get("/wishlists/create")
#     json_response = response.json()
#     result = json_response["Result"]
#     profiles = result["profiles"]

#     assert len(profiles) == 0
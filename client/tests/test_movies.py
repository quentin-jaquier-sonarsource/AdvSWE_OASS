"""
This file tests the /movies endpoints
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
    Tests /movies/all
    """

    response = client.get("/movies/all")
    json_response = response.json()
    result = json_response["Result"]
    movies = result["movies"]

    assert len(movies) == 5

    titles = [x["details"]["title"] for x in movies]

    assert "Nope" in titles
    assert "The Matrix" in titles
    assert "Host" in titles
    assert "Skyfall" in titles
    assert "Casino Royale" in titles
    
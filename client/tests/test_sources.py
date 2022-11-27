"""
This file is meant to test every endpoint with the Movie Sources Endpoints tag
"""
from fastapi.testclient import TestClient
import pytest

from app.main import app
from tests.test_constants import (CLIENT_EMAIL, HOST_ID, HOST_TITLE, SOURCES_PREFIX)
from tests.utils import setup_end_to_end

client = TestClient(app)



@pytest.fixture(autouse=True)
def setUp():
    """
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
    tests /sources/all
    """

    response = client.get(SOURCES_PREFIX + "/all", params= {"id" : HOST_ID})
    json_resp = response.json()

    title = json_resp["Movie title"]
    info = json_resp["All Sources Info"]

    assert title == HOST_TITLE
    assert len(info) > 0

def test_sub():
    """
    tests /sources/sub
    """

    response = client.get(SOURCES_PREFIX + "/sub", params= {"id" : HOST_ID})
    json_resp = response.json()

    title = json_resp["Movie title"]
    sub_sources = json_resp["Sources that are free with subscription"]

    assert title == HOST_TITLE
    assert len(sub_sources) > 0
    assert "Shudder" in sub_sources


def test_rent():
    """
    tests /sources/rent
    """

    response = client.get(SOURCES_PREFIX + "/rent", params= {"id" : HOST_ID})
    json_resp = response.json()

    title = json_resp["Movie title"]
    rent_sources = json_resp["Sources where you can rent the given movie"]

    assert title == HOST_TITLE
    assert len(rent_sources) > 0
    assert "Shudder" not in rent_sources
    assert "Amazon" in rent_sources

def test_buy():
    """
    tests /sources/buy
    """

    response = client.get(SOURCES_PREFIX + "/buy", params= {"id" : HOST_ID})
    json_resp = response.json()

    title = json_resp["Movie title"]
    buy_sources = json_resp["Sources where you can buy the given movie"]

    assert title == HOST_TITLE
    assert len(buy_sources) > 0
    assert "Shudder" not in buy_sources
    assert "Amazon" in buy_sources
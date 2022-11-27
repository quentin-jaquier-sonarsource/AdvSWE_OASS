"""
This file is meant to test every endpoint with the Search Endpoints tag
"""
import pytest
from fastapi.testclient import TestClient

from app.main import app
from tests.test_constants import (CLIENT_EMAIL, HOST_ID, HOST_TITLE,
                                  SEARCH_PREFIX, SEARCH_TITLE)
from tests.utils import setup_end_to_end

client = TestClient(app)


@pytest.fixture(autouse=True)
def setUp():
    """
    Apparently it is frowned up in pytest to have a setup/teardown method like
    this, but I don't think it's good practice to keep calling the same function
    before every test function

    This runs setup code before every test function
    """

    # Run before tests
    setup_end_to_end()
    token = client.get("/clients/signup/", params={"email": CLIENT_EMAIL})

    # Run the tests
    yield

    # Run after tests

def test_limited():
    """
    tests /search/limited
    """

    response = client.get(SEARCH_PREFIX + "/limited", params= {"title" : SEARCH_TITLE})
    json_resp = response.json()

    titles = json_resp["Search Results"]["searchTitles"]

    assert len(titles) > 0

    first_title = titles[0]
    first_title_details = first_title["details"]

    # Make sure the output is limited, not verbose
    assert "genreNames" not in first_title_details
    assert "releaseDate" not in first_title_details
    assert "plotOverview" not in first_title_details
    
    # Assumes that Casino Royale (2006) is the most relevant movie to the search
    assert first_title_details["year"] == 2006
    assert first_title_details["runtimeMinutes"] == 144

def test_verbose():
    """
    tests /search/verbose
    """

    response = client.get(SEARCH_PREFIX + "/verbose", params= {"title" : SEARCH_TITLE})
    json_resp = response.json()

    titles = json_resp["Search Results"]["searchTitles"]


    assert len(titles) > 0

    first_title = titles[0]
    first_title_details = first_title["details"]

    assert "genreNames" in first_title_details
    assert "releaseDate" in first_title_details
    assert "plotOverview" in first_title_details
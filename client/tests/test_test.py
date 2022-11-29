"""
This tests that the test endpoints works
"""

import os

import psycopg2
from fastapi.testclient import TestClient
from requests import Response

from app.main import app
from tests.test_constants import CLIENT_EMAIL, DB_PASSWORD_ENV_VAR, DB_USER_NAME_ENV_VAR
from tests.utils import erase_clients, reset_token, setup_end_to_end

# db_conn = psycopg2.connect(
#     host = "localhost",
#     database = "testdb",
#     user = os.environ[DB_USER_NAME_ENV_VAR],
#     password = os.environ[DB_PASSWORD_ENV_VAR]
# )

client = TestClient(app)

def test_tests(mocker):
    """
    Not end to end because we're mocking
    """

    mock_return = {"test" : "test"}

    mocker.patch("app.routers.test.query_graphql_service", return_value = mock_return)

    response = client.get("/tests/graphql/")
    assert response.status_code == 200
    assert response.json() == {"Json Data" : mock_return}

def test_tests_end_to_end_fail():

    setup_end_to_end()

    # token = client.get("/clients/signup/", params={"email": CLIENT_EMAIL})

    response = client.get("/tests/")
    json_resp = response.json()

    assert "Exception: " in json_resp


def test_tests_end_to_end_happy():

    setup_end_to_end()

    token = client.get("/clients/signup/", params={"email": CLIENT_EMAIL})

    response = client.get("/tests/")
    json_resp = response.json()

    service_resp = json_resp["Response from service"]

    assert service_resp == "All good!"


def test_graphql():

    setup_end_to_end()

    token = client.get("/clients/signup/", params={"email": CLIENT_EMAIL})

    response = client.get("/tests/graphql/")
    json_resp = response.json()

    assert "Exception" not in json_resp
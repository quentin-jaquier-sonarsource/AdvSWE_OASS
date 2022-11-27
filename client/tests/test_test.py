"""
This tests that the test endpoints works
"""

import os

import psycopg2
from fastapi.testclient import TestClient
from requests import Response

from app.main import app
from tests.test_constants import DB_PASSWORD_ENV_VAR, DB_USER_NAME_ENV_VAR
from tests.utils import erase_clients

client = TestClient(app)

# db_conn = psycopg2.connect(
#     host = "localhost",
#     database = "testdb",
#     user = os.environ[DB_USER_NAME_ENV_VAR],
#     password = os.environ[DB_PASSWORD_ENV_VAR]
# )

def test_tests(mocker):
    """
    Not end to end because we're mocking
    """

    mock_return = {"test" : "test"}

    mocker.patch("app.routers.test.query_graphql_service", return_value = mock_return)

    response = client.get("/tests/graphql/")
    assert response.status_code == 200
    assert response.json() == {"Json Data" : mock_return}

def test_tests_end_to_end():
    erase_clients()

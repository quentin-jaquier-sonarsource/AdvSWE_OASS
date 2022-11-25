"""
This tests that the test endpoints works
"""

from fastapi.testclient import TestClient
from requests import Response

from app.main import app

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

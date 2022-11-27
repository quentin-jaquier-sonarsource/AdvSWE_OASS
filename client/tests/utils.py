
import os

import psycopg2
from fastapi.testclient import TestClient

from app.main import app
from app.routers.router_constants import TOKEN_PATH
from tests.test_constants import CLIENT_EMAIL, DB_PASSWORD_ENV_VAR, DB_USER_NAME_ENV_VAR

def reset_token():
    """
    Ensures that there is no token stored locally so we must call signup
    endpoint
    """
    if os.path.exists(TOKEN_PATH):
        os.remove(TOKEN_PATH)

def erase_clients():
    """
    Erases all rows of the client table
    """
    db_conn = psycopg2.connect(
        host = "localhost",
        database = "testdb",
        user = os.environ[DB_USER_NAME_ENV_VAR],
        password = os.environ[DB_PASSWORD_ENV_VAR]
    )

    cursor = db_conn.cursor()

    try:
        erase_table_query = """ DELETE from clients
        """

        cursor.execute(erase_table_query)

        # Make sure changes persist
        db_conn.commit()

    except (Exception, psycopg2.Error) as error:
        print(f"failure: {error.__str__()}")
    
    finally:
        if db_conn:
            cursor.close()
            db_conn.close()


def setup_end_to_end():
    
    reset_token()
    erase_clients()
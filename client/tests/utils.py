
import os
import psycopg2

from tests.test_constants import DB_PASSWORD_ENV_VAR, DB_USER_NAME_ENV_VAR


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

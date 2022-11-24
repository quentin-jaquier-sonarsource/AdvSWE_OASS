import os
import tempfile

URL = "http://localhost:8080"

TEST_URL = URL + "/test/"
SIGNUP_URL = URL+"/new-client/"

DEFAULT_TITLE = "Casino Royale"
DEFAULT_ID = 1616666 #Host (2020)

SUB = "sub"
RENT = "rent"
BUY = "buy"

SUB_DESCRIPTION = "Sources that are free with subscription"
RENT_DESCRIPTION = "Sources where you can rent the given movie"
BUY_DESCRIPTION = "Sources where you can buy the given movie"

# TOKEN_PATH = os.path.join(tempfile.gettempdir(), 'OASS_Client_token')
TOKEN_PATH = "./token.txt"
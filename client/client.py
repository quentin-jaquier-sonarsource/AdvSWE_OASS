import requests
from requests import Response

from constants import TEST_URL

# For after authorization is merged to main
LOGIN_PARAMS = {
    'username' : 'sachin',
    'password' : 'password',
    'email' : 'email'
    }

def test_signup():
    print("testing! Doesn't work rn because authorization has to be merged to main")
    signup_resp : Response = requests.get(url=TEST_URL)
    print(signup_resp.url)
    print(signup_resp.text)

def driver():
    test_signup()

if __name__ == "__main__":
    driver()
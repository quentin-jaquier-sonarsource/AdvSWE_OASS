GRAPHQL_URL = "http://localhost:8080/graphql/"
URL = "http://localhost:8080"

TEST_URL = URL + "/test/"

CLIENT_DESCRIPTION = """
Our Client is used to exercise our service end to end

TODO: update as we add more funcitonality to the client
"""

TEST_TAG = "Test Endpoints"
TEST_DESCRIPTION = "Simple endpoints used to test if the client and the service work"

SOURCE_TAG = "Movie Sources Endpoints"
SOURCE_TAG_DESCRIPTION = """
Endpoints related to movie sources. I.e. where one can buy, rent, and stream
movies. Movie sources are one of 3 types:
1. sub: a source that is free with subscription such as Netflix
2. rent: a source that requires you to rent the movie, e.g. Vudu
3. buy: a source that requires you to buy the movie, e.g. iTunes
"""

CLIENT_TAG = "Client Endpoints"
CLIENT_TAG_DESCRIPTION = "Endpoints related to client data"

PROFILE_TAG = "Profile Endpoints"
USER_TAG_DESCRIPTION = """"Endpoints related to profiles.
Profiles are groupings used by clients to serve end users."""

WISHLIST_TAG = "Wishlist Endpoints"
WISHLIST_TAG_DESCRIPTION = """ Endpoints related to wishlists
"""

SEARCH_TAG = "Search Endpoints"
SEARCH_TAG_DESCRIPTION = "Endpoints related to searching movies by title"

TAG_METADATA = [
    {
        "name" : TEST_TAG,
        "description" : TEST_DESCRIPTION
    },
    {
        "name": SEARCH_TAG,
        "description": SEARCH_TAG_DESCRIPTION
    },
    {
        "name" : SOURCE_TAG,
        "description" : SOURCE_TAG_DESCRIPTION
    },
    {
        "name": CLIENT_TAG,
        "description": CLIENT_TAG_DESCRIPTION
    },
    {
        "name": PROFILE_TAG,
        "description": USER_TAG_DESCRIPTION
    },
    {
        "name": WISHLIST_TAG,
        "description": WISHLIST_TAG_DESCRIPTION
    },
]
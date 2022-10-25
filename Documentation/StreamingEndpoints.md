# Streaming Endpoints

These are endpoints defined in `StreamingController.java`. They are used to
interact with the WatchMode API.

## `/streaming/available`

A test endpoint that calls the WatchMode API on a specific fixed movie (Skyfall (2012)) and
returns the names of all the streaming services where one can view this movie
for free with subscription.


## `/streaming/freeWithSub/{id}`

This endpoint queries the WatchMode API using the WatchMode `id` of a movie and
returns the names all streaming services where one can view the given movie
for free with subscription.

Notably what this does is filter out all the sources where the movie is available to buy or rent and only returns sources where it's free with subscription
Some example IDs and where they can be found for free with subscription:
  - 1586594 -> El Camino (2019) (netflix)
  - 1295258 -> Parasite (2019) (hulu)
  - 1616666 -> Host (2020) (shudder)
  - 1350564 -> Skyfall (2012) (amazon prime)
  - 130381 -> Annihilation (2018) (Paramount+)

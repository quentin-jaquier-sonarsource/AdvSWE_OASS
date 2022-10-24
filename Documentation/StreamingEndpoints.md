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

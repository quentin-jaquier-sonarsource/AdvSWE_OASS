-- This file ensures that the movies database is in an expected state

-- connect to db
\c testdb

-- Diplay current state
select * from movies;

-- erase current state
DELETE FROM movies;

-- create state
INSERT INTO movies (id, release_year, title, watchmode_id, critic_score, movie_gener, movie_name, movie_release_year, movie_runtime)
VALUES (1,2020, 'Host', 1616666, 100, 'Horror', 'Host', 2020, 65);

INSERT INTO movies (id, release_year, title, watchmode_id, critic_score, movie_gener, movie_name, movie_release_year, movie_runtime)
VALUES (2,2012, 'Skyfall', 1350564, 100, 'Action', 'Skyfall', 2012, 143);

INSERT INTO movies (id, release_year, title, watchmode_id, critic_score, movie_gener, movie_name, movie_release_year, movie_runtime)
VALUES (3,2006, 'Casino Royale', 168482, 100, 'Action', 'Casino Royale', 2006, 144);

INSERT INTO movies (id, release_year, title, watchmode_id, critic_score, movie_gener, movie_name, movie_release_year, movie_runtime)
VALUES (4,2022, 'Nope', 1591366, 100, 'Horror', 'Nope', 2022, 130);

INSERT INTO movies (id, release_year, title, watchmode_id, critic_score, movie_gener, movie_name, movie_release_year, movie_runtime)
VALUES (5,1999, 'The Matrix', 1406847, 100, 'Action', 'The Matrix', 1999, 136);

select * from movies;
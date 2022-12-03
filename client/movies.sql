-- This file ensures that the movies database is in an expected state

-- connect to db
\c testdb

-- Diplay current state
select * from movies;

-- erase current state
DELETE FROM movies;

-- create state
INSERT INTO movies (id, release_year, title, watchmode_id) VALUES (1,2020, 'Host', 1616666);
INSERT INTO movies (id, release_year, title, watchmode_id) VALUES (2,2012, 'Skyfall', 1350564);
INSERT INTO movies (id, release_year, title, watchmode_id) VALUES (3,2006, 'Casino Royale', 168482);
INSERT INTO movies (id, release_year, title, watchmode_id) VALUES (4,2022, 'Nope', 1591366);
INSERT INTO movies (id, release_year, title, watchmode_id) VALUES (5,1999, 'The Matrix', 1406847);

select * from movies;
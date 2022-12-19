insert into role (id, name, description) select 1, 'admin', 'admin' where not exists (select id from role where id=1);
insert into role (id, name, description) select 2, 'client', 'basic client' where not exists (select id from role where id=2);
insert into role (id, name, description) select 3, 'rating', 'has access to the rating endpoints' where not exists (select id from role where id=3);
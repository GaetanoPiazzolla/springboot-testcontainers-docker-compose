SET SEARCH_PATH = testcontainer;

create table person (
    id serial primary key,
    name varchar(255),
    email varchar(255)
);
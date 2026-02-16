create table if not exists USERS (
id serial not null  primary key ,
name VARCHAR(255) NOT NULL,
email varchar(255) not null );

create table if not exists requests (
id serial not null  primary key ,
description varchar(255) not null,
requester BIGINT  references users(id),
created timestamp WITHOUT TIME ZONE NOT NULL );

create table if not exists ITEMS (
id serial not null  primary key ,
name VARCHAR(255) NOT NULL,
description varchar(255) not null,
is_available boolean not null,
owner_id BIGINT  references users(id) ,
request_id BIGINT );

create table if not exists BOOKINGS (
    id serial not null  primary key ,
    start_date timestamp WITHOUT TIME ZONE  NOT NULL,
    end_date timestamp WITHOUT TIME ZONE NOT NULL,
    item_id BIGINT  references items(id),
    booker_id BIGINT  references users(id),
    status varchar(255) not null );


create table if not exists COMMENTS (
id serial not null  primary key,
text VARCHAR(255) NOT NULL,
item_id BIGINT  references items(id),
author_id  BIGINT references users(id),
created TIMESTAMP WITHOUT TIME ZONE NOT NULL);
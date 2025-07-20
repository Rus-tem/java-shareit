create table if not exists BOOKINGS (
id serial not null  primary key,
start_date timestamp without time zone not null,
end_date timestamp without time zone not null,
item_id bigint  not null,
booker_id bigint  not null,
status varchar(255) not null );

create table if not exists USERS (
id serial not null  primary key ,
name VARCHAR(255) NOT NULL,
email varchar(255) not null );

create table if not exists requests (
id serial not null  primary key ,
description varchar(255) not null,
requestor_id bigint  not null  );

create table if not exists ITEMS (
id serial not null  primary key ,
name VARCHAR(255) NOT NULL,
description varchar(255) not null,
is_available boolean not null,
owner_id bigint  references users(id) ,
request_id bigint  references requests(id) );

create table if not exists COMMENTS (
id serial not null  primary key,
text VARCHAR(255) NOT NULL,
item_id bigint  references items(id),
author_id  bigint references users(id),
created timestamp not null);

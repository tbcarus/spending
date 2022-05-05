create table users
(
    id char(36) not null
        constraint users_pk
            primary key,
    name text default 'Имя не задано'::text,
    email text not null,
    password text not null,
    start_period_date timestamp default now() not null
);

create unique index users_email_uindex
    on users (email);

create unique index users_id_uindex
    on users (id);

create table costs
(
    id char(36) not null
        constraint costs_pkey
            primary key,
    type text,
    price integer not null,
    description text,
    date timestamp default now() not null,
    user_id char(36)
        constraint costs_user_id_fk
            references users
            on delete cascade,
    date_of_creation timestamp default now()
);

create unique index costs_id_uindex
    on costs (id);


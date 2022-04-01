create table costs
(
    -- Only integer types can be auto increment
    id char(36) default nextval('costs_id_seq'::regclass) not null
        constraint costs_pk
            primary key,
    type text,
    prise integer not null,
    description text,
    date date default now() not null,
    user_id char(36)
);

alter table costs owner to postgres;

create unique index costs_id_uindex
    on costs (id);


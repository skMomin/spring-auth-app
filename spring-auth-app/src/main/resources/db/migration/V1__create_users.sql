create table if not exists users (
    id uuid primary key,
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    created_at timestamptz not null default current_timestamp
);

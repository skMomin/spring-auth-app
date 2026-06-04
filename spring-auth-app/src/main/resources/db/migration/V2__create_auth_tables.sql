create table if not exists roles (
    id bigserial primary key,
    code varchar(50) not null unique,
    description varchar(255) not null
);

create table if not exists user_roles (
    user_id uuid not null references users (id) on delete cascade,
    role_id bigint not null references roles (id) on delete cascade,
    assigned_at timestamptz not null default current_timestamp,
    primary key (user_id, role_id)
);

create table if not exists refresh_tokens (
    id uuid primary key,
    user_id uuid not null references users (id) on delete cascade,
    token_hash varchar(255) not null unique,
    expires_at timestamptz not null,
    revoked_at timestamptz,
    created_at timestamptz not null default current_timestamp
);

insert into roles (code, description)
select 'USER', 'Default application user'
where not exists (
    select 1
    from roles
    where code = 'USER'
);

insert into roles (code, description)
select 'ADMIN', 'Administrative user'
where not exists (
    select 1
    from roles
    where code = 'ADMIN'
);

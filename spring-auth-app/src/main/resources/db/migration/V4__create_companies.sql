create table if not exists companies (
    id uuid primary key,
    name varchar(255) not null,
    description text,
    owner_id uuid not null references users (id) on delete cascade,
    created_at timestamptz not null default current_timestamp
);

create index if not exists idx_companies_owner_id on companies (owner_id);

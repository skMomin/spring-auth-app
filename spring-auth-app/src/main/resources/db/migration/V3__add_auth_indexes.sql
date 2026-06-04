create index if not exists idx_refresh_tokens_user_id on refresh_tokens (user_id);
create index if not exists idx_refresh_tokens_expires_at on refresh_tokens (expires_at);

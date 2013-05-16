-- migration file for Cinnamon 3 repositories to Cinnamon 3.0.0.34
-- (Not required when upgrading from 2.5.2 to 3.0.0)

alter table relationtypes add column clone_on_left_version boolean not null default false;
alter table relationtypes add column clone_on_right_version boolean not null default false;

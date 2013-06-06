--  This table may be created in a separate database for security and performance reasons

  create table lifecycle_log(id serial  primary key, repository character varying(255) not null,
  hibernate_id bigint not null, 
  user_name varchar(255) not null, user_id bigint not null, date_created timestamp without time zone not null, 
  lifecycle_id bigint not null, lifecycle_name varchar(255) not null, old_state_id bigint not null, 
  old_state_name varchar(255) not null, new_state_id bigint not null, new_state_name varchar(255) not null, 
  folder_path varchar(8191) not null, name varchar(255) not null );
  
--   Table "public.lifecycle_log"
--      Column     |            Type             | Modifiers 
-- ----------------+-----------------------------+-----------
--  id             | bigint                      | not null default nextval('lifecycle_log_id_seq'::regclass)
--  repository     | character varying(255)      | not null
--  hibernate_id   | bigint                      | not null
--  user_name      | character varying(255)      | not null
--  user_id        | bigint                      | not null
--  date_created   | timestamp without time zone | not null
--  lifecycle_id   | bigint                      | not null
--  lifecycle_name | character varying(255)      | not null
--  old_state_id   | bigint                      | not null
--  old_state_name | character varying(255)      | not null
--  new_state_id   | bigint                      | not null
--  new_state_name | character varying(255)      | not null
--  folder_path    | character varying(8191)     | not null
--  name           | character varying(255)      | not null
--  
--     Indexes:
--         "lifecycle_log_pkey" PRIMARY KEY, btree (id)

 -- substitute with your local connection:
 insert into customtables values(1,'jdbc:postgresql://127.0.0.1/demo?user=cinnamon&password=cinnamon',
 'org.postgresql.Driver','audit.connection', 0, 3)

-- you must change the id of the inserted type to a valid value - or use Dandelion to create it.
 insert into change_trigger_types(id, description, name, trigger_class) values(
 2, 'Lifecyclestate Audit ChangeTrigger','lifecycle.state.trigger','server.trigger.impl.LifecycleStateAuditTrigger');

-- you must change the id of the inserted changeTrigger to a valid value - or use Dandelion to create the change trigger.
 insert into change_triggers values(6,true,0,100,2,'changestate', true,true,'<config />');
 grant SELECT,INSERT on lifecycle_log TO cinnamon ;
 grant USAGE on lifecycle_log_id_seq TO cinnamon ;
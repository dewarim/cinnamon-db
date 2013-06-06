-- Database: cinnamon_audit

-- DROP DATABASE cinnamon_audit;

CREATE DATABASE cinnamon_audit
  WITH OWNER = cinnamon
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'de_DE.UTF-8'
       LC_CTYPE = 'de_DE.UTF-8'
       CONNECTION LIMIT = -1;

-- Table: audit_log
-- DROP TABLE audit_log;

\c cinnamon_audit

CREATE TABLE audit_log
(
  id bigserial NOT NULL,
  repository character varying(255),
  hibernate_id character varying(24),
  username character varying(255),
  user_id character varying(255),
  field_name character varying(255),
  old_value_name character varying(255),
  new_value_name character varying(255),
  object_name character varying(4000),
  old_value text,
  new_value text,
  metadata text,
  class_name character varying(255),
  date_created timestamp with time zone,
  log_message text,
  CONSTRAINT pk_audit_log_id PRIMARY KEY (id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE audit_log
  OWNER TO cinnamon;

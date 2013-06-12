CREATE SEQUENCE lifecycle_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.lifecycle_log_id_seq OWNER TO postgres;

--
-- Name: lifecycle_log; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE lifecycle_log (
    id integer DEFAULT nextval('lifecycle_log_id_seq'::regclass) NOT NULL,
    repository character varying(255) NOT NULL,
    hibernate_id bigint NOT NULL,
    user_name character varying(255) NOT NULL,
    user_id bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    lifecycle_id bigint NOT NULL,
    lifecycle_name character varying(255) NOT NULL,
    old_state_id bigint NOT NULL,
    old_state_name character varying(255) NOT NULL,
    new_state_id bigint NOT NULL,
    new_state_name character varying(255) NOT NULL,
    folder_path character varying(8191) NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.lifecycle_log OWNER TO cinnamon;

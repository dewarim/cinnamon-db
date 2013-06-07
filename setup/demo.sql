--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: aclentries; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE aclentries (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    acl_id bigint NOT NULL,
    group_id bigint NOT NULL
);


ALTER TABLE public.aclentries OWNER TO cinnamon;

--
-- Name: aclentry_permissions; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE aclentry_permissions (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    aclentry_id bigint NOT NULL,
    permission_id bigint NOT NULL
);


ALTER TABLE public.aclentry_permissions OWNER TO cinnamon;

--
-- Name: acls; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE acls (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    name character varying(128) NOT NULL
);


ALTER TABLE public.acls OWNER TO cinnamon;

--
-- Name: change_trigger_types; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE change_trigger_types (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    name character varying(128) NOT NULL,
    trigger_class character varying(255) NOT NULL
);


ALTER TABLE public.change_trigger_types OWNER TO cinnamon;

--
-- Name: change_triggers; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE change_triggers (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    action character varying(255) NOT NULL,
    active boolean NOT NULL,
    config character varying(10485760) NOT NULL,
    controller character varying(255) NOT NULL,
    post_trigger boolean NOT NULL,
    pre_trigger boolean NOT NULL,
    ranking integer NOT NULL,
    change_trigger_type_id bigint NOT NULL
);


ALTER TABLE public.change_triggers OWNER TO cinnamon;

--
-- Name: config_entries; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE config_entries (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    config character varying(10485760) NOT NULL,
    name character varying(128) NOT NULL
);


ALTER TABLE public.config_entries OWNER TO cinnamon;

--
-- Name: customtables; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE customtables (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    acl_id bigint NOT NULL,
    connstring character varying(512) NOT NULL,
    jdbc_driver character varying(128) NOT NULL,
    name character varying(128) NOT NULL
);


ALTER TABLE public.customtables OWNER TO cinnamon;

--
-- Name: folder_metasets; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE folder_metasets (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    folder_id bigint NOT NULL,
    metaset_id bigint NOT NULL
);


ALTER TABLE public.folder_metasets OWNER TO cinnamon;

--
-- Name: folder_types; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE folder_types (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    config character varying(10485760) NOT NULL,
    name character varying(128) NOT NULL
);


ALTER TABLE public.folder_types OWNER TO cinnamon;

--
-- Name: folders; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE folders (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    acl_id bigint NOT NULL,
    metadata character varying(10485760) NOT NULL,
    name character varying(128) NOT NULL,
    owner_id bigint NOT NULL,
    parent_id bigint,
    type_id bigint NOT NULL
);


ALTER TABLE public.folders OWNER TO cinnamon;

--
-- Name: formats; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE formats (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    contenttype character varying(128) NOT NULL,
    default_object_type_id bigint,
    extension character varying(64) NOT NULL,
    name character varying(128) NOT NULL
);


ALTER TABLE public.formats OWNER TO cinnamon;

--
-- Name: group_users; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE group_users (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    group_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.group_users OWNER TO cinnamon;

--
-- Name: groups; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE groups (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    group_of_one boolean NOT NULL,
    name character varying(128) NOT NULL,
    parent_id bigint
);


ALTER TABLE public.groups OWNER TO cinnamon;

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: cinnamon
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO cinnamon;

--
-- Name: index_groups; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE index_groups (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    name character varying(128) NOT NULL
);


ALTER TABLE public.index_groups OWNER TO cinnamon;

--
-- Name: index_items; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE index_items (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    fieldname character varying(255) NOT NULL,
    for_content boolean NOT NULL,
    for_metadata boolean NOT NULL,
    for_sys_meta boolean NOT NULL,
    index_group_id bigint NOT NULL,
    index_type_id bigint NOT NULL,
    multiple_results boolean NOT NULL,
    name character varying(128) NOT NULL,
    search_condition character varying(10485760) NOT NULL,
    search_string character varying(10485760) NOT NULL,
    store_field boolean NOT NULL,
    systemic boolean NOT NULL,
    va_params character varying(10485760) NOT NULL
);


ALTER TABLE public.index_items OWNER TO cinnamon;

--
-- Name: index_jobs; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE index_jobs (
    id bigint NOT NULL,
    failed boolean NOT NULL,
    indexable_class character varying(255) NOT NULL,
    indexable_id bigint NOT NULL
);


ALTER TABLE public.index_jobs OWNER TO cinnamon;

--
-- Name: index_types; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE index_types (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    data_type character varying(255) NOT NULL,
    indexer_class character varying(255) NOT NULL,
    name character varying(128) NOT NULL,
    va_provider_class character varying(255) NOT NULL
);


ALTER TABLE public.index_types OWNER TO cinnamon;

--
-- Name: languages; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE languages (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    iso_code character varying(32) NOT NULL,
    metadata character varying(10485760) NOT NULL
);


ALTER TABLE public.languages OWNER TO cinnamon;

--
-- Name: lifecycle_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

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

--
-- Name: lifecycle_states; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE lifecycle_states (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    config character varying(10485760) NOT NULL,
    life_cycle_id bigint,
    life_cycle_state_for_copy_id bigint,
    name character varying(128) NOT NULL,
    state_class character varying(255) NOT NULL
);


ALTER TABLE public.lifecycle_states OWNER TO cinnamon;

--
-- Name: lifecycles; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE lifecycles (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    default_state_id bigint,
    name character varying(128) NOT NULL
);


ALTER TABLE public.lifecycles OWNER TO cinnamon;

--
-- Name: links; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE links (
    id bigint NOT NULL,
    version bigint NOT NULL,
    acl_id bigint NOT NULL,
    folder_id bigint,
    osd_id bigint,
    owner_id bigint NOT NULL,
    parent_id bigint NOT NULL,
    resolver character varying(255) NOT NULL,
    type character varying(255) NOT NULL
);


ALTER TABLE public.links OWNER TO cinnamon;

--
-- Name: messages; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE messages (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    ui_language_id bigint NOT NULL,
    message character varying(255) NOT NULL,
    translation character varying(255) NOT NULL
);


ALTER TABLE public.messages OWNER TO cinnamon;

--
-- Name: metaset_types; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE metaset_types (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    config character varying(10485760) NOT NULL,
    name character varying(128) NOT NULL
);


ALTER TABLE public.metaset_types OWNER TO cinnamon;

--
-- Name: metasets; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE metasets (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    content character varying(10485760) NOT NULL,
    type_id bigint NOT NULL
);


ALTER TABLE public.metasets OWNER TO cinnamon;

--
-- Name: objects; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE objects (
    id bigint NOT NULL,
    version bigint NOT NULL,
    acl_id bigint NOT NULL,
    appname character varying(255) NOT NULL,
    cmn_version character varying(128) NOT NULL,
    content_path character varying(255),
    content_size bigint,
    created timestamp without time zone NOT NULL,
    creator_id bigint NOT NULL,
    format_id bigint,
    language_id bigint NOT NULL,
    latest_branch boolean NOT NULL,
    latest_head boolean NOT NULL,
    locker_id bigint,
    metadata character varying(10485760) NOT NULL,
    modified timestamp without time zone NOT NULL,
    modifier_id bigint NOT NULL,
    name character varying(255) NOT NULL,
    owner_id bigint NOT NULL,
    parent_id bigint NOT NULL,
    predecessor_id bigint,
    procstate character varying(128) NOT NULL,
    root_id bigint,
    state_id bigint,
    type_id bigint NOT NULL
);


ALTER TABLE public.objects OWNER TO cinnamon;

--
-- Name: objtypes; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE objtypes (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    config character varying(10485760) NOT NULL,
    name character varying(128) NOT NULL
);


ALTER TABLE public.objtypes OWNER TO cinnamon;

--
-- Name: osd_metasets; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE osd_metasets (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    metaset_id bigint NOT NULL,
    osd_id bigint NOT NULL
);


ALTER TABLE public.osd_metasets OWNER TO cinnamon;

--
-- Name: permissions; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE permissions (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    name character varying(128) NOT NULL
);


ALTER TABLE public.permissions OWNER TO cinnamon;

--
-- Name: relation_resolvers; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE relation_resolvers (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    config character varying(10485760) NOT NULL,
    name character varying(128) NOT NULL,
    class_name character varying(255) NOT NULL
);


ALTER TABLE public.relation_resolvers OWNER TO cinnamon;

--
-- Name: relations; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE relations (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    left_id bigint NOT NULL,
    metadata character varying(255) NOT NULL,
    right_id bigint NOT NULL,
    type_id bigint NOT NULL
);


ALTER TABLE public.relations OWNER TO cinnamon;

--
-- Name: relationtypes; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE relationtypes (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    clone_on_left_copy boolean NOT NULL,
    clone_on_left_version boolean NOT NULL,
    clone_on_right_copy boolean NOT NULL,
    clone_on_right_version boolean NOT NULL,
    left_resolver_id bigint NOT NULL,
    leftobjectprotected boolean NOT NULL,
    name character varying(128) NOT NULL,
    right_resolver_id bigint NOT NULL,
    rightobjectprotected boolean NOT NULL
);


ALTER TABLE public.relationtypes OWNER TO cinnamon;

--
-- Name: sessions; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE sessions (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    expires timestamp without time zone NOT NULL,
    ui_language_id bigint NOT NULL,
    lifetime bigint NOT NULL,
    machinename character varying(128) NOT NULL,
    message character varying(65000) NOT NULL,
    ticket character varying(255) NOT NULL,
    user_id bigint NOT NULL,
    username character varying(128) NOT NULL
);


ALTER TABLE public.sessions OWNER TO cinnamon;

--
-- Name: transformers; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE transformers (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    name character varying(255) NOT NULL,
    source_format_id bigint NOT NULL,
    target_format_id bigint NOT NULL,
    transformer_class character varying(255) NOT NULL
);


ALTER TABLE public.transformers OWNER TO cinnamon;

--
-- Name: ui_languages; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE ui_languages (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    iso_code character varying(32) NOT NULL
);


ALTER TABLE public.ui_languages OWNER TO cinnamon;

--
-- Name: users; Type: TABLE; Schema: public; Owner: cinnamon; Tablespace: 
--

CREATE TABLE users (
    id bigint NOT NULL,
    obj_version bigint NOT NULL,
    account_expired boolean NOT NULL,
    account_locked boolean NOT NULL,
    activated boolean NOT NULL,
    description character varying(255) NOT NULL,
    email character varying(255),
    fullname character varying(255) NOT NULL,
    language_id bigint,
    name character varying(128) NOT NULL,
    password_expired boolean NOT NULL,
    pwd character varying(255) NOT NULL,
    sudoable boolean NOT NULL,
    sudoer boolean NOT NULL,
    token character varying(255) NOT NULL,
    token_age timestamp without time zone NOT NULL,
    tokens_today integer NOT NULL
);


ALTER TABLE public.users OWNER TO cinnamon;

--
-- Data for Name: aclentries; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY aclentries (id, obj_version, acl_id, group_id) FROM stdin;
44	0	30	42
\.


--
-- Data for Name: aclentry_permissions; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY aclentry_permissions (id, obj_version, aclentry_id, permission_id) FROM stdin;
\.


--
-- Data for Name: acls; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY acls (id, obj_version, name) FROM stdin;
31	0	review.acl
32	0	released.acl
33	0	authoring.acl
30	1	_default_acl
\.


--
-- Data for Name: change_trigger_types; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY change_trigger_types (id, obj_version, name, trigger_class) FROM stdin;
78	0	RelationChangeTriggerType	cinnamon.trigger.impl.RelationChangeTrigger
\.


--
-- Data for Name: change_triggers; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY change_triggers (id, obj_version, action, active, config, controller, post_trigger, pre_trigger, ranking, change_trigger_type_id) FROM stdin;
79	0	setmeta	t	<meta />	cinnamon	t	t	100	78
80	0	delete	t	<meta />	cinnamon	t	t	100	78
81	0	setsysmeta	t	<meta />	cinnamon	t	t	100	78
82	0	saveObject	t	<meta />	osd	t	t	100	78
83	0	version	t	<meta />	cinnamon	t	t	100	78
84	0	setcontent	t	<meta />	cinnamon	t	t	100	78
85	0	create	t	<meta />	cinnamon	t	t	100	78
86	0	newVersion	t	<meta />	osd	t	t	100	78
87	0	saveField	t	<meta />	osd	t	t	100	78
88	0	saveMetadata	t	<meta />	osd	t	t	100	78
89	0	saveContent	t	<meta />	osd	t	t	100	78
90	0	iterate	t	<meta />	osd	t	t	100	78
\.


--
-- Data for Name: config_entries; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY config_entries (id, obj_version, config, name) FROM stdin;
97	0	<config><aclForTranslatedObjects>_default_acl</aclForTranslatedObjects></config>	translation.config
98  0 <config><logEverything>false</logEverything></config> audit.trail.filter
\.


--
-- Data for Name: customtables; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY customtables (id, obj_version, acl_id, connstring, jdbc_driver, name) FROM stdin;
\.


--
-- Data for Name: folder_metasets; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY folder_metasets (id, obj_version, folder_id, metaset_id) FROM stdin;
\.


--
-- Data for Name: folder_types; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY folder_types (id, obj_version, config, name) FROM stdin;
34	0	<meta />	_folder_reference
35	0	<meta />	_default_folder_type
\.


--
-- Data for Name: folders; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY folders (id, obj_version, acl_id, metadata, name, owner_id, parent_id, type_id) FROM stdin;
151	0	30	<meta />	root	36	151	35
152	0	30	<meta />	system	36	151	35
153	0	30	<meta />	users	36	152	35
154	0	30	<meta />	admin	36	153	35
155	0	30	<meta />	home	36	154	35
156	0	30	<meta />	templates	36	151	35
157	0	30	<meta />	test	36	152	35
158	0	30	<meta />	workflows	36	152	35
159	0	30	<meta />	transient	36	152	35
160	0	30	<meta />	translation_tasks	36	159	35
161	0	30	<meta />	render_tasks	36	159	35
162	0	30	<meta />	config	36	154	35
163	0	30	<meta />	tasks	36	158	35
164	0	30	<meta />	searches	36	154	35
165	0	30	<meta />	carts	36	154	35
166	0	30	<meta />	templates	36	158	35
167	0	30	<meta />	applications	36	152	35
168	0	30	<meta />	task_definitions	36	158	35
169	0	30	<meta />	config	36	152	35
170	0	30	<meta />	custom	36	152	35
\.


--
-- Data for Name: formats; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY formats (id, obj_version, contenttype, default_object_type_id, extension, name) FROM stdin;
103	0	application/vnd.ms-excel	\N	xls	format.xls
104	0	application/vnd.openxmlformats-officedocument.spreadsheetml.sheet	\N	xlsx	format.xlsx
105	0	image/jpeg	\N	jpeg	format.jpeg
106	0	application/pdf	\N	pdf	format.pdf
107	0	text/js	\N	js	format.js
108	0	application/vnd.openxmlformats-officedocument.presentationml.presentation	\N	pptx	format.pptx
109	0	application/fm	\N	fm	format.fm
110	0	application/vnd.oasis.opendocument.image-template	\N	oti	format.oti
111	0	application/vnd.oasis.opendocument.text-web	\N	oth	format.oth
112	0	application/vnd.oasis.opendocument.graphics-template	\N	otg	format.otg
113	0	application/vnd.oasis.opendocument.formula-template	\N	otf	format.otf
114	0	application/vnd.oasis.opendocument.text-template	\N	ott	format.ott
115	0	image/cgm	\N	cgm	format.cgm
116	0	application/vnd.oasis.opendocument.spreadsheet-template	\N	ots	format.ots
117	0	application/xml-dtd	\N	dtd	format.dtd
118	0	application/vnd.oasis.opendocument.presentation-template	\N	otp	format.otp
119	0	image/tiff	\N	tiff	format.tiff
120	0	application/vnd.oasis.opendocument.graphics	\N	odg	format.odg
121	0	application/vnd.oasis.opendocument.formula	\N	odf	format.odf
122	0	application/vnd.oasis.opendocument.image	\N	odi	format.odi
123	0	application/vnd.oasis.opendocument.presentation	\N	odp	format.odp
124	0	application/xml	\N	dita	format.dita
125	0	application/xml	\N	xml	format.xml
126	0	application/vnd.oasis.opendocument.text-master	\N	odm	format.odm
127	0	application/vnd.oasis.opendocument.chart-template	\N	otc	format.otc
128	0	application/vnd.ms-powerpoint	\N	pps	format.pps
129	0	application/cdr	\N	cdr	format.cdr
130	0	application/vnd.oasis.opendocument.spreadsheet	\N	ods	format.ods
131	0	application/x-vnd.oasis.opendocument.text	\N	odt	format.odt
132	0	application/vnd.ms-powerpoint	\N	ppt	format.ppt
133	0	application/clp	\N	clp	format.clp
134	0	image/eps	\N	eps	format.eps
135	0	text/csv	\N	csv	format.csv
136	0	application/vnd.oasis.opendocument.chart	\N	odc	format.odc
137	0	text/css	\N	css	format.css
138	0	image/gif	\N	gif	format.gif
139	0	application/vnd.oasis.opendocument.database	\N	odb	format.odb
140	0	application/xml	\N	ditamap	format.ditamap
141	0	application/xhtml+xml	\N	xhtml	format.xhtml
142	0	text/plain	\N	txt	format.txt
143	0	application/xml	\N	ddn	format.ddn
144	0	application/xml	\N	dm	format.dm
145	0	application/zip	\N	zip	format.zip
146	0	image/png	\N	png	format.png
147	0	application/xml	\N	xsd	format.xsd
148	0	application/msword	\N	doc	format.doc
149	0	text/html	\N	html	format.html
150	0	application/vnd.openxmlformats-officedocument.wordprocessingml.document	\N	docx	format.docx
\.


--
-- Data for Name: group_users; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY group_users (id, obj_version, group_id, user_id) FROM stdin;
41	0	40	36
43	0	37	36
45	0	42	36
\.


--
-- Data for Name: groups; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY groups (id, obj_version, group_of_one, name, parent_id) FROM stdin;
38	0	f	_owner	\N
39	0	f	_everyone	\N
40	1	f	_superusers	\N
37	1	f	_users	\N
42	2	t	_36_admin	\N
\.


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: cinnamon
--

SELECT pg_catalog.setval('hibernate_sequence', 246, true);


--
-- Data for Name: index_groups; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY index_groups (id, obj_version, name) FROM stdin;
172	31	_default_index_group
221	5	dita.content
\.


--
-- Data for Name: index_items; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY index_items (id, obj_version, fieldname, for_content, for_metadata, for_sys_meta, index_group_id, index_type_id, multiple_results, name, search_condition, search_string, store_field, systemic, va_params) FROM stdin;
173	0	active_workflow	f	t	f	172	171	f	index.workflow.active_workflow	true()	/meta/metaset[@type='workflow_template']/active_workflow	f	t	<vaParams />
177	0	lockedby	f	f	t	172	176	f	index.lockedby	true()	/sysMeta/object/lockedBy/id	f	t	<vaParams />
179	0	folderpath	f	f	t	172	178	f	index.folder.path	true()	/sysMeta/folder/parentId	f	t	<vaParams />
181	0	content	f	t	f	172	180	f	index.tika	true()	/meta/metaset[@type='tika']/html/body	f	t	<vaParams />
183	0	name	f	f	t	172	175	f	index.name	true()	/sysMeta/object/name	f	t	<vaParams />
186	0	folderpath	f	f	t	172	178	f	index.path	true()	/sysMeta/folder/parentId	f	t	<vaParams />
187	0	lifecyclestate	f	f	t	172	176	f	index.lifecycle.state	true()	/sysMeta/object/lifeCycleState	f	t	<vaParams />
188	0	modifier	f	f	t	172	176	f	index.format	true()	/sysMeta/object/modifier/id	f	t	<vaParams />
189	0	root	f	f	t	172	176	f	index.root	true()	/sysMeta/object/rootId	f	t	<vaParams />
191	0	workflow_deadline	f	t	f	172	190	f	index.workflow_deadline	true()	/meta/metaset[@type='task_definition' or @type='workflow_template']/deadline	f	t	<vaParams />
192	0	folderType	f	f	t	172	176	f	index.folder.type	true()	/sysMeta/folder/typeId	f	t	<vaParams />
193	0	parent_id	f	f	t	172	176	f	index.parentId	true()	/sysMeta/object/parentId	f	t	<vaParams />
194	0	objecttype	f	f	t	172	176	f	index.objecttype	true()	/sysMeta/object/objectType/id	f	t	<vaParams />
195	0	modifier	f	f	t	172	176	f	index.modifier	true()	/sysMeta/object/modifier/id	f	t	<vaParams />
196	0	language	f	f	t	172	176	f	index.language	true()	/sysMeta/object/language/id	f	t	<vaParams />
197	0	modified.time	f	f	t	172	174	f	index.modified.time	true()	/sysMeta/object/modified	f	t	<vaParams />
200	0	owner	f	f	t	172	176	f	index.folder.owner	true()	/sysMeta/folder/owner/id	f	t	<vaParams />
201	0	owner	f	f	t	172	176	f	index.owner	true()	/sysMeta/object/owner/id	f	t	<vaParams />
204	0	creator	f	f	t	172	176	f	index.creator	true()	/sysMeta/object/creator/id	f	t	<vaParams />
206	0	parent_id	f	f	t	172	176	f	index.folder.parent_id	true()	/sysMeta/folder/parentId	f	t	<vaParams />
207	0	objectTypeName	f	f	t	172	182	f	index.object_type_name	true()	/sysMeta/object/objectType/sysName	f	t	<vaParams />
209	0	contentsize	f	f	t	172	176	f	index.contentsize	true()	/sysMeta/object/contentsize	f	t	<vaParams />
210	0	acl	f	f	t	172	176	f	index.acl	true()	/sysMeta/object/aclId	f	t	<vaParams />
211	0	created.time	f	f	t	172	174	f	index.created.time	true()	/sysMeta/object/created	f	t	<vaParams />
212	0	latesthead	f	f	t	172	171	f	index.latesthead	true()	/sysMeta/folder/latestHead	f	t	<vaParams />
213	0	version	f	f	t	172	175	f	index.version	true()	/sysMeta/object/version	f	t	<vaParams />
214	0	modified.date	f	f	t	172	203	f	index.modified.date	true()	/sysMeta/object/modified	f	t	<vaParams />
217	0	appname	f	f	t	172	175	f	index.appname	true()	/sysMeta/object/appname	f	t	<vaParams />
218	0	name	f	f	t	172	175	f	index.folder.name	true()	/sysMeta/folder/name	f	t	<vaParams />
219	0	latestbranch	f	f	t	172	171	f	index.latestbranch	true()	/sysMeta/folder/latestBranch	f	t	<vaParams />
220	0	created.date	f	f	t	172	203	f	index.created.date	true()	/sysMeta/object/created	f	t	<vaParams />
222	0	dita.content	t	f	f	221	180	f	ditacontent.topic	string(/sysMeta/object/appName)='dita'	/topic/body	f	f	<vaParams />
223	0	dita.title	t	f	f	221	175	f	ditacontent.title	string(/sysMeta/object/appName)='dita'	//title	f	f	<vaParams />
224	0	dita.content	t	f	f	221	180	f	ditacontent.task	string(/sysMeta/object/appName)='dita'	/task/taskbody	f	f	<vaParams />
225	0	dita.content	t	f	f	221	180	f	ditacontent.reference	string(/sysMeta/object/appName)='dita'	/reference/refbody	f	f	<vaParams />
226	0	dita.content	t	f	f	221	180	f	ditacontent.concept	string(/sysMeta/object/appName)='dita'	/concept/conbody	f	f	<vaParams />
\.


--
-- Data for Name: index_jobs; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY index_jobs (id, failed, indexable_class, indexable_id) FROM stdin;
227	f	cinnamon.Folder	151
228	f	cinnamon.Folder	152
229	f	cinnamon.Folder	153
230	f	cinnamon.Folder	154
231	f	cinnamon.Folder	155
232	f	cinnamon.Folder	156
233	f	cinnamon.Folder	157
234	f	cinnamon.Folder	158
235	f	cinnamon.Folder	159
236	f	cinnamon.Folder	160
237	f	cinnamon.Folder	161
238	f	cinnamon.Folder	162
239	f	cinnamon.Folder	163
240	f	cinnamon.Folder	164
241	f	cinnamon.Folder	165
242	f	cinnamon.Folder	166
243	f	cinnamon.Folder	167
244	f	cinnamon.Folder	168
245	f	cinnamon.Folder	169
246	f	cinnamon.Folder	170
\.


--
-- Data for Name: index_types; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY index_types (id, obj_version, data_type, indexer_class, name, va_provider_class) FROM stdin;
171	0	BOOLEAN	cinnamon.index.indexer.BooleanXPathIndexer	xpath.boolean_indexer	cinnamon.index.valueAssistance.DefaultProvider
174	0	TIME	cinnamon.index.indexer.TimeXPathIndexer	xpath.time_indexer	cinnamon.index.valueAssistance.DefaultProvider
175	0	TEXT	cinnamon.index.indexer.DefaultIndexer	xpath.string_indexer	cinnamon.index.valueAssistance.DefaultProvider
176	0	INTEGER	cinnamon.index.indexer.IntegerXPathIndexer	xpath.integer_indexer	cinnamon.index.valueAssistance.DefaultProvider
178	0	STRING	cinnamon.index.indexer.ParentFolderPathIndexer	xpath.parent_folder_path_indexer	cinnamon.index.valueAssistance.DefaultProvider
180	0	STRING	cinnamon.index.indexer.DescendingStringIndexer	xpath.descending_string_indexer	cinnamon.index.valueAssistance.DefaultProvider
182	0	STRING	cinnamon.index.indexer.CompleteStringIndexer	xpath.complete_string_indexer	cinnamon.index.valueAssistance.DefaultProvider
184	0	STRING	cinnamon.index.indexer.EncodedFieldIndexer	xpath.encoded_field_indexer	cinnamon.index.valueAssistance.DefaultProvider
185	0	INTEGER	cinnamon.index.indexer.CountIndexer	xpath.count_indexer	cinnamon.index.valueAssistance.DefaultProvider
190	0	INTEGER	cinnamon.index.indexer.DateTimeIndexer	xpath.date_time_indexer	cinnamon.index.valueAssistance.DefaultProvider
198	0	STRING	cinnamon.index.indexer.CompleteStringExpressionIndexer	xpath.complete_string_expression_indexer	cinnamon.index.valueAssistance.DefaultProvider
199	0	STRING	cinnamon.index.indexer.DescendingReverseStringIndexer	xpath.descending_reverse_string_indexer	cinnamon.index.valueAssistance.DefaultProvider
202	0	STRING	cinnamon.index.indexer.ReverseCompleteStringIndexer	xpath.reverse_complete_string_indexer	cinnamon.index.valueAssistance.DefaultProvider
203	0	DATE_TIME	cinnamon.index.indexer.DateXPathIndexer	xpath.date_indexer	cinnamon.index.valueAssistance.DefaultProvider
205	0	STRING	cinnamon.index.indexer.ReverseStringIndexer	xpath.reverse_string_indexer	cinnamon.index.valueAssistance.DefaultProvider
208	0	STRING	cinnamon.index.indexer.DescendingReverseCompleteStringIndexer	xpath.descending_reverse_complete_string_indexer	cinnamon.index.valueAssistance.DefaultProvider
215	0	STRING	cinnamon.index.indexer.DescendingCompleteStringIndexer	xpath.descending_complete_string_indexer	cinnamon.index.valueAssistance.DefaultProvider
216	0	DECIMAL	cinnamon.index.indexer.DecimalXPathIndexer	xpath.decimal_indexer	cinnamon.index.valueAssistance.DefaultProvider
\.


--
-- Data for Name: languages; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY languages (id, obj_version, iso_code, metadata) FROM stdin;
94	0	zxx	<meta />
95	0	mul	<meta />
96	0	und	<meta />
\.


--
-- Data for Name: lifecycle_log; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY lifecycle_log (id, repository, hibernate_id, user_name, user_id, date_created, lifecycle_id, lifecycle_name, old_state_id, old_state_name, new_state_id, new_state_name, folder_path, name) FROM stdin;
\.


--
-- Name: lifecycle_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('lifecycle_log_id_seq', 1, false);


--
-- Data for Name: lifecycle_states; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY lifecycle_states (id, obj_version, config, life_cycle_id, life_cycle_state_for_copy_id, name, state_class) FROM stdin;
99	0	<meta />	98	\N	executeRenderTask	cinnamon.lifecycle.state.NopState
100	0	<meta />	98	\N	finishedRenderTask	cinnamon.lifecycle.state.NopState
101	0	<meta />	98	\N	failedRenderTask	cinnamon.lifecycle.state.NopState
102	0	<meta />	98	\N	newRenderTask	cinnamon.lifecycle.state.NopState
\.


--
-- Data for Name: lifecycles; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY lifecycles (id, obj_version, default_state_id, name) FROM stdin;
98	5	102	_RenderServerLC
\.


--
-- Data for Name: links; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY links (id, version, acl_id, folder_id, osd_id, owner_id, parent_id, resolver, type) FROM stdin;
\.


--
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY messages (id, obj_version, ui_language_id, message, translation) FROM stdin;
\.


--
-- Data for Name: metaset_types; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY metaset_types (id, obj_version, config, name) FROM stdin;
1	0	<metaset />	translation_folder
2	0	<metaset />	test
3	0	<metaset />	render_input
4	0	<metaset />	transition
5	0	<metaset />	cart
6	0	<metaset />	task_definition
7	0	<metaset />	log
8	0	<metaset />	render_output
9	0	<metaset />	workflow_template
10	0	<metaset />	tika
11	0	<metaset />	search
12	0	<metaset />	translation_extension
13	0	<metaset />	notification
\.


--
-- Data for Name: metasets; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY metasets (id, obj_version, content, type_id) FROM stdin;
\.


--
-- Data for Name: objects; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY objects (id, version, acl_id, appname, cmn_version, content_path, content_size, created, creator_id, format_id, language_id, latest_branch, latest_head, locker_id, metadata, modified, modifier_id, name, owner_id, parent_id, predecessor_id, procstate, root_id, state_id, type_id) FROM stdin;
\.


--
-- Data for Name: objtypes; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY objtypes (id, obj_version, config, name) FROM stdin;
14	0	<meta/>	_render_task
15	0	<meta/>	_config
16	0	<meta/>	document
17	0	<meta/>	_task
18	0	<meta/>	_default_objtype
19	0	<meta/>	_search
20	0	<meta/>	_supporting_document
21	0	<meta/>	_workflow_template
22	0	<meta/>	_object_reference
23	0	<meta/>	_task_definition
24	0	<meta/>	image
25	0	<meta/>	_notification
26	0	<meta/>	_cart
27	0	<meta/>	_translation_task
28	0	<meta/>	_workflow
29	0	<meta/>	_rendition
\.


--
-- Data for Name: osd_metasets; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY osd_metasets (id, obj_version, metaset_id, osd_id) FROM stdin;
\.


--
-- Data for Name: permissions; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY permissions (id, obj_version, name) FROM stdin;
46	0	_write_object_content
47	0	_read_object_content
48	0	_write_object_custom_metadata
49	0	_read_object_custom_metadata
50	0	_write_object_sysmeta
51	0	_read_object_sysmeta
52	0	_version
53	0	_delete_object
54	0	_browse
55	0	_lock
56	0	_create_folder
57	0	_delete_folder
58	0	_browse_folder
59	0	_edit_folder
60	0	_create_inside_folder
61	0	_move
62	0	_set_acl
63	0	_query_custom_table
64	0	_create_instance
\.


--
-- Data for Name: relation_resolvers; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY relation_resolvers (id, obj_version, config, name, class_name) FROM stdin;
65	0	<meta/>	FixedRelationResolver	cinnamon.relation.resolver.FixedRelationResolver
76	0	<meta/>	LatestHeadResolver	cinnamon.relation.resolver.LatestHeadResolver
77	0	<meta/>	LatestBranchResolver	cinnamon.relation.resolver.LatestBranchResolver
\.


--
-- Data for Name: relations; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY relations (id, obj_version, left_id, metadata, right_id, type_id) FROM stdin;
\.


--
-- Data for Name: relationtypes; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY relationtypes (id, obj_version, clone_on_left_copy, clone_on_left_version, clone_on_right_copy, clone_on_right_version, left_resolver_id, leftobjectprotected, name, right_resolver_id, rightobjectprotected) FROM stdin;
66	0	f	f	f	f	65	t	_workflow_task	65	t
67	0	f	f	f	f	65	f	translation_source_list	65	t
68	0	f	f	f	f	65	t	translation_source	65	f
69	0	f	f	f	f	65	t	_workflow_deadline_task	65	t
70	0	f	f	f	f	65	t	translation_root	65	f
71	0	t	f	f	f	65	f	child_content	65	t
72	0	f	f	f	f	65	t	rendition	65	f
73	0	f	f	f	f	65	f	translation_target_list	65	t
74	0	f	f	f	f	65	t	_workflow_start_task	65	t
75	0	t	f	f	f	65	f	child_no_content	65	t
\.


--
-- Data for Name: sessions; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY sessions (id, obj_version, expires, ui_language_id, lifetime, machinename, message, ticket, user_id, username) FROM stdin;
\.


--
-- Data for Name: transformers; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY transformers (id, obj_version, name, source_format_id, target_format_id, transformer_class) FROM stdin;
\.


--
-- Data for Name: ui_languages; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY ui_languages (id, obj_version, iso_code) FROM stdin;
91	0	zxx
92	0	mul
93	0	und
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: cinnamon
--

COPY users (id, obj_version, account_expired, account_locked, activated, description, email, fullname, language_id, name, password_expired, pwd, sudoable, sudoer, token, token_age, tokens_today) FROM stdin;
36	3	f	f	t	Cinnamon Administrator	\N	Administrator	\N	admin	f	admin	f	t	1cce300f-5643-4b6c-8944-3aa901c7aaf9	1970-01-01 01:00:00	0
\.


--
-- Name: aclentries_group_id_acl_id_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY aclentries
    ADD CONSTRAINT aclentries_group_id_acl_id_key UNIQUE (group_id, acl_id);


--
-- Name: aclentries_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY aclentries
    ADD CONSTRAINT aclentries_pkey PRIMARY KEY (id);


--
-- Name: aclentry_permissions_permission_id_aclentry_id_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY aclentry_permissions
    ADD CONSTRAINT aclentry_permissions_permission_id_aclentry_id_key UNIQUE (permission_id, aclentry_id);


--
-- Name: aclentry_permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY aclentry_permissions
    ADD CONSTRAINT aclentry_permissions_pkey PRIMARY KEY (id);


--
-- Name: acls_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY acls
    ADD CONSTRAINT acls_name_key UNIQUE (name);


--
-- Name: acls_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY acls
    ADD CONSTRAINT acls_pkey PRIMARY KEY (id);


--
-- Name: change_trigger_types_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY change_trigger_types
    ADD CONSTRAINT change_trigger_types_name_key UNIQUE (name);


--
-- Name: change_trigger_types_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY change_trigger_types
    ADD CONSTRAINT change_trigger_types_pkey PRIMARY KEY (id);


--
-- Name: change_triggers_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY change_triggers
    ADD CONSTRAINT change_triggers_pkey PRIMARY KEY (id);


--
-- Name: config_entries_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY config_entries
    ADD CONSTRAINT config_entries_name_key UNIQUE (name);


--
-- Name: config_entries_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY config_entries
    ADD CONSTRAINT config_entries_pkey PRIMARY KEY (id);


--
-- Name: customtables_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY customtables
    ADD CONSTRAINT customtables_name_key UNIQUE (name);


--
-- Name: customtables_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY customtables
    ADD CONSTRAINT customtables_pkey PRIMARY KEY (id);


--
-- Name: folder_metasets_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY folder_metasets
    ADD CONSTRAINT folder_metasets_pkey PRIMARY KEY (id);


--
-- Name: folder_types_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY folder_types
    ADD CONSTRAINT folder_types_name_key UNIQUE (name);


--
-- Name: folder_types_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY folder_types
    ADD CONSTRAINT folder_types_pkey PRIMARY KEY (id);


--
-- Name: folders_parent_id_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY folders
    ADD CONSTRAINT folders_parent_id_name_key UNIQUE (parent_id, name);


--
-- Name: folders_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY folders
    ADD CONSTRAINT folders_pkey PRIMARY KEY (id);


--
-- Name: formats_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY formats
    ADD CONSTRAINT formats_name_key UNIQUE (name);


--
-- Name: formats_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY formats
    ADD CONSTRAINT formats_pkey PRIMARY KEY (id);


--
-- Name: group_users_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY group_users
    ADD CONSTRAINT group_users_pkey PRIMARY KEY (id);


--
-- Name: group_users_user_id_group_id_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY group_users
    ADD CONSTRAINT group_users_user_id_group_id_key UNIQUE (user_id, group_id);


--
-- Name: groups_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY groups
    ADD CONSTRAINT groups_name_key UNIQUE (name);


--
-- Name: groups_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY groups
    ADD CONSTRAINT groups_pkey PRIMARY KEY (id);


--
-- Name: index_groups_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY index_groups
    ADD CONSTRAINT index_groups_name_key UNIQUE (name);


--
-- Name: index_groups_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY index_groups
    ADD CONSTRAINT index_groups_pkey PRIMARY KEY (id);


--
-- Name: index_items_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY index_items
    ADD CONSTRAINT index_items_name_key UNIQUE (name);


--
-- Name: index_items_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY index_items
    ADD CONSTRAINT index_items_pkey PRIMARY KEY (id);


--
-- Name: index_jobs_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY index_jobs
    ADD CONSTRAINT index_jobs_pkey PRIMARY KEY (id);


--
-- Name: index_types_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY index_types
    ADD CONSTRAINT index_types_name_key UNIQUE (name);


--
-- Name: index_types_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY index_types
    ADD CONSTRAINT index_types_pkey PRIMARY KEY (id);


--
-- Name: languages_iso_code_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY languages
    ADD CONSTRAINT languages_iso_code_key UNIQUE (iso_code);


--
-- Name: languages_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY languages
    ADD CONSTRAINT languages_pkey PRIMARY KEY (id);


--
-- Name: lifecycle_log_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY lifecycle_log
    ADD CONSTRAINT lifecycle_log_pkey PRIMARY KEY (id);


--
-- Name: lifecycle_states_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY lifecycle_states
    ADD CONSTRAINT lifecycle_states_pkey PRIMARY KEY (id);


--
-- Name: lifecycles_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY lifecycles
    ADD CONSTRAINT lifecycles_name_key UNIQUE (name);


--
-- Name: lifecycles_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY lifecycles
    ADD CONSTRAINT lifecycles_pkey PRIMARY KEY (id);


--
-- Name: links_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY links
    ADD CONSTRAINT links_pkey PRIMARY KEY (id);


--
-- Name: messages_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- Name: messages_ui_language_id_message_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY messages
    ADD CONSTRAINT messages_ui_language_id_message_key UNIQUE (ui_language_id, message);


--
-- Name: metaset_types_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY metaset_types
    ADD CONSTRAINT metaset_types_name_key UNIQUE (name);


--
-- Name: metaset_types_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY metaset_types
    ADD CONSTRAINT metaset_types_pkey PRIMARY KEY (id);


--
-- Name: metasets_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY metasets
    ADD CONSTRAINT metasets_pkey PRIMARY KEY (id);


--
-- Name: objects_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY objects
    ADD CONSTRAINT objects_pkey PRIMARY KEY (id);


--
-- Name: objtypes_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY objtypes
    ADD CONSTRAINT objtypes_name_key UNIQUE (name);


--
-- Name: objtypes_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY objtypes
    ADD CONSTRAINT objtypes_pkey PRIMARY KEY (id);


--
-- Name: osd_metasets_metaset_id_osd_id_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY osd_metasets
    ADD CONSTRAINT osd_metasets_metaset_id_osd_id_key UNIQUE (metaset_id, osd_id);


--
-- Name: osd_metasets_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY osd_metasets
    ADD CONSTRAINT osd_metasets_pkey PRIMARY KEY (id);


--
-- Name: permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY permissions
    ADD CONSTRAINT permissions_pkey PRIMARY KEY (id);


--
-- Name: relation_resolvers_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY relation_resolvers
    ADD CONSTRAINT relation_resolvers_name_key UNIQUE (name);


--
-- Name: relation_resolvers_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY relation_resolvers
    ADD CONSTRAINT relation_resolvers_pkey PRIMARY KEY (id);


--
-- Name: relations_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY relations
    ADD CONSTRAINT relations_pkey PRIMARY KEY (id);


--
-- Name: relations_type_id_right_id_left_id_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY relations
    ADD CONSTRAINT relations_type_id_right_id_left_id_key UNIQUE (type_id, right_id, left_id);


--
-- Name: relationtypes_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY relationtypes
    ADD CONSTRAINT relationtypes_name_key UNIQUE (name);


--
-- Name: relationtypes_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY relationtypes
    ADD CONSTRAINT relationtypes_pkey PRIMARY KEY (id);


--
-- Name: sessions_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY sessions
    ADD CONSTRAINT sessions_pkey PRIMARY KEY (id);


--
-- Name: transformers_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY transformers
    ADD CONSTRAINT transformers_name_key UNIQUE (name);


--
-- Name: transformers_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY transformers
    ADD CONSTRAINT transformers_pkey PRIMARY KEY (id);


--
-- Name: ui_languages_iso_code_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY ui_languages
    ADD CONSTRAINT ui_languages_iso_code_key UNIQUE (iso_code);


--
-- Name: ui_languages_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY ui_languages
    ADD CONSTRAINT ui_languages_pkey PRIMARY KEY (id);


--
-- Name: users_name_key; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_name_key UNIQUE (name);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: cinnamon; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: fk1c403bfd9c212348; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY relationtypes
    ADD CONSTRAINT fk1c403bfd9c212348 FOREIGN KEY (right_resolver_id) REFERENCES relation_resolvers(id);


--
-- Name: fk1c403bfdddbf78b3; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY relationtypes
    ADD CONSTRAINT fk1c403bfdddbf78b3 FOREIGN KEY (left_resolver_id) REFERENCES relation_resolvers(id);


--
-- Name: fk2110aced72d2a8d7; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY aclentry_permissions
    ADD CONSTRAINT fk2110aced72d2a8d7 FOREIGN KEY (permission_id) REFERENCES permissions(id);


--
-- Name: fk2110acedb6096957; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY aclentry_permissions
    ADD CONSTRAINT fk2110acedb6096957 FOREIGN KEY (aclentry_id) REFERENCES aclentries(id);


--
-- Name: fk41cacc4898b2cc14; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY group_users
    ADD CONSTRAINT fk41cacc4898b2cc14 FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: fk41cacc48c55d9f33; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY group_users
    ADD CONSTRAINT fk41cacc48c55d9f33 FOREIGN KEY (group_id) REFERENCES groups(id);


--
-- Name: fk4e59b116989729d; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY customtables
    ADD CONSTRAINT fk4e59b116989729d FOREIGN KEY (acl_id) REFERENCES acls(id);


--
-- Name: fk53bfd09d620ef2f6; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY sessions
    ADD CONSTRAINT fk53bfd09d620ef2f6 FOREIGN KEY (ui_language_id) REFERENCES ui_languages(id);


--
-- Name: fk53bfd09d98b2cc14; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY sessions
    ADD CONSTRAINT fk53bfd09d98b2cc14 FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: fk5a6c3cc6989729d; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY aclentries
    ADD CONSTRAINT fk5a6c3cc6989729d FOREIGN KEY (acl_id) REFERENCES acls(id);


--
-- Name: fk5a6c3cc6c55d9f33; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY aclentries
    ADD CONSTRAINT fk5a6c3cc6c55d9f33 FOREIGN KEY (group_id) REFERENCES groups(id);


--
-- Name: fk6234fb925f0b18f; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY links
    ADD CONSTRAINT fk6234fb925f0b18f FOREIGN KEY (osd_id) REFERENCES objects(id);


--
-- Name: fk6234fb9355513b; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY links
    ADD CONSTRAINT fk6234fb9355513b FOREIGN KEY (parent_id) REFERENCES folders(id);


--
-- Name: fk6234fb94997c2c; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY links
    ADD CONSTRAINT fk6234fb94997c2c FOREIGN KEY (owner_id) REFERENCES users(id);


--
-- Name: fk6234fb9989729d; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY links
    ADD CONSTRAINT fk6234fb9989729d FOREIGN KEY (acl_id) REFERENCES acls(id);


--
-- Name: fk6234fb9a75f76f7; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY links
    ADD CONSTRAINT fk6234fb9a75f76f7 FOREIGN KEY (folder_id) REFERENCES folders(id);


--
-- Name: fk63f40e1376e75f86; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY index_items
    ADD CONSTRAINT fk63f40e1376e75f86 FOREIGN KEY (index_type_id) REFERENCES index_types(id);


--
-- Name: fk63f40e13abc37ece; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY index_items
    ADD CONSTRAINT fk63f40e13abc37ece FOREIGN KEY (index_group_id) REFERENCES index_groups(id);


--
-- Name: fk6a68e087907c421; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY users
    ADD CONSTRAINT fk6a68e087907c421 FOREIGN KEY (language_id) REFERENCES ui_languages(id);


--
-- Name: fk900cbe3525f0b18f; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY osd_metasets
    ADD CONSTRAINT fk900cbe3525f0b18f FOREIGN KEY (osd_id) REFERENCES objects(id);


--
-- Name: fk900cbe35411ca83d; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY osd_metasets
    ADD CONSTRAINT fk900cbe35411ca83d FOREIGN KEY (metaset_id) REFERENCES metasets(id);


--
-- Name: fk9158567ae7a1b33; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY transformers
    ADD CONSTRAINT fk9158567ae7a1b33 FOREIGN KEY (source_format_id) REFERENCES formats(id);


--
-- Name: fk9158567ae99b4929; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY transformers
    ADD CONSTRAINT fk9158567ae99b4929 FOREIGN KEY (target_format_id) REFERENCES formats(id);


--
-- Name: fk9d13c514355513b; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY objects
    ADD CONSTRAINT fk9d13c514355513b FOREIGN KEY (parent_id) REFERENCES folders(id);


--
-- Name: fk9d13c5144573c988; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY objects
    ADD CONSTRAINT fk9d13c5144573c988 FOREIGN KEY (modifier_id) REFERENCES users(id);


--
-- Name: fk9d13c5144997c2c; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY objects
    ADD CONSTRAINT fk9d13c5144997c2c FOREIGN KEY (owner_id) REFERENCES users(id);


--
-- Name: fk9d13c5146a5d25a7; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY objects
    ADD CONSTRAINT fk9d13c5146a5d25a7 FOREIGN KEY (locker_id) REFERENCES users(id);


--
-- Name: fk9d13c514989729d; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY objects
    ADD CONSTRAINT fk9d13c514989729d FOREIGN KEY (acl_id) REFERENCES acls(id);


--
-- Name: fk9d13c514b445fc6d; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY objects
    ADD CONSTRAINT fk9d13c514b445fc6d FOREIGN KEY (root_id) REFERENCES objects(id);


--
-- Name: fk9d13c514c366362d; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY objects
    ADD CONSTRAINT fk9d13c514c366362d FOREIGN KEY (language_id) REFERENCES languages(id);


--
-- Name: fk9d13c514dd1320e6; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY objects
    ADD CONSTRAINT fk9d13c514dd1320e6 FOREIGN KEY (predecessor_id) REFERENCES objects(id);


--
-- Name: fk9d13c514ddca28b1; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY objects
    ADD CONSTRAINT fk9d13c514ddca28b1 FOREIGN KEY (state_id) REFERENCES lifecycle_states(id);


--
-- Name: fk9d13c514f3aed013; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY objects
    ADD CONSTRAINT fk9d13c514f3aed013 FOREIGN KEY (creator_id) REFERENCES users(id);


--
-- Name: fk9d13c514f3c30cd6; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY objects
    ADD CONSTRAINT fk9d13c514f3c30cd6 FOREIGN KEY (type_id) REFERENCES objtypes(id);


--
-- Name: fk9d13c514f3ebde57; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY objects
    ADD CONSTRAINT fk9d13c514f3ebde57 FOREIGN KEY (format_id) REFERENCES formats(id);


--
-- Name: fkb63dd9d42295d928; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY groups
    ADD CONSTRAINT fkb63dd9d42295d928 FOREIGN KEY (parent_id) REFERENCES groups(id);


--
-- Name: fkceca8c87411ca83d; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY folder_metasets
    ADD CONSTRAINT fkceca8c87411ca83d FOREIGN KEY (metaset_id) REFERENCES metasets(id);


--
-- Name: fkceca8c87a75f76f7; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY folder_metasets
    ADD CONSTRAINT fkceca8c87a75f76f7 FOREIGN KEY (folder_id) REFERENCES folders(id);


--
-- Name: fkd1620649d7b1fcef; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY lifecycles
    ADD CONSTRAINT fkd1620649d7b1fcef FOREIGN KEY (default_state_id) REFERENCES lifecycle_states(id);


--
-- Name: fkd74671c5355513b; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY folders
    ADD CONSTRAINT fkd74671c5355513b FOREIGN KEY (parent_id) REFERENCES folders(id);


--
-- Name: fkd74671c54997c2c; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY folders
    ADD CONSTRAINT fkd74671c54997c2c FOREIGN KEY (owner_id) REFERENCES users(id);


--
-- Name: fkd74671c582d15685; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY folders
    ADD CONSTRAINT fkd74671c582d15685 FOREIGN KEY (type_id) REFERENCES folder_types(id);


--
-- Name: fkd74671c5989729d; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY folders
    ADD CONSTRAINT fkd74671c5989729d FOREIGN KEY (acl_id) REFERENCES acls(id);


--
-- Name: fkd79f075c7c52d594; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY formats
    ADD CONSTRAINT fkd79f075c7c52d594 FOREIGN KEY (default_object_type_id) REFERENCES objtypes(id);


--
-- Name: fkd7e8a56adf492399; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY change_triggers
    ADD CONSTRAINT fkd7e8a56adf492399 FOREIGN KEY (change_trigger_type_id) REFERENCES change_trigger_types(id);


--
-- Name: fke3bd987792a571e0; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY lifecycle_states
    ADD CONSTRAINT fke3bd987792a571e0 FOREIGN KEY (life_cycle_id) REFERENCES lifecycles(id);


--
-- Name: fke3bd9877dc8aa3cd; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY lifecycle_states
    ADD CONSTRAINT fke3bd9877dc8aa3cd FOREIGN KEY (life_cycle_state_for_copy_id) REFERENCES lifecycle_states(id);


--
-- Name: fke475014c620ef2f6; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY messages
    ADD CONSTRAINT fke475014c620ef2f6 FOREIGN KEY (ui_language_id) REFERENCES ui_languages(id);


--
-- Name: fke5345bd68ec81b7a; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY metasets
    ADD CONSTRAINT fke5345bd68ec81b7a FOREIGN KEY (type_id) REFERENCES metaset_types(id);


--
-- Name: fkff8b45f765514f28; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY relations
    ADD CONSTRAINT fkff8b45f765514f28 FOREIGN KEY (left_id) REFERENCES objects(id);


--
-- Name: fkff8b45f7934e30eb; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY relations
    ADD CONSTRAINT fkff8b45f7934e30eb FOREIGN KEY (type_id) REFERENCES relationtypes(id);


--
-- Name: fkff8b45f7c6a78f3; Type: FK CONSTRAINT; Schema: public; Owner: cinnamon
--

ALTER TABLE ONLY relations
    ADD CONSTRAINT fkff8b45f7c6a78f3 FOREIGN KEY (right_id) REFERENCES objects(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- Name: lifecycle_log_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON SEQUENCE lifecycle_log_id_seq FROM PUBLIC;
REVOKE ALL ON SEQUENCE lifecycle_log_id_seq FROM postgres;
GRANT ALL ON SEQUENCE lifecycle_log_id_seq TO postgres;


--
-- Name: lifecycle_log; Type: ACL; Schema: public; Owner: cinnamon
--

REVOKE ALL ON TABLE lifecycle_log FROM PUBLIC;
REVOKE ALL ON TABLE lifecycle_log FROM cinnamon;
GRANT ALL ON TABLE lifecycle_log TO cinnamon;
GRANT ALL ON TABLE lifecycle_log TO postgres;


--
-- PostgreSQL database dump complete
--


-- Table: public.contracts

ALTER DATABASE table_data_gateway;

DROP TABLE IF EXISTS public.people;

DROP SEQUENCE IF EXISTS people_id_seq;

CREATE SEQUENCE people_id_seq;

CREATE TABLE public.people
(
    person_id integer NOT NULL DEFAULT nextval('people_id_seq'),
    first_name character varying(100) NOT NULL,
    second_name character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    CONSTRAINT people_pkey PRIMARY KEY (person_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.people
    OWNER to table_data_gateway;
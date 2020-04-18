-- Table: public.contracts

ALTER DATABASE active_record;

DROP TABLE IF EXISTS public.persons;

DROP SEQUENCE IF EXISTS persons_id_seq;

CREATE SEQUENCE persons_id_seq;

CREATE TABLE public.persons
(
    person_id integer NOT NULL DEFAULT nextval('persons_id_seq'),
    first_name character varying(100) NOT NULL,
    second_name character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    CONSTRAINT contracts_pkey PRIMARY KEY (person_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.persons
    OWNER to active_record;
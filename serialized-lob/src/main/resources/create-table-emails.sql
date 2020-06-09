-- Table: public.emails

ALTER DATABASE serialized_lob;

DROP TABLE IF EXISTS public.emails;

DROP SEQUENCE IF EXISTS emails_id_seq;

CREATE SEQUENCE emails_id_seq;

CREATE TABLE public.emails
(
    email_id integer NOT NULL DEFAULT nextval('emails_id_seq'),
    email character varying(100) NOT NULL,
    CONSTRAINT emails_pkey PRIMARY KEY (email_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.emails
    OWNER to serialized_lob;
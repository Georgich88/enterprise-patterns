-- Table: public.customers

ALTER DATABASE serialized_lob;

DROP TABLE IF EXISTS public.customers;

DROP TABLE IF EXISTS public.emails;

DROP SEQUENCE IF EXISTS customers_id_seq;

DROP SEQUENCE IF EXISTS emails_id_seq;

CREATE SEQUENCE customers_id_seq;

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

CREATE TABLE public.customers
(
    customer_id integer NOT NULL DEFAULT nextval('customers_id_seq'::regclass),
    first_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    second_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    email_id integer,
    department xml,
    CONSTRAINT contracts_pkey PRIMARY KEY (customer_id),
    CONSTRAINT emails_pkey FOREIGN KEY (email_id)
        REFERENCES public.emails (email_id) MATCH SIMPLE
        ON UPDATE SET NULL
        ON DELETE SET NULL
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.customers
    OWNER to serialized_lob;
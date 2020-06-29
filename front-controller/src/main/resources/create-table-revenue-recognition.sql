-- Table: public.students

ALTER DATABASE transaction_script;

DROP TABLE IF EXISTS public.revenue_recognition;

DROP SEQUENCE IF EXISTS revenue_recognition_id_seq;

CREATE SEQUENCE revenue_recognition_id_seq;

CREATE TABLE public.revenue_recognition
(
    revenue_recognition_id integer NOT NULL DEFAULT nextval('revenue_recognition_id_seq'),
    contract_id integer,
    date_recognition date NOT NULL,
    amount decimal NOT NULL,
    CONSTRAINT revenue_recognition_pkey PRIMARY KEY (revenue_recognition_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.revenue_recognition
    OWNER to transaction_script;
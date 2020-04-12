-- Table: public.students

ALTER DATABASE transaction_script;

DROP TABLE IF EXISTS public.revenue_recognition;

DROP SEQUENCE IF EXISTS revenue_recognition_id_seq;

CREATE SEQUENCE revenue_recognition_id_seq;

CREATE TABLE public.students
(
    revenue_recognition_id integer NOT NULL DEFAULT nextval('students_id_seq'),
    contract_id integer,
    date_recognition date NOT NULL,
    amount decimal NOT NULL,
    CONSTRAINT students_pkey PRIMARY KEY (student_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.students
    OWNER to transaction_script;
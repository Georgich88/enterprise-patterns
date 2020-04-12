-- Table: public.contracts

ALTER DATABASE transaction_script;

DROP TABLE IF EXISTS public.contracts;

CREATE TABLE public.contracts
(
    contract_id integer NOT NULL,
    contract_name character varying(100) NOT NULL,
    contract_description character varying(250) NOT NULL,
    product_id integer NOT NULL,
    revenue decimal NOT NULL,
    CONSTRAINT contracts_pkey PRIMARY KEY (contract_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.contracts
    OWNER to transaction_script;
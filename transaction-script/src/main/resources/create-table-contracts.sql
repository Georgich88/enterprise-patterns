-- Table: public.contracts

ALTER DATABASE transaction_script;

DROP TABLE IF EXISTS public.contracts;

CREATE TABLE public.contracts
(
    contract_id integer NOT NULL,
    product_id integer NOT NULL,
    date_signed date NOT NULL,
    revenue decimal NOT NULL,
    CONSTRAINT contracts_pkey PRIMARY KEY (contract_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.contracts
    OWNER to transaction_script;
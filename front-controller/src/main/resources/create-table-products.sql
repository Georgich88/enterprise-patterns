-- Table: public.products

ALTER DATABASE transaction_script;

DROP TABLE IF EXISTS public.products;

CREATE TABLE public.products
(
    product_id integer NOT NULL,
    name character varying(100) NOT NULL,
    type character varying(100) NOT NULL,
    CONSTRAINT products_pkey PRIMARY KEY (product_id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.products
    OWNER to transaction_script;
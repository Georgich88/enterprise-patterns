-- Role: table_data_gateway
DO
$do$
BEGIN
   IF NOT EXISTS (
      SELECT
      FROM   pg_catalog.pg_roles
      WHERE  rolname = 'table_data_gateway') THEN
      CREATE ROLE courses_admin WITH
		LOGIN
		NOSUPERUSER
		CREATEDB
		NOCREATEROLE
		INHERIT
		NOREPLICATION
		CONNECTION LIMIT -1
		PASSWORD '123456';
   END IF;
END
$do$;
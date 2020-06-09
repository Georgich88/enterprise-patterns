-- Role: serialized_lob
DO
$do$
BEGIN
   IF NOT EXISTS (
      SELECT
      FROM   pg_catalog.pg_roles
      WHERE  rolname = 'serialized_lob') THEN
      CREATE ROLE data_mapper WITH
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
DROP TABLE IF EXISTS "user_roles";
DROP TABLE IF EXISTS "roles";
DROP TABLE IF EXISTS "users";
DROP SEQUENCE IF EXISTS "user_id_sequence";
DROP SEQUENCE IF EXISTS "role_id_sequence";
CREATE SEQUENCE "user_id_sequence" START 1000 INCREMENT BY 1;
CREATE SEQUENCE "role_id_sequence" START 1000 INCREMENT BY 1;
CREATE TABLE "roles"
(
   "id" BIGINT DEFAULT NEXTVAL('role_id_sequence') PRIMARY KEY,
   "name" VARCHAR (30) NOT NULL
);
CREATE TABLE "users"
(
   "id" BIGINT DEFAULT NEXTVAL('user_id_sequence') PRIMARY KEY,
   "email" VARCHAR (50),
   "password" VARCHAR (120),
   "username" VARCHAR (20)
);
ALTER TABLE "users" ADD UNIQUE ("username");
ALTER TABLE "users" ADD UNIQUE ("email");
CREATE TABLE "user_roles"
(
   "user_id" BIGINT NOT NULL,
   "role_id" BIGINT NOT NULL
);
ALTER TABLE "user_roles" ADD PRIMARY KEY
(
   "user_id",
   "role_id"
);
ALTER TABLE "user_roles" ADD FOREIGN KEY ("role_id") REFERENCES "roles" ("id");
ALTER TABLE "user_roles" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("id");
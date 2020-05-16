DROP TABLE IF EXISTS "book";
DROP SEQUENCE IF EXISTS book_id_sequence;
CREATE SEQUENCE book_id_sequence START WITH 1000 INCREMENT BY 1;
CREATE TABLE "book"
(
   "id" BIGINT DEFAULT NEXTVAL('book_id_sequence') PRIMARY KEY,
   "author" VARCHAR (30),
   "isbn" VARCHAR (13) NOT NULL,
   "name" VARCHAR (30) NOT NULL
);
ALTER TABLE "book" ADD UNIQUE ("isbn");
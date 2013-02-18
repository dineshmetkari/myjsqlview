-- ============================================================
--    Test tables that represent a sample of various key
-- combinations.
-- =============================================================
-- Version 02/02/2013 Original key_tables Database tables.
--      
-- danap@dandymadeproductions.com
-- =============================================================

--
-- Table structure for table 'key_table1"
--

-- DROP TABLE "key_table1";
CREATE TABLE "key_table1" (
  "key_id1" int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  "key_id2" int NOT NULL WITH DEFAULT 0,
  "text" long varchar,
  PRIMARY KEY  ("key_id1", "key_id2")
);

--
-- Table structure for table 'keY_tAble2'
--

-- DROP TABLE "keY_tAble2";
CREATE TABLE "keY_tAble2" (
  "Host" char(60) NOT NULL WITH DEFAULT '',
  "Db" char(64) NOT NULL WITH DEFAULT '',
  "User" char(16) NOT NULL WITH DEFAULT '',
  "Select_priv" boolean NOT NULL WITH DEFAULT true,
  PRIMARY KEY  ("Host","Db","User")
);

--
-- Table structure for table "key_table3'
-- Blob Keys Not Supported.

-- DROP TABLE "key_table3";
CREATE TABLE "key_table3" (
  "blob_col" blob(200K),
  PRIMARY KEY ("blob_col")
);

--
-- Table structure for table 'key_table4'
--

-- DROP TABLE "key_table4";
CREATE TABLE "key_table4" (
  "avatar_id" int NOT NULL WITH DEFAULT 0,
  "user_id" int NOT NULL WITH DEFAULT 0,
  "bing_id" int NOT NULL WITH DEFAULT 0,
  PRIMARY KEY ("avatar_id","user_id")
);

--
-- Table structure for table 'key_table5'
--

-- DROP TABLE "key_table5";
CREATE TABLE "key_table5" (
  "name" varchar(30) NOT NULL WITH DEFAULT '',
  "color" varchar(10) WITH DEFAULT NULL,
  "price" float(6) WITH DEFAULT NULL,
  UNIQUE ("name"),
  PRIMARY KEY ("color")
);

--
-- Table structure for table 'key_table6'
--

-- DROP TABLE "key_table6";
CREATE TABLE "key_table6" (
  "image_id" int NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
  "name" varchar(30) WITH DEFAULT NULL,
  "blob_field1" blob(50K),
  "blob_field2" blob(1M),
  PRIMARY KEY  ("image_id")
);

--
-- Table structure for table 'key_table7'
-- Long Varchar or Clob Can not be indexed.

-- DROP TABLE "key_table7";
CREATE TABLE "key_table7" (
  "text_id" long varchar NOT NULL,
  "name" varchar(30) WITH DEFAULT NULL,
  "blob_field2" blob(1M),
  PRIMARY KEY ("text_id")
);

--
-- Table structure for table 'key_table8'
--

-- DROP TABLE "key_table8";
CREATE TABLE "key_table8" (
  "date_id" DATE NOT NULL,
  "name" varchar(30) WITH DEFAULT NULL,
  PRIMARY KEY ("date_id")
);

--
-- Table structure for table 'parent', 'child'
--

-- DROP TABLE "child";
-- DROP TABLE "parent";

CREATE TABLE "parent" (
  "id" int,
  "name" varchar(60) WITH DEFAULT '',
  PRIMARY KEY ("id")
);


CREATE TABLE "child" (
  "parent_id" int,
  "name" varchar(60) WITH DEFAULT '',
  FOREIGN KEY ("parent_id") REFERENCES "parent"("id") ON DELETE CASCADE
);

--
-- View for table5
--

-- DROP VIEW "key_table5_View";
CREATE VIEW "key_table5_View" AS SELECT * FROM "key_table5";
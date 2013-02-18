-- =============================================================
-- Test table to create the various data types that are
-- defined by the Apache Derby database.
-- =============================================================
-- Dana Proctor 
-- Version 02/02/2013 Original Test Data Type Table.
--      
-- danap@dandymadeproductions.com
-- =============================================================

-- Note you may have to comment the DROP TABLE statement
-- if it is not the table does not exist. Otherwise Derby
-- will throw an error in TABLE NOT FOUND.

DROP TABLE "derbytypes";
CREATE TABLE "derbytypes" (

--  Table id and creation data entries.
    "data_type_id" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),

--  Character, text, and blob type fields.

    "char_type" CHAR(30) DEFAULT NULL,
    "char_bit_type" CHAR(30) FOR BIT DATA DEFAULT NULL,
    "varchar_type" VARCHAR(30) DEFAULT NULL,
    "varchar_bit_type" VARCHAR(30) FOR BIT DATA DEFAULT NULL,
    "blob_type" BLOB(1M) DEFAULT NULL,
    "longvarchar_type" LONG VARCHAR DEFAULT NULL,
    "longvarchar_bit_type" LONG VARCHAR FOR BIT DATA DEFAULT NULL,
    "clob_type" CLOB(1M) DEFAULT NULL,

--  Numeric fields.

    "boolean_type" BOOLEAN DEFAULT NULL,
    "smallInt_type" SMALLINT DEFAULT NULL,
    "int_type" INT DEFAULT NULL,
    "bigInt_type" BIGINT DEFAULT NULL,
    "float_type" FLOAT DEFAULT NULL,
    "real_type" REAL DEFAULT NULL,
    "double_type" DOUBLE DEFAULT NULL,
    "decimal_type" DECIMAL(16,2) DEFAULT NULL,  
    
--  Date and time fields.

    "date_type" DATE DEFAULT NULL,
    "time_type" TIME DEFAULT NULL,
    "timeStamp_type" TIMESTAMP,
    PRIMARY KEY (data_type_id)
);

--
-- View for derbytypes
--

DROP VIEW "myView";
CREATE VIEW "myView" AS SELECT * FROM "derbytypes";
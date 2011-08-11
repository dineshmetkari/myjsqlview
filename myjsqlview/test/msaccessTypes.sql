-- =============================================================
-- Test table to create the various data types that are
-- defined by the MS Access 97, Jet SQL, database.
-- =============================================================
-- Dana Proctor 
-- Version 06/02/2011 Orignal Test Data Type Table.
--      
-- danap@dandymadeproductions.com
-- =============================================================

DROP TABLE IF EXISTS msaccesstypes;
CREATE TABLE msaccesstypes (

--  Table id and creation data entries.

    data_type_id COUNTER PRIMARY KEY,

--  Character, text, and blob type fields.

    binary_type BINARY DEFAULT NULL,
    longbinary_type LONGBINARY DEFAULT NULL,
    text_type TEXT DEFAULT NULL,
    longText_type LONGTEXT DEFAULT NULL,

--  Numeric fields.

    bit_type BIT DEFAULT NULL,
    byte_type BYTE DEFAULT NULL,
    short_type SHORT DEFAULT NULL,
    long_type LONG DEFAULT NULL,
    single_type SINGLE DEFAULT NULL,
    double_type DOUBLE DEFAULT NULL,
    currency_type CURRENCY DEFAULT NULL,
    guid_type GUID DEFAULT NUll,
    
--  Date and time fields.
   
    dateTime_type DATETIME DEFAULT NULL
);
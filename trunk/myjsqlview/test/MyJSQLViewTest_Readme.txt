			              MyJSQLView Test Outline
			
   The basic requirements for performing these various tests involve the
setting up of two databases for each supported database application, ie,
MySQL, PostgreSQL, HSQL, Oracle etc. The two databases for the purpose of
this outline shall be called key_tables and datatypes. To be more helpful
in differentiation in the MyJSQLView connection Manager it might be useful
to prefix the database with the database application name. Example:
mysql_key_tables and mysql_datatypes. Of course any existing database
could be used, and then just set up the needed tables for the testing. The
installation directory for MyJSQLView holds a test directory which holds
this test outline file and contains all the script SQL files that may be
used to create the databases tables used during the test phase. To outline
as an example for setup of database tables prior to testing with MySQL:

Create databases key_tables and datatypes.
Load the script mysql_keyTables.sql > into the key_tables database.
Load the script mysql_loadTest.sql > into the datatypes database.
Load the script mysqlTypes.sql > into the datatypes database.

Though the above test database tables provide the data configuration for
testing MyJSQLView this does not address the various platforms that may
be used to run the application. Therefore the tests should be performed on
various platforms to insure any peculiarties are discovered. 

Note: Insure for each test case that involves a menu command that the 
      corresponding toolbar command also executes the action.

1. Database Testing (Each Supported Database Application)
 
	a. Test add, edit, delete, sort, search, advanced sort/search, update field
	   rows, and functions in the test database key_tables for each of the seven
	   key type tables. It is helpful for this test to review the appropriate
	   database key_tables SQL file to understand the purpose for each of
	   the test tables. Insure that each supported action is also properly
	   executed from the popup menu in the Summary Table.

	b. Perform a data types load test with test database datatypes table
	   xxxxtypes. Where xxxx stands for the associated database application,
	   ie. mysql, hsql.

	c. Test add, edit, sort, search, advanced sort/search, update field rows,
	   and functions in the test database datatypes table xxxxtypes. Where xxxx
	   stands for the associated database application, ie. mysql, hsql.

2. Table Summary Printing

	a. Test printing of basic summary table data.
	
	b. Test page formatting, 

3. Cut, Copy, and Pasting Data

	a. Summary Table copying and pasting into external editor.
	
	b. Summary Table paste, CSV Import, from external editor.

	c. Edit/View Forms Cutting, Copying, and Pasting.
	
4. Test Query Table Tool (Each Supported Database Application)

	a. Execute SELECT * FROM xxxxtypes. Where xxxx stands for the assocaited
	   database application, ie mysql, hsql.
	
	b. Execute various SELECT SQL statements on database tables. Insure to try
       statements that contain WHERE, GROUP key words.
       
	c. Execute SELECT SQL statements on data. Example SELECT 2*2.
       Note: Oracle requires the selection from the table DUAL.
       
5. Test Search Database Tool (Each Supported Database Application)

	a. Execute a generic search on the each of the test databases.
    
	b. Insure tables in the list may be selected and automatically
       displayed in the main applications window.
       
	c. Insure clearing and canceling operations functions along with
       copying, cutting, and pasting in the search phrase text field.
       
6. Preferences & Basic Import/Exports (Each Supported Database Application)

	a. Table Summary View - Change visible column fields.

	b. Table Summary View - Change number of rows shown in tab summary tables.

	c. Data Export - Check the generic CSV export of table and summary
       table. Verify the inclusion of TEXT, MEDIUMTEXT, & LONGTEXT is enabled
	   during CSV export from the Preferences | Data Export | CSV.

	d. Data Export CSV - Change the deliminter, date format and export CSV data
	   both table & summary table.
       
	e. Data Export SQL - Check the generic SQL export of table and summary table
       for the xxxxtypes database table. Insure this is done for one row of data
       and multiple rows of data. Use the Preferences Panel Edit | Preferences |
       SQL Format options to exercise INSERT/UPDATE, Singular/Plural/Explicit.
           
	f. Data Import CSV - Verify data exported in 5.c. & 5.d. can be imported into
       same table. Change preferences import deliminter, date format as needed.
       Check both insert and update.
       
	g. Data Import SQL - Verify data exported in 5.e. can be imported into same
	   table.

	h. Export Database and Database Scheme. Import both output files with
       MyJSQLView. As an alternate check, use each database's standard
       management tool to import the database and database scheme created
       to verify conformity.
       
7.	Summary Table State Saving/Opening.

	a. Table Summary View - Insure that state of summary view table is saved to
	   directed file. Vary properties, table fields, row number, sort fields,
	   search fields, and advanced sort/search entries.
	   
	b. Table Summary View - Insure that state is restored for each saved *.myj
	   file from 7.a by opening with MyJSQLView.
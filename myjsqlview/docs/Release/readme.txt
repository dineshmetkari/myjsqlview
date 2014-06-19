MyJSQLView Version 3.49

Copyright 2005-2014
by Dana Proctor
http://dandymadeproductions.com 

What is MyJSQLView?

   MyJSQLView provides an easy to use Java based interface for viewing, adding,
editing, or deleting entries in the Derby, H2, HSQL, Microsoft SQL, MSAccess,
MariaDB, MySQL, Oracle, PostgreSQL, and SQLite databases. All tables and fields
are available for access in the selected database.

   MyJSQLView is the result of a condensed version of a larger project that was
needed to access an inventory database. The project from its inception was to be
constructed from code that could be run on most OS, Operating Systems, platform
independent. In addition the selected database, MySQL, was also deemed to be
chosen because of its open source nature. The final key was network enabled.
The application would need to be able to connect to the database either locally,
same machine, or a server anywhere that was Internet accessable. Java and MySQL
fit these needs.
    MyJSQLView has expanded beyond the scope of MySQL and now supports alternate
databases and plugins. The application only needs two other pieces of code, the
JRE, Java Runtime Environment, and a JDBC, Java Database Connectivity, driver.
The JDBC provides the driver interface between the Java SQL, structured query
language, statements, and the database. As of the release of this version of
MyJSQLView the application has been tested with the Derby, H2, HSQL, MS SQL,
MSAccess, MySQL, Oracle, PostgreSQL, and SQLite databases. The application once
installed can provide a much quicker access/update to a database than a web
based interface frontend and MyJSQLView supports transaction locking. If your
small business, scientific community, government agency, or educational institute
wants to quickly access either a Apache Derby, H2, HSQL, MS SQL, MSAccess, MySQL,
Oracle, PostgreSQL, or SQLite database for viewing, adding, editing, searching,
and analyzing data MyJSQLView provides an alternative that is simple and easy to
use.

   Dana M. Proctor
   Project Manager 

Features:

   * Plugin Framework.
   * Internationalization Support.
   * Simple Easy to Use Interface.
   * User Connection Preferences Saving/Managing.
   * User Preferences Summary Table Column Selection & Sizing.
   * User Preferences Summary Table Row Sizing.
   * Access to All User Tables in Database During Session.
   * Simple and Advanced Table Sorting and Searching.
   * Summary Table State Saving/Loading.
   * Viewing, Adding, Editing, and Deleting All Table Fields.
   * Update Multiple Rows of Selected Table Field.
   * Support for Table Field Function Operations.
   * Support for All Basic Data Types, Including Blob/Bytea/Binary.
   * Data Type Checking During Adding or Updates.
   * Support for All Table Types, Including Transaction Locking.
   * Support for Flushing Privileges for User root on mysql database.
   * Printing of Summary Table Data, Screen View.
   * Export of CSV Summary or Complete Table Data.
   * Export of SQL Summary or Complete Table Structure/Data.
   * Export of PDF Summary Table.
   * Export of SQL Current Open Database/Scheme.
   * Import of SQL Statement File.
   * Import of CSV data File Into Selected Database Table via Insert/Update.
   * Query Bucket for Storing/Organizing SQL Statements.
   * Query Frame for Building Complex SQL Statements.
   * Search Frame for Running Generic LIKE Queries on All Tables in Database.
   * In Memory Data Analysis Capability

Requirements:

   * Microsoft® Windows 7, Vista ?, XP, 2000, 98, NT, ME, MAC?, Linux, & X-Window Environment.
   * Java Runtime Environment, JRE 1.5 or Newer.
   * Apache Derby 10.10.2.0.
   * H2 1.3.173 - 1.3.175.
   * HSQL 2.2.5 - 2.3.2. *NOTE, 1.8.x No Longer Supported.
   * MSAccess 97 - 2003, ODBC-JDBC Bridge, Control Panel Data Sources.
   * Microsoft SQL Server 8? - 11.00.2100, JDBC 4.0.
   * MySQL 5.0.7 - 5.5.32, JDBC 5.0.7, 5.1.5 - 5.1.30.
   * MariaDB 5.5.32, JDBC 1.1.7.
   * Oracle 10g JDBC 14.
   * PostgreSQL 8.2.5 - 8.4.4, 9.0.1, JDBC3 8.2-506 - 8.4-702, 9.3.
   * Xerial SQLite JDBC 3.6.20 - 3.7.2. Note, SQLite need not be installed on the system.

Installation Notes:

   MyJSQLView is a stand alone application. To get started download and unzip
the release file to your local hard drive using a program such as WinZip, 7-Zip,
Tar or similar compression/decompression program. Extract the MyJSQLView files
to the desired location on the hard disk. On a Windows system this may be any
location, but on a Unix or Linux system it will be in the home directory of
the user. If multiple users are to access the application then consult with
the system administrator of your workstation. The likely location will be in
the /usr/local or a public directory, which all users have access to for
application execution.

Update Notes:

   MyJSQLView updates need no special attention. Just install the latest
release into your existing directory. For prior releases to Version 3.31
though the project is recommending to perform a clean install. This is not a
necessity, but will clean out some unused files, mainly in the images directory.
The only other major concern of a new release that might be of interest is
obtaining new connection parameters. Each new release that supports an
additional database, release v3.49 does, will have an example configuration
for the connection parameters in the advanced login parameters. Likely
changes will be included in the reference myjsqlview.xml file located in the
installation directory. Often the Driver, Protocol, SubProtocol, and Port
parameters will be different for the various database servers. To gain access
to any new connection parameters one may just open the reference myjsqlview.xml
file and copy and paste any additional entries desired into your own working
copy of the myjsqlview.xml file. See the General Setup Instructions below for
locating this file. NOTICE! AS OF MyJSQLView v3.17 RELEASE A MINOR CHANGE IN
THE myjsqlview.xml CONFIGURATION FILE WILL INVALIDATE ALL PREVIOUSLY VERSIONS'
SAVED PASSWORDS. To remedy this discrepancy use the Connection Manager to Update
your sites by typing in the password again.


General Setup Instructions

    The MyJSQLView application is a Java based program and does require the JRE,
to be installed. The minimum runtime environment needs to be JRE-1.5. In addition
to the JRE an extension, JDBC, needs to be installed that allows Java to
communicate to the Derby, MS SQL, MariaDB, MySQL, Oracle, PostgreSQL, or SQLite
database. The programs are available on the Internet free of charge. Check with
the sites db.apache.org/derby/, microsoft.com/en-us/sqlserver/, mariadb.org,
dev.mysql.com, oracle.com, jdbc.postgresql.org, and xerial.org. The jar file
for these extensions needs to be installed to the JRE /lib/ext directory. On a
Windows system this JRE directory in normally under C:/Windows/java. On a Linux/Unix
system the directory is normally under the /usr/lib directory, and is most easily
found by typing the command "which java" in a console. Please consult with each
vendor for specific installation instructions for these JDBC pieces of code.
Similarly if other databases are to be accessed via the application then again
installed the recommended driver(s) to the Java Runtime /lib/ext directory. The
exceptions to JDBC installations are for the Derby, H2, and HSQL databases. Derby
requires the derby.jar, derbynet.jar, and derbyclient.jar files to placed in the
Java Extension directory. H2, and HSQL uses an integrated JDBC built into the
application. MyJSQLView as of v3.47 includes these databases so no additional
installation need take place.

    The new login interface and Connection Manager use a XML configuration file
for saving user preferences. Upon first running MyJSQLView a reference XML file,
myjsqlview.xml, located in the installation directory is read then copied to
the users home directory under a newly created directory, .myjsqlview. DO NOT
MODIFY THIS REFERENCE FILE! The newly created directory On Windows ® 98 will be
'C:Windows\.myjsqlview'. On Windows ® XP, Vista, and Windows 7 the new directory
will be in the user's home folder. On a Linux system the new directory will be
'/home/user/.myjsqview'. All saved changes performed in site management will be
stored in the XML file located in the above referenced directory. The user is
encouraged to make a backup of this file occasionally. As of MyJSQLView Version
3.17 the application also supports internationalization. The .myjsqlview
directory in addition to retaining the XML configuration file will also house
the file myjsqlview_locale.txt which holds the entry that allows control of
language support.

    MyJSQLView requires a database to be setup to communicate with in order to
work. It is beyond the scope of this document for installing and setting up a
database, but remember you must have user rights to the database tables you are
trying to access. Please consult with your database system administrator for
proper grant rights. If you would like to try the application and do not have
access to a database the Dandy Made Productions web site has a MySQL database
with some sample data in several tables that may be connected too. Visit the
MyJSQLView project area at the site to obtain additional information about
connecting via MyJSQLView to these sample database tables.

Adding a Shortcut

    To add a MyJSQLView icon to your desktop, right click the mouse pointer on
an unused area of your desktop to display the pop up menu and select New |
Shortcut. Choose Browse, find and double click on MyJSQLView.jar, then click on
Next. Type in "MyJSQLView" for the name of the shortcut, then click Finish. To
run MyJSQLView, either double click on the new MyJSQLView icon or use the Start
| Run command.

     On a Unix or Linux system a shortcut may be created in the KDE Desktop
environment by right clicking on the desktop and selecting Create New | File |
Link to Application. Give the application a name such as "MyJSQLView" in the
General tab. Next select the Application tab and fill in the description desired
then browse to the location on the disk where MyJSQLView was installed and
select the MyJSQLView.jar file for the Command entry. The Command entry is still
not complete and must contain the Java command. Place before the quoted
MyJSQLView.jar entry, "java -jar ". Do not place quotes of any kind around this
command, the quotes only contain the Jar file location. Example:
java -jar '/home/~user/MyJSQLView/MyJSQLView.jar'. Finally select the Work
Path as the directory where installation took place.

     The MyJSQLView application may also be run directly from a command/console
window by typing in the command "java -jar MyJSQLView.jar" while in the
installation directory. A Linux/Unix environment will require the file location
to be specified by "./MyJSQLView.jar", remember this must be done with a console
that is running within a X Window environment like KDE or Gnome. As an alternative
some uses who are using large database table with tuples, larger than 300k rows,
may need to allocate additional memory by using the command "java -Xmx128m -jar
MyJSQLView.jar". 

Copyright Notice:

This program is licensed under the GNU GPL.

Credits (in chronological order):

   * Dana Proctor - Project Manager
   * Clifford Hanson - Consultant
   * Jujubi - Developer
   * Lookfwd - Analysis/Design
   * Blarkin - Developer
   * Nil_lin - Developer
   * Gashogtoo - Tester
   * Poisonerbg - Developer
   * reydelcompas - Tester

Special thanks to:
L.S. Proctor who provided support and mental clarity.

Version History:

Production (2008-12-31):

   * Version 3.49 MariaDB Database Support. Maintenance Update to Address Issues With MySQL
                  JDBC New Than 5.1.8.
   * Version 3.48 Microsoft SQL Database Support. Visual QueryBuilder Plugin Update.
   * Version 3.47 H2 Database Support. Architectural revisions to support multiple
                  database connections instances. HeatMapper plugin CSV Imports.
   * Version 3.46 Bug fix for CSV imports of single digit month fields. Feature
                  additions of LIMIT Control over CSV Summary Table Exports and
                  overall interface font sizing. Finnish and Hebrew language
                  support.
   * Version 3.44 Apache Derby Database Support. Additional Connection Properties
                  Input Via Database Entry Login Form. Query Frame UI Changes.
                  HeatMapper Plugin Enhancements. 
   * Version 3.41 Initial Plugin Inclusion of the visual SQL tool QueryBuilder. 
   * Version 3.40 Code reorganization to have sub-packages to myjsqlview. Rebuild
                  of resource bundler to handle network assets. Complete rebuild
                  of plugin framework to handle network located plugins.
   * Version 3.35 Maintenance update to address issues with immutable strings
                  associated with the SQL Query Bucket, alias name resolution
                  in the Query Frame, and a complete update to data types to
                  remove legacy frameworks.
   * Version 3.34 Maintenance update to address issues with data imports and
                  exports on database tables with rows larger than 100k+ tuples.
                  Creation Preferences General Options for controlling table
                  read and write batching.
   * Version 3.33 Integration of HSQLDB2, HyperSQL, database for In-Memory data
                  Analysis. HyperSQL database File and Jar resource connection
		  		  support. Advanced Sort/Search interface Aggregation and GROUP
		  		  BY feature. SQL Query Bucket Fixes and interface changes to
		  		  increase efficiency of use. HeatMapper Plugin.
   * Versoin 3.31 HSQLDB2 and MSAccess Database Support. Bug fix for large SQL
                  import files. Multi-language PDF support through the use of
		  		  embedded fonts. Corrections to table definition generator unique
		  		  key construction. 
   * Versoin 3.30 Preliminary Production Release to Address/Correct Reported Problems
                  With PDF Exports and Gnome Desktop Functionality.
   * Version 3.29 Summary Table sort/search history implementation and addition of
                  Query Bucket for query storage, organizing, and drops to plugins.
   * Version 3.26 Architectural modularization for core aspects relating to login
                  and connection management. Date display formatting preferences
                  selection. PNG image output of summary table display.
   * Version 3.23 SQLite Access. Manual Plugin Management. Additional Internationalization
                  Support.
   * Version 3.22 Threaded/Refinements to Plugin Architecture. Upgrade to Java
                  5.0 API. PDF Exports.
   * Version 3.17 Internationalization Support. Completed Plugin Framework.
   * Version 3.06 Initial Plugin Inclusion, Table Field Charts.
   * Version 3.05 Incremental Release with Core Architecture Cleanup, and Minor
                  Bug Fixes. Update Multiple Rows Feature.
   * Version 3.03 Incremental Release with Minor Bug Fixes and Network Bandwidth
                  Reduction Enhancements. CSV Import/Export Date Format Option
                  and Summary Table State Saving/Loading.
   * Version 3.01 Minor Bug Fixes and Performance Enhancements, GUI Revisions,
                  and Plugin Framework.

Beta (2006-10-01):

   * Version 2.91 Minor Bug Fixes and Performance Enhancements, Oracle Database
                  Access.
   * Version 2.87 Fully Qualified Schema.TableNames Support, Fault Tolerance.
   * Version 2.86 Minor Bug Fixes, HSQL Database Access, PostgreSQL Array Support,
                  & CSV Insert/Update Import.
   * Version 2.81 Minor Bug Fixes, PostgreSQL Database Access, & CSV Import.
   * Version 2.76 Code Cleanup, Field Function Operations, Advanced Sort/Search,
                  Database SQL Export, & Root User Flush Privileges.
   * Version 2.72 Bug Fix for Multiple Keys, SQL Data Import/Export. TableEntryForm
                  Enhancements.
   * Version 2.64 New Login Interface & Connection Manager.
   * Version 2.63 Another Attempt Bug Fix for Timestamp Fields. Primary Keys
                  Editing if not AutoIncrement.
   * Version 2.61 Bug Fix for Date Fields.
   * Version 2.5  Bug Fix for Timestamp Fields.
   * Version 2.4  Documentation Revision.
   * Pre-Beta	  Initial Public Release.
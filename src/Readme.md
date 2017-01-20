### Vacation Management

#####Requirements:
I. Install Wildfly locally and make the following changes:
1. in wildfly-9.0.2\standalone\configuration\standalone.xml
2. in wildfly-9.0.2\modules\system\layers\base\com add a new folder oracle\jdbc\main\ with 2 files:
module.xml and ojdbc6.jar
3. If you use Eclipse Idee add the server in the servers tab.

II. Install Oracle11GExpress and Oracle SQL Developper

III. Install Eclipse idea.

#####DB 
1. Create a user: appvacation with password:password and with some privilegies
2. Create a schema vacation-app using the above user.
3. Create the following tables in that schema. (or import it/run script from: vacation\db\UtilityScripts\database.sql).

######DBtable:
#######1. EMPLOYEE
#######2. APP_USER
#######3. DEPARTMENT
#######4. PUBLIC_HOLIDAY
#######5. REQUESTED_PTO

4. Run script intialization.sh which contains:
- insert into APP_USER values('6', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'HR', '0');
- insert into EMPLOYEE values('5', 'Simona', 'Roboiu', '05-JAN-2017', '1', '1', '21', '0', '1', '6');
- CREATE SEQUENCE hibernate_sequence;

OBS. Username: admin and password:admin

#####Project:
1. Import the project in Eclipse
2. Start server
3. Call in the browser the following link: http://localhost:8080/vacation-webapp/index.xhtml

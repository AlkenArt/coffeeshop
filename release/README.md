# coffeeshop
Coffee Shop order management by staff person

Installation instruction:
	1. install mongodb 3.2 
	2. start mongobb with the command (windows)
	<Mongodb location>\Server\3.2\bin\mongod.exe --dbpath <Mongodb location>\data
	3. build the package by below command in downloaded directorycopy
		#mvn clean install
	4. Take the build from /application/target/coffeeshop.war and put it in deployment location
	5. run below command to start the application
	java -jar coffeeshop.war
	6. it can be deployed in tomcat as well.
	
Default User list:
admin User:
admin@coffeeshop.com/Coffeeshop@123
staff@coffeeshop.com/Coffeeshop@123
	
Supported Feature List:
1. New User addition and login
2. Login and hover on user name to see last login time
3. login as admin user and see the user details.
4. default sign up gives the STAFF priviledges
5. STAFF can login and place order and check the status in detiled section.

Feature to be supported
1. Currently User addition and modification are provided but this release doesn't assure that those feature will work since it's in progress
2. Inmemory database - in progress currently integrated with Mongodb
3. Unit Test to be written because of time condtrant currently not available
4. Price invoice in form of pdf
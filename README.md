# Aut_timetable_builder
A timetable builder for classes at AUT university in Auckland NZ.

This code is based in java 8.0 and above.

It has methods to scrape the AUT arion website, taking all the course details that matter to us. 
Once it has all the details the program formats this into comma seperated variables, which is then used
and uploaded to a MSSQL server hosted by azure. Currently the database is setup to only allow connections 
from a certain range of IP addresses which AUT uni own. Therefore any changes made to the DB through the program 
have to be done from onsite.

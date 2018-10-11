# Aut_timetable_builder
A timetable builder for classes at AUT university in Auckland NZ.

This code is based in java 8.0 and above.

It has methods to scrape the AUT arion website, taking all the course details that matter to us. 
Once it has all the details the program formats this into comma seperated variables, which is then used
and uploaded to a MSSQL server hosted by azure. Currently the database is setup to only allow connections 
from a certain range of IP addresses which AUT uni own. Therefore any changes made to the DB through the program 
have to be done from onsite.

The purpose of this is to be able to retrieve all papers that AUT offers, get all paper details of each paper, format this into a csv format so that it can be uploaded to the sql database.

Once this is completed, I will be able to incorperate my previously built timetable builder which takes all posible combinations of papers that a user chooses, with this new data. This allows essentially any student to go to the AUT timetable builder website, pick 4-5 papers and have all combinations of non-clashing timetables be generated. With this, the user can easily pick and choose visually what timetable they may want and prioritize certain aspects like having a certain day off, not having morning classes etc..

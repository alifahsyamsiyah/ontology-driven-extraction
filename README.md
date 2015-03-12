# Ontology-Driven-Extraction
Ontology-Driven Extraction is a prototype of ProM plugin that is built to supports domain experts in the extraction of XES event log information from legacy relational databases.

# Prerequisites
1. Mysql
2. Java 1.7 or later
3. Eclipse or any Java IDE that you like. Notice that in this documentation we use Eclipse as it is an open source Java IDE.

# How to install
1. Import the database `sakila.sql.zip` into your local server.
2. Open file `ontology.obda` under the folder `/ProM/src/main/resources/example/` and change your local server properties (your local server name, the username, and the password) as shown in these lines:
  * connectionUrl	jdbc:mysql://[your local server name]/sakila
  * username	[your username]
  * password	[your password]
3. 

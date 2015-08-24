# Ontology-Driven-Extraction
Ontology-Driven Extraction is a prototype of ProM plugin that is built to supports domain experts in the extraction of XES event log information from legacy relational databases.

# Prerequisites
1. Mysql
2. Java 1.7 or later, for 64-bit
3. Java IDE with Maven installation. In this documentation we use Eclipse as it is an open source Java IDE.

# Files in this folder
1. `ProM` is a Maven project. You may move it into your Eclipse workspace
2. `sakila.sql.zip` is the database
3. `README.md` is this readme file
4. Some examples for the ontology, mapping, and annotation are given under `/ProM/src/main/resources/example/`:
  * `ontology-man-sakilas.owl` is the domain ontology
  * `ontology-man-sakilas.obda` is the first mapping, which is the mapping from the database to the domain ontology
  * `anno_sakila_man3.annotation` and `anno_sakila.annotation` are the annotations

Note: you may use different ontology, mapping, and annotation files.

# How to install
1. Import the database `sakila.sql.zip` into your local server.
2. Open file `ontology-man-sakilas.obda` under the folder `/ProM/src/main/resources/example/` and change your local server properties (your local server name, the username, and the password) as shown in these lines:
  * connectionUrl	jdbc:mysql://`[your local server name]`/sakila
  * username	`[your username]`
  * password	`[your password]` <br />
3. Create a Maven project in Eclipse by do this following:
  * `File > Import > Maven > Existing Maven Projects > Next` 
  * In `Root Directory` click `Browse`
  * Select the folder where you place `ProM`, click `Open`, then click `Finish`

# How to run
1. Click on `Run` button on Eclipse (the green circle with white play button inside) and choose `ProM with UITopia` 
2. After ProM window appears (which may take several time during first installation), import `ontology-man-sakilas.obda`, `ontology-man-sakilas.owl`, and `anno_sakila_man3.annotation` into ProM.
3. Click on `Action` tab. It is the middle button on the top bar of ProM.
4. Search `Ontology-Driven Extraction Plugin` 
5. Choose `ontology-man-sakilas.owl` as the Domain Ontology, `ontology-man-sakilas.obda` as the First Mapping, and `anno_sakila_man3.annotation` as the Annotation.
6. Click `Start`
7. Now the system will create the automatic mapping from the database to the event ontology. After it finishes, a window to input SPARQL queries appears. Here you can test any SPARQL query to see whether the annotation and mapping are done correctly. One simple query that you may ask to check all traces and all events is given below: <br />
PREFIX : \<http://myproject.org/odbs#> <br />
SELECT DISTINCT ?x ?y<br />
WHERE { ?x a :Trace; :TcontainsE ?y.}
8. Click `Process` to see the answers
9. If you click `Finish` means that you materialize the event data into an XES event log file.  You may use this file as an input of process mining algorithms (e.g. Mine for a Petri Net using Alpha-algorithm) to get more interesting result.

# Create the domain ontology and mappings automatically
1. Under the `Action` tab, search for `Ontology-Mapping Bootstrap Plugin`.
2. Click `Start`
3. Enter your bootstrap configuration. You may use the following example: <br />
Ontology file: `src/main/resources/example/ontology-boot-sakilas.owl` <br />
Mapping file: `src/main/resources/example/ontology-boot-sakilas.obda` <br />
Base URI: `http://www.example.org/` <br />
Jdbc URL: `jdbc:mysql://[your local server name]/sakilas` <br />
Username: `[your username]` <br />
Password: `[your password]` <br />
Jdbc Driver Class: `com.mysql.jdbc.Driver` <br />
4. Click `Finish`, and now the system will create the domain ontology and mapping files.
5. Import `anno_sakila.annotation` as the annotation files
6. Now you can run the `Ontology-Driven Extraction Plugin` following the "How to run" section above using these three files.





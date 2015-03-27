# Ontology-Driven-Extraction
Ontology-Driven Extraction is a prototype of ProM plugin that is built to supports domain experts in the extraction of XES event log information from legacy relational databases.

# Prerequisites
1. Mysql
2. Java 1.7 or later, for 64-bit
3. Java IDE with Maven installation. In this documentation we use Eclipse as it is an open source Java IDE.

# Files in this folder
1. `ProM` is a Maven project. You may move it into your Eclipse workspace
2. `conference.sql` and `sakila.sql.zip` are the databases
3. `README.md` is this readme file
4. Some examples for the ontology, mapping, and annotation is given under `/ProM/src/main/resources/example/`:
  * `ontology.owl` and `ontology3.owl` are the domain and event ontology
  * `ontology.obda` and `ontology3.obda` are the first mapping, which is the mapping from database to the domain ontology
  * `anno.annotation` and `anno9.annotation` are the annotation

Note: you may use different ontology, mapping, and annotation files and put them into `/ProM/src/main/resources/example/` folder

# How to install
1. Import the database `conference.sql` and `sakila.sql.zip` into your local server.
2. Open file `ontology.obda` under the folder `/ProM/src/main/resources/example/` and change your local server properties (your local server name, the username, and the password) as shown in these lines:
  * connectionUrl	jdbc:mysql://`[your local server name]`/sakila
  * username	`[your username]`
  * password	`[your password]` <br />
The same instruction for `ontology3.obda` under the folder `/ProM/src/main/resources/example/`
3. Create a Maven project in Eclipse by do this following:
  * `File > Import > Maven > Existing Maven Projects > Next` 
  * In `Root Directory` click `Browse`
  * Select the folder where you place `ProM`, click `Open`, then click `Finish`

# How to run
1. Click on `Run` button on Eclipse (the green circle with white play button inside) and choose `ProM with UITopia` 
2. After ProM window appears (which may take several time during first installation), click on `Action` tab. It is the middle button on the top bar.
3. Search `Ontology-Driven Extraction Plugin` and click `Start`
4. Enter your input files. You may use examples given in this folder by typing:
  * Ontology: `ontology.owl`
  * First mapping: `ontology.obda`
  * Annotation: `anno.annotation`<br />
 You may user another example: `ontology3.owl` (ontology), `ontology3.owl` (first mapping), and `anno9.annotation` (annotation).
5. Click `Create Second Mapping`
6. After the information "Second mapping is successfully created. Click finish to go to the next step." is shown, click `Finish`
7. Here you can test your SPARQL Query to see whether the annotation and mapping are done correctly. One simple query that you may ask to check all traces and all events is given below: <br />
PREFIX : \<http://myproject.org/odbs#> <br />
SELECT DISTINCT ?x ?y<br />
WHERE { ?x a :Trace; :TcontainsE ?y.}
8. Click `Process` to see the answers
9. If you click `Finish` means that you materialize all annotation in a single XES event log that you may use to process further.


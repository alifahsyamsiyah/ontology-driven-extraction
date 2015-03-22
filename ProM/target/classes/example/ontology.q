[QueryItem="Payment Detail"]
PREFIX : <http://myproject.org/odbs#>

SELECT ?x ?price ?date
WHERE { 
	?y a :Rental ; :idRental "76"^^xsd:integer; :paymentRental ?x .
	 ?x a :Payment; :price ?price ; :dateOfPayment ?date. }

[QueryItem="Customer Window"]
PREFIX : <http://myproject.org/odbs#>

SELECT ?title ?genre ?languageFilm ?releaseDate ?y ?dateRent ?dateReturn ?rentalId
WHERE { ?x a :Customer; :firstName "MARY" ; :lastName "SMITH". 
?y a :Rental ; :customerRental ?x ; :dateOfRent ?dateRent ; :dateOfReturn ?dateReturn ; :basedOn ?z ; :idRental ?rentalId . 
?z a :Inventory ; :filmInventory ?u .
?u a :Film ; :title ?title ; :genre ?genre ; :languageFilm ?languageFilm ; :releaseDate ?releaseDate .}

[QueryItem="Profile"]
PREFIX : <http://myproject.org/odbs#>

SELECT ?street ?sector ?postalCode ?city ?country
WHERE { ?x a :Person; :firstName "MARY"; :lastName "SMITH"; :hasAddress ?y.
	?y a :Address; :street ?street ; :sector ?sector; :postalCode ?postalCode; :addressLocatedIn ?z.
	?z a :City; :nameCity ?city; :cityLocatedIn ?u.
	?u a :Country; :nameCountry ?country.
	}

[QueryItem="All Countries Name"]
PREFIX : <http://myproject.org/odbs#>

SELECT ?nameCountry
WHERE {?x a :Country ; :nameCountry ?nameCountry . }

[QueryItem="All Cities in a Country"]
PREFIX : <http://myproject.org/odbs#>

SELECT ?nameCity
WHERE {?x :cityLocatedIn ?y. ?y :nameCountry "Indonesia".
	?x :nameCity ?nameCity .}

[QueryItem="All Stores in a City"]
PREFIX : <http://myproject.org/odbs#>

SELECT ?idStore
WHERE { ?x :storeLocatedIn ?a.
	?a :addressLocatedIn ?c.
	?c :nameCity "Lethbridge".
	?x :idStore ?idStore .}

[QueryItem="All Customer which Have Rented a Movie from this Store"]
PREFIX : <http://myproject.org/odbs#>

SELECT ?firstname ?lastname
WHERE { ?s :idStore "1"^^xsd:integer. 
	?f :title "AMADEUS HOLY".
	?i :filmInventory ?f.
	?i :storeInventory ?s.
	?r :basedOn ?i.
	?r :customerRental ?c.
	?c :firstName ?firstname ; :lastName ?lastname.
	}

[QueryItem="Customer by Name"]
PREFIX : <http://myproject.org/odbs#>

SELECT ?firstname ?lastname
WHERE { ?x a :Customer; :firstName ?firstname; :lastName ?lastname.
	}

[QueryItem="All Available Movie from a Store"]
PREFIX : <http://myproject.org/odbs#>

SELECT
WHERE {
	}

[QueryItem="All Movie from a Store"]
PREFIX : <http://myproject.org/odbs#>

SELECT ?title
WHERE { ?x :idStore "1"^^xsd:integer .
	?y :storeInventory ?x.
	?y :filmInventory ?z.
	?z :title ?title.
	}

[QueryItem="Cellphone"]
PREFIX : <http://myproject.org/odbs#>

SELECT ?phone
WHERE { ?x a :Person; :firstName "MARY"; :lastName "SMITH"; :cellphone ?phone. }

[QueryItem="Search Film by Name"]
PREFIX : <http://myproject.org/odbs#>

SELECT DISTINCT ?title ?genre ?releaseDate ?city
WHERE {?x a :Film; :title ?title; :genre ?genre; :releaseDate ?releaseDate.  FILTER regex(?title, "^ama", "i").
	?y a :Inventory; :filmInventory ?x; :numberOfAvailability ?num; :storeInventory ?z. FILTER (?num > "0^^xsd:integer") .
	?z a :Store; :storeLocatedIn ?u.
	?u a :Address; :addressLocatedIn ?v.
	?v a :City; :nameCity ?city.
	}

[QueryItem="Search Film by City"]
PREFIX : <http://myproject.org/odbs#>

SELECT DISTINCT ?title ?genre ?releaseDate ?city ?country
WHERE {?x a :Film; :title ?title; :genre ?genre; :releaseDate ?releaseDate.
	?y a :Inventory; :filmInventory ?x; :numberOfAvailability ?num; :storeInventory ?z. FILTER (?num > "0^^xsd:integer") .
	?z a :Store; :storeLocatedIn ?u.
	?u a :Address; :addressLocatedIn ?v.
	?v a :City; :nameCity ?city; :cityLocatedIn ?w. FILTER regex(?city, "Lethbridge", "i").
	?w a :Country; :nameCountry ?country.
	}

[QueryItem="Search FIlm by Director"]
PREFIX : <http://myproject.org/odbs#>

SELECT DISTINCT ?title ?genre ?releaseDate ?city
WHERE {?x a :Film; :title ?title; :genre ?genre; :releaseDate ?releaseDate; :hasActor ?z.
	?y a :Inventory; :filmInventory ?x; :numberOfAvailability ?num; :storeInventory ?u. FILTER (?num > "0^^xsd:integer") .
	?z a :Actor; :firstNameCeleb "KEVIN"; :lastNameCeleb "BLOOM".
	?u a :Store; :storeLocatedIn ?v.
	?v a :Address; :addressLocatedIn ?w.
	?w a :City; :nameCity ?city.
	}

[QueryItem="test"]
PREFIX : <http://myproject.org/odbs#>

SELECT * WHERE {
{SELECT *
WHERE {?x a :Film; :title "ACADEMY DINOSAUR"}}
UNION
{SELECT *
WHERE {?x a :Film; :title "ADAPTATION HOLES"}}
}

[QueryItem="Search Film by Actor"]
PREFIX : <http://myproject.org/odbs#>

SELECT DISTINCT ?title ?genre ?releaseDate ?city ?fname ?lname 
WHERE {?x a :Film; :title ?title; :genre ?genre; :releaseDate ?releaseDate; :hasActor ?z.
	?y a :Inventory; :filmInventory ?x; :numberOfAvailability ?num; :storeInventory ?u. FILTER (?num > "0^^xsd:integer") .
	?z a :Actor; :firstNameCeleb ?fname; :lastNameCeleb ?lname. FILTER regex(?fname, "kev", "i"). FILTER regex(?lname, "bloo", "i").
	?u a :Store; :storeLocatedIn ?v.
	?v a :Address; :addressLocatedIn ?w.
	?w a :City; :nameCity ?city.
	}

[QueryItem="Search Film by Actor2"]
PREFIX : <http://myproject.org/odbs#>

SELECT * WHERE {
{SELECT DISTINCT ?title ?genre ?releaseDate ?city ?fname 
WHERE {?x a :Film; :title ?title; :genre ?genre; :releaseDate ?releaseDate; :hasActor ?z.
	?y a :Inventory; :filmInventory ?x; :numberOfAvailability ?num; :storeInventory ?u. FILTER (?num > "0^^xsd:integer") .
	?z a :Actor; :firstNameCeleb ?fname; :lastNameCeleb ?lname. FILTER regex(?fname, "gar", "i").
	?u a :Store; :storeLocatedIn ?v.
	?v a :Address; :addressLocatedIn ?w.
	?w a :City; :nameCity ?city.
	}}
UNION
{SELECT DISTINCT ?title ?genre ?releaseDate ?city ?lname
WHERE {?x a :Film; :title ?title; :genre ?genre; :releaseDate ?releaseDate; :hasActor ?z.
	?y a :Inventory; :filmInventory ?x; :numberOfAvailability ?num; :storeInventory ?u. FILTER (?num > "0^^xsd:integer") .
	?z a :Actor; :firstNameCeleb ?fname; :lastNameCeleb ?lname. FILTER regex(?lname, "gar", "i").
	?u a :Store; :storeLocatedIn ?v.
	?v a :Address; :addressLocatedIn ?w.
	?w a :City; :nameCity ?city.
	}}
}

[QueryItem="test2"]
PREFIX : <http://myproject.org/odbs#>

SELECT DISTINCT ?title ?genre ?releaseDate ?city ?lname
WHERE {?x a :Film; :title ?title; :genre ?genre; :releaseDate ?releaseDate; :hasActor ?z.
	?y a :Inventory; :filmInventory ?x; :numberOfAvailability ?num; :storeInventory ?u. FILTER (?num > "0^^xsd:integer") .
	?z a :Actor; :firstNameCeleb ?fname; :lastNameCeleb ?lname. FILTER regex(?lname, "gar", "i").
	?u a :Store; :storeLocatedIn ?v.
	?v a :Address; :addressLocatedIn ?w.
	?w a :City; :nameCity ?city.
	}

[QueryItem="All rented movie of someone"]
PREFIX : <http://myproject.org/odbs#>

SELECT DISTINCT ?id ?drent ?dreturn
WHERE {?f a :Film; :title "patient sister".
	?i :filmInventory ?f.
	?r :basedOn ?i; :idRental ?id; :dateOfRent ?drent; :dateOfReturn ?dreturn; :customerRental ?c.
	?c :firstName "mary"; :lastName "smith".
	}

[QueryItem="Payment detail of someone"]
PREFIX : <http://myproject.org/odbs#>

SELECT DISTINCT ?id ?drent ?dreturn ?idpay ?price ?dpay
WHERE {?f a :Film; :title "closer bang".
	?i :filmInventory ?f.
	?r :basedOn ?i; :idRental ?id; :dateOfRent ?drent; :dateOfReturn ?dreturn; :customerRental ?c; :paymentRental ?p.
	?c :firstName "mary"; :lastName "smith".
	?p :idPayment ?idpay; :price ?price; :dateOfPayment ?dpay.
	}

[QueryItem="test3"]
PREFIX : <http://myproject.org/odbs#>

SELECT * WHERE {
{SELECT DISTINCT ?fname ?lname ?city 
WHERE {?x a :Customer; :firstName ?fname; :lastName ?lname; :hasAddress ?y. FILTER regex(?fname, "i" ,"i").
	?y a :Address; :addressLocatedIn ?z.
	?z a :City; :nameCity ?city. 
}}
UNION
{SELECT DISTINCT ?fname ?lname ?city
WHERE {?x a :Customer; :firstName ?fname; :lastName ?lname; :hasAddress ?y. FILTER regex(?lname, "inn" ,"i").
	?y a :Address; :addressLocatedIn ?z.
	?z a :City; :nameCity ?city. 
}}
}

[QueryItem="test4"]
PREFIX : <http://myproject.org/odbs#>

SELECT * WHERE {
{SELECT DISTINCT ?fname ?lname ?city
WHERE {?x a :Customer; :firstName ?fname; :lastName ?lname; :hasAddress ?y. FILTER regex(?fname, "mil" ,"i").
	?y a :Address; :addressLocatedIn ?z.
	?z a :City; :nameCity ?city. 
	}}
UNION
{SELECT DISTINCT ?fname ?lname ?city
WHERE {?x a :Customer; :firstName ?fname; :lastName ?lname; :hasAddress ?y. FILTER regex(?lname, "mil" ,"i").
	?y a :Address; :addressLocatedIn ?z.
	?z a :City; :nameCity ?city. 
	}}
}

[QueryItem="tes"]
PREFIX : <http://myproject.org/odbs#>

SELECT DISTINCT ?title ?genre ?relDate ?nCopies ?nAv
				WHERE { ?x a :Staff; :firstName "Mike" ; :lastName "Hillyer" ; :worksIn ?y.
				?y a :Store; :idStore  ?idStore.
				?z a :Inventory; :numberOfCopies ?nCopies; :numberOfAvailability ?nAv ; :storeInventory ?y; :filmInventory ?u. 				
FILTER (?nAv > "0^^xsd:integer").
				?u a :Film; :title ?title; :genre ?genre; :releaseDate ?relDate .}

[QueryItem="tess"]
PREFIX : <http://myproject.org/odbs#> 

SELECT * WHERE {
{SELECT ?x
WHERE {?x a :Director.}}
UNION
{SELECT ?x
WHERE {?x a :Director.}}
}

[QueryItem="tesss"]
PREFIX : <http://myproject.org/odbs#> 

SELECT DISTINCT ?f ?d  ?price 
WHERE {?f a :Film; :hasDirector ?d . 
BIND ("123" AS ?price) 
}

[QueryItem="tessss"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?e 
WHERE { ?e a :Rental; :dateOfRent ?t; :idRental ?i. FILTER(?i < "10"^^xsd:integer) . }

[QueryItem="tessssss"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?e ?a ?t
WHERE { ?a a :Rental; :customerRental ?t; :dateOfReturn ?x. ?a ?e ?x .  ?a :idRental ?i . FILTER (?i < "5"^^xsd:integer) . }

[QueryItem="tesssssss"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?e ?a ?t
WHERE { ?a a :Rental; :customerRental ?t; :dateOfReturn ?x. ?a ?e ?x . ?a :idRental ?i . FILTER (?i < “10"^^xsd:integer) .  }

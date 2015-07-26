[QueryItem="q"]
prefix : <http://www.example.org/>
prefix rent: <http://www.example.org/rental#>
select ?t ?a ?e
where {?e a :rental ; rent:rental_id ?d ; rent:rental_date ?t . filter(?d < 77) . bind (?t as ?a) . }

[QueryItem="tes"]
prefix : <http://www.example.org/>
prefix rent: <http://www.example.org/rental#>
select *
where {?e a :rental . ?d a :store .  bind(concat (?e, " ", ?d) as ?t) }

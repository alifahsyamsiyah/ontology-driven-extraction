[QueryItem="q"]
prefix : <http://www.example.org/>
select ?c ?t
where {?r :rental_customer ?c ; :rental_inventory ?i . bind(concat (?c, ?i) as ?t) . }

[QueryItem="q2"]


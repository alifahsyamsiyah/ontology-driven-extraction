[QueryItem="q"]
prefix : <http://www.example.org/pm/>
select *
where {?t a :Trace ; :TcontainsA ?a . ?a :valueA ?v . }

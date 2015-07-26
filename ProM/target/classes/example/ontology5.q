[QueryItem="user"]
PREFIX : <http://myproject.org/odbs#>

select ?u
where {?u a :User . }

[QueryItem="review"]
PREFIX : <http://myproject.org/odbs#>

select ?r ?s
where {?r a :Review; :submissionTime ?s .}

[QueryItem="trace"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?t
WHERE {?t a :Paper ; :submittedto ?c . ?c :confName ?n . FILTER regex(?n, "BPM 2015", "i") .}

[QueryItem="event_us"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?e ?t
WHERE {?t a :Paper; :has1 ?e ; :submittedto ?c . ?c :confName ?n . FILTER regex(?n, "BPM 2015", "i") .}

[QueryItem="time_us"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?t ?e
WHERE {?e a :UploadSubmitted; :uploadTime ?t.}

[QueryItem="name_us"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?n ?e
WHERE {?e a :UploadSubmitted. BIND ("upload" AS ?n) .}

[QueryItem="test"]


[QueryItem="event_dec"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?e ?t
WHERE {?e a :Decision. ?t a :Paper; :has3 ?e .}

[QueryItem="name_dec"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?n ?e
WHERE {?e a :Decision. BIND ("get a decision" AS ?n) .}

[QueryItem="time_dec"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?t ?e
WHERE {?e a :Decision; :decisionTime ?t .}

[QueryItem="event_rev"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?e ?t
WHERE { ?e a :Review. ?r a :ReviewRequest; :has4 ?e; :for ?t .}

[QueryItem="time_rev"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?t ?e
WHERE { ?e a :Review; :submissionTime ?t .}

[QueryItem="name_rev"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?n ?e
WHERE { ?e a :Review. BIND ("review" AS ?n) .}

[QueryItem="event_ua"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?e ?t
WHERE { ?e a :UploadAccepted. ?a a :AcceptedPaper; :has2 ?e; :correspondsto ?t .}

[QueryItem="event_revreq"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?e ?t
WHERE { ?e a :ReviewRequest; :for ?t . }

[QueryItem="name_revreq"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?n ?e
WHERE { ?e a :ReviewRequest. BIND ("request a review" AS ?n) . }

[QueryItem="time_revreq"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?t ?e
WHERE { ?e a :ReviewRequest; :invitationTime ?t . }

[QueryItem="event_conf"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?e ?t
WHERE { ?e a :Conference. ?t a :Paper; :submittedto ?e . }

[QueryItem="time_conf"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?t ?e
WHERE { ?e a :Conference; :confTime ?t . }

[QueryItem="name_conf"]


[QueryItem="event_all_us"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?t ?e ?n ?s
WHERE {?t a :Paper; :has1 ?e. ?e a :UploadSubmitted; :uploadTime ?s . BIND ("submit the paper" AS ?n) .}

[QueryItem="event_all_review"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?t ?e ?n ?s
WHERE {?t a :Paper. ?e a :Review; :submissionTime ?s . ?q a :ReviewRequest; :has4 ?e; :for ?t . BIND ("get a review" AS ?n) .}

[QueryItem="event_all_revreq"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?t ?e ?n ?s
WHERE {?t a :Paper. ?e a :ReviewRequest; :invitationTime ?s; :for ?t . BIND ("request a review" AS ?n) .}

[QueryItem="event_all_conf"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?t ?e ?n ?s
WHERE {?t a :Paper. ?e a :Conference; :confTime ?s. ?t :submittedto ?e. BIND ("present the paper" AS ?n) .}

[QueryItem="time_ua"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?t ?e
WHERE { ?e a :UploadAccepted; :uploadAcceptedTime ?t .}

[QueryItem="name_ua"]
PREFIX : <http://myproject.org/odbs#>
SELECT DISTINCT ?n ?e
WHERE { ?e a :UploadAccepted. BIND ("submit final paper" AS ?n) . }

[QueryItem="tesss"]
PREFIX : <http://myproject.org/odbs#>
select *
where {?d :outcome ?x . filter (?x = "true"^^xsd:boolean) .}

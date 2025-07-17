### Ktor server


## Ktor server 

Ovaj deo projekta predstavlja serversku stranu aplikacije, razvijenu u Kotlinu koristeći **Ktor** framework. 
Server prima zahteve aplikacije, komunicira sa veštačkom inteligencijom (Gemini Pro), upravlja bazom podataka (MySQL), i vraća relevantne podatke klijentu.

## Ključne funkcionalnosti 
 - Prima HTTP zahteve za generisanje detektivskih priča 
 - Komunicira sa Gemini Pro 
 - Parsira i transformiše AI odgovor u JSON format 
 - Čuva generisane podatke u MySQL bazu 
 - Omogućava REST API za dohvatanje priča i korisničkih podataka



 ## Tok rada servera  
 1. Klijent šalje zahtev za novu priču 
 2. Server kreira prompt i šalje ga Gemini Pro API-ju 
 3. Odgovor AI modela se parsira 
 4. Podaci se upisuju u MySQL bazu 
 5. Server vraća JSON nazad klijentu




### BAZA

##  Uvod

U okviru projekta implementiran je višeslojni sistem upravljanja podacima koji obuhvata:

-   **Globalnu bazu** koja je napravljena u MySQL Workbench-u i koristi se na serverskoj strani za trajno skladištenje sadržaja i upravljanje korisnicima.
    
-   **Lokalnu bazu** (Realm) na Android uređaju za keširanje sadržaja i brži pristup pri interaktivnom korišćenju aplikacije.
    

Cilj ovog pristupa je da se smanji broj poziva prema serveru, ubrza rad aplikacije, optimizuje korisničko iskustvo i obezbedi stabilan prikaz sadržaja i u uslovima ograničene mrežne dostupnosti.



##  Struktura podataka

Aplikacija koristi isti model podataka na klijentu i serveru, koji uključuje sledeće ključne entitete:


Svi podaci se prenose u formatu **JSON**, koristeći serijalizaciju Kotlin data klasa, kako bi razmena bila što jednostavnija između klijenta i servera.


##  Tok podataka

1.   Korisnik pokreće igru i generiše se novi slučaj
    
2.  Klijent šalje zahtev serveru putem Retrofit-a
    
3.  Server formira prompt i šalje ga Gemini Pro modelu
    
4. AI vraća priču, koja se parsira i čuva u MySQL bazi
    
5.  Server vraća JSON podatke klijentu
    
6.  Aplikacija podatke upisuje u Realm bazu (lokalni keš)
    

## 🗃️ Globalna baza – MySQL Workbench

MySQL se koristi kao **relaciona baza na serverskoj strani**. U njoj se čuvaju trajni podaci, uključujući:

-   Sve generisane priče (njihovi elementi)
    
-   Korisničke naloge, kredencijale i podatke o sesijama
    

### Glavne tabele:


##  Lokalna baza – Realm

**Realm** se koristi kao **lokalna NoSQL baza** u Android aplikaciji. Njena uloga je keširanje sadržaja radi:

-   Bržeg učitavanja priča bez ponovnih upita serveru
    
-   Offline rada korisnika
    
-   Privremenog čuvanja korisničkog toka igre
    

Realm koristi iste modele, ali samo one koje su relevantne za prikaz sadržaja. Nema tabele za korisnike, jer se podaci o prijavi i tokenima čuvaju posebno.

## 🔄 Modeli i JSON struktura

Podaci koje server vraća Android aplikaciji formirani su kao JSON objekti. Primer za jednu scenu:

```json


```




# 🕵️‍♀️WHODUNIT - detektivska igra sa AI podrškom

### Android aplikacija sa Ktor serverom i AI generisanjem priča

##  Opis projekta

Ovaj projekat predstavlja mobilnu aplikaciju koja kombinuje elemente detektivske igre sa tehnologijama veštačke inteligencije. Kroz aplikaciju, korisnik učestvuje u rešavanju AI-generisanih slučajeva i prikuplja tragove u cilju rešavanja zagonetke.

## Arhitektura sistema

Aplikacija je podeljena u tri osnovna dela:

```
+-----------------+         HTTP + JSON         +-----------------+
|  Android App    | <-------------------------> |     Ktor API    |
| (Jetpack Compose)                             |                 |
+-----------------+                             +--------+--------+
                                                       |
                                                   Gemini Pro API
                                                       |
                                                 +-----v-----+
                                                 | AI Prompt |
                                                 +-----------+

```

### Komponente:

-   **Android aplikacija** (Jetpack Compose + Realm): prikazuje priče, upravlja interfejsom, kešira podatke.
    
-   **Ktor server** (Kotlin + MySQL): prima zahteve, obrađuje logiku igre, komunicira s AI, vraća rezultate.
    
-   **Gemini Pro AI**: generiše detektivske priče u realnom vremenu.
    
-   **MySQL + Realm**: čuvaju podatke globalno (server) i lokalno (klijent) radi performansi.
    

----------

## ⚙️ Korišćene tehnologije

| Tehnologija | Svrha |
|--|--|
| Kotlin | Glavni programski jezik |
| Jetpack Compose | UI framework za Android |
| Ktor | Server-side framework |
| Retrofit | HTTP klijent za Android |
| Realm | Lokalna baza podataka  |
| MySQL Workbench |  Relaciona baza (server)|
| Gemini Pro | Generisanje AI sadržaja |
| Docker | Testiranje  |
| Kover | Pokrivenost koda |

##  Pokretanje projekta

### 1. Backend (Ktor server)

```kotlin
./gradlew run
```

### 2. Android aplikacija
Pokretanje aplikacije u emulatoru ili fizičkom uređaju
    
### 3. Testovi i pokrivenost koda na serveru
```kotlin
./gradlew koverHtmlReport
```

### 4. Dokumentacija na serveru
```kotlin
./gradlew dokkaHtml
```

##  Upravljanje podacima

Aplikacija koristi:

-   **MySQL**  bazu– serverska baza za trajno čuvanje priča, korisnika...
    
-   **Realm** – lokalna android baza za keširanje sadržaja radi bržeg prikaza
    

Za više informacija, pogledati: [`/readme/README ZA BAZU.md`]



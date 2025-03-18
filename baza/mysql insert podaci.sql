-- Unos podataka u tabelu Korisnik
INSERT INTO `Korisnik` (korisnickoIme, ime, prezime, sifra, email, nacinPrijave, poeni)
VALUES
('user1', 'Marko', 'Marković', 'sifra123', 'marko@example.com', 'Email', 100),
('user2', 'Jovana', 'Jovanović', 'sifra456', 'jovana@example.com', 'Google', 150);

-- Unos podataka u tabelu Zlocin
INSERT INTO `Zlocin` (naziv, opis, idTipZlocina)
VALUES
('Ubistvo u parku', 'Telo je pronađeno u parku, uveče, sa tragovima nasilja.', 1),  -- 'murder' tip
('Nestanak osobe', 'Osoba je nestala pre nekoliko dana, bez traga.', 2),  -- 'disappearance' tip
('Pljačka u banci', 'Pljačka se dogodila u banci, uz pretnju oružjem.', 3);  -- 'robbery' tip

-- Unos podataka u tabelu Dokaz
INSERT INTO `Dokaz` (opis, lokacija, idZlocin)
VALUES
('Otisci prstiju na stolu', 'Banka, radni stol', 3),  -- Povezano sa pljačkom
('Krvi na majici', 'Park, u blizini tela', 1),  -- Povezano sa ubistvom
('Zgazio tragove u snegu', 'Bliži ulaz u kuću nestale osobe', 2);  -- Povezano sa nestankom

-- Unos podataka u tabelu Svedok
INSERT INTO `Svedok` (ime, prezime, iskaz, idZlocin)
VALUES
('Ana', 'Petrović', 'Videla sam osobu u crnom kako beži iz parka.', 1),  -- Svedok ubistva
('Milan', 'Jović', 'Čuo sam pucanj u banci, a potom vidio maskirane pljačkaše.', 3),  -- Svedok pljačke
('Ivana', 'Marković', 'Videla sam osobu koja je trčala prema kući nestale osobe.', 2);  -- Svedok nestanka

-- Unos podataka u tabelu Osumnjicen
INSERT INTO `Osumnjicen` (ime, prezime, alibi, idZlocin)
VALUES
('Nemanja', 'Stojanović', 'Bio je na poslu tokom noći', 1),  -- Osumnjičen za ubistvo
('Luka', 'Petrović', 'Imao je alibi, bio je sa prijateljima', 3),  -- Osumnjičen za pljačku
('Jovan', 'Nikolić', 'Nema potvrdu alibija, bio je u parku', 2);  -- Osumnjičen za nestanak

-- Unos podataka u tabelu Trag
INSERT INTO `Trag` (opis, idDokaz, idOsumnjicen)
VALUES
('Otisci prstiju na stolu u banci', 1, 2),  -- Trag povezan sa pljačkom i osumnjičenim Luku
('Cipele sa blatom pored tela', 2, 1),  -- Trag povezan sa ubistvom i osumnjičenim Nemanjom
('Dugme sa jakne na mestu nestanka', 3, 3);  -- Trag povezan sa nestankom i osumnjičenim Jovanom

-- Unos podataka u tabelu Obdukcija
INSERT INTO `Obdukcija` (uzrokSmrti, zakljucak, idZlocin)
VALUES
('Pucanj u glavu', 'Ubistvo sa predumišljajem', 1),  -- Obdukcija za ubistvo
('Neprirodna smrt, bez traga nasilja', 'Nestala osoba, mogući samoubistvo', 2);  -- Obdukcija za nestanak

-- Unos podataka u tabelu ForenzickiDokaz
INSERT INTO `ForenzickiDokaz` (opis, tip, idZlocin)
VALUES
('Forenzička analiza krvi na mestu zločina', 'Krvi', 1),  -- Forenzika vezana za ubistvo
('Prašina iz obuće pronađena na mestu zločina', 'Prašina', 3);  -- Forenzika vezana za pljačku

-- Unos podataka u tabelu Motiv
INSERT INTO `Motiv` (opis)
VALUES
('Novčana dobit'),  -- Motiv za pljačku
('Osvetnička namera'),  -- Motiv za ubistvo
('Porodični nesuglasice');  -- Motiv za nestanak

-- Unos podataka u tabelu Ucena
INSERT INTO `Ucena` (opis, idOsumnjicen)
VALUES
('Prijetnja smrću ako ne da novac', 2),  -- Ucena prema Luki (pljačka)
('Zahtev za šutiranje novca', 1);  -- Ucena prema Nemanja

-- Unos podataka u tabelu TajnaPorodice
INSERT INTO `TajnaPorodice` (opis, idOsumnjicen)
VALUES
('Pronađena pismo sa sumnjivim informacijama', 3);  -- Tajna povezanaa sa osumnjičenim Jovanom

-- Unos podataka u tabelu Organizacija
INSERT INTO `Organizacija` (naziv, tip)
VALUES
('Banda iz centra grada', 'Banda'),
('Mafija Beograd', 'Mafija');

-- Unos podataka u tabelu ClanOrganizacije
INSERT INTO `ClanOrganizacije` (idOsumnjicen, idOrganizacija)
VALUES
(2, 1),  -- Luka je član bande iz centra grada
(1, 2);  -- Nemanja je član mafije Beograd

-- Unos podataka u tabelu Misija
INSERT INTO `Misija` (naziv, opis, cilj)
VALUES
('Istraga o pljački u banci', 'Istražiti pljačku u banci u centru grada', 'Otkrivenje počinitelja pljačke');

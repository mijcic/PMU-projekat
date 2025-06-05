CREATE DATABASE IF NOT EXISTS whodunit;

-- Korišćenje baze
USE whodunit;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `NapredakIstrage`;
DROP TABLE IF EXISTS `ZabelezeniIzbor`;
DROP TABLE IF EXISTS `Osoba`;
DROP TABLE IF EXISTS `Zrtva`;
DROP TABLE IF EXISTS `Dokaz`;
DROP TABLE IF EXISTS `Svedok`;
DROP TABLE IF EXISTS `Osumnjicen`;
DROP TABLE IF EXISTS `OdnosOsumnjicenZrtva`;
DROP TABLE IF EXISTS `DokazOsumnjicen`;
DROP TABLE IF EXISTS `Alibi`;
DROP TABLE IF EXISTS `Trag`;
DROP TABLE IF EXISTS `Obdukcija`;
DROP TABLE IF EXISTS `ForenzickiDokaz`;
DROP TABLE IF EXISTS `Telefon`;
DROP TABLE IF EXISTS `Ucena`;
DROP TABLE IF EXISTS `TajnaPorodice`;
DROP TABLE IF EXISTS `Organizacija`;
DROP TABLE IF EXISTS `ClanOrganizacije`;
DROP TABLE IF EXISTS `Misija`;
DROP TABLE IF EXISTS `MisijaPoruka`;
DROP TABLE IF EXISTS `OneContact`;
DROP TABLE IF EXISTS `Beleska`;
DROP TABLE IF EXISTS `WhatsAppKontakt`;
DROP TABLE IF EXISTS `WhatsAppPoruka`;
DROP TABLE IF EXISTS `OneCall`;
DROP TABLE IF EXISTS `Gallery`;
DROP TABLE IF EXISTS `ObicnaPoruka`;
DROP TABLE IF EXISTS `Pitanje`;
DROP TABLE IF EXISTS `Odgovor`;
DROP TABLE IF EXISTS `PitanjeIspitivanjeOsumnjicenog`;
DROP TABLE IF EXISTS `Korisnik`;
DROP TABLE IF EXISTS `Zlocin`;
DROP TABLE IF EXISTS `TipZlocina`;
DROP TABLE IF EXISTS `PrijavljeniKorisnik`;
DROP TABLE IF EXISTS `PitanjeIspitivanjeSvedoka`;
DROP TABLE IF EXISTS `Zadatak`;
DROP TABLE IF EXISTS `DokazZadatak`;
DROP TABLE IF EXISTS `IspitivanjeOsumnjicenogZadatak`;
DROP TABLE IF EXISTS `IspitivanjeSvedokaZadatak`;
DROP TABLE IF EXISTS `TelefonZadatak`;
DROP TABLE IF EXISTS `ForenzickiDokazZadatak`;
-- DROP TABLE IF EXISTS `PorukeZadatak`;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `Korisnik` (
    idKorisnik INT AUTO_INCREMENT PRIMARY KEY,
    korisnickoIme VARCHAR(255) NOT NULL UNIQUE,
    ime VARCHAR(100) NOT NULL,
    prezime VARCHAR(100) NOT NULL,
    sifra VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    nacinPrijave VARCHAR(50) NOT NULL,  -- Na primer: 'Google', 'Facebook', 'Email'
    poeni INT DEFAULT 0,
    poslednjaAktivnost DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabela za tipove zločina (statični podaci)
CREATE TABLE `TipZlocina` (
    `idTipZlocina` INT AUTO_INCREMENT PRIMARY KEY,
    `naziv` VARCHAR(255) NOT NULL
);

-- Tabela za zločine (globalni podaci)
CREATE TABLE Zlocin (
    idZlocin INT AUTO_INCREMENT PRIMARY KEY,
    tipZlocinaId INT NOT NULL,
    naziv VARCHAR(255) NOT NULL,
    datum DATETIME DEFAULT CURRENT_TIMESTAMP,
    mesto VARCHAR(255) NOT NULL,
    opis TEXT NOT NULL,
    statusS ENUM('u_istrazi', 'resen') NOT NULL,
    FOREIGN KEY (tipZlocinaId) REFERENCES TipZlocina(idTipZlocina)
);

CREATE TABLE UsedZlocin(
	idUsedZlocin INT AUTO_INCREMENT PRIMARY KEY,
	zlocinId INT NOT NULL,
    used BOOLEAN NOT NULL,
    FOREIGN KEY (zlocinId) REFERENCES Zlocin(idZlocin)
);



-- Insertovanje osnovnih vrednosti u tabelu TipZlocina
INSERT INTO `TipZlocina` (`naziv`) VALUES
('murder'),
('disappearance'),
('robbery'),
('kidnappingAndBlackmail'),
('FamilySecrets'),
('Abuse'),
('GangConflicts'),
('Corruption'),
('MysteriousSymptoms'),
('MafiaCrimesOfPassion'),
('FalseIdentities'),
('CultsAndSecrets');

CREATE TABLE Osoba (
    idOsoba INT AUTO_INCREMENT PRIMARY KEY,  -- Primarni ključ
    ime VARCHAR(50) NOT NULL,          
    kontakt VARCHAR(255) NOT NULL,               
    datum DATETIME DEFAULT CURRENT_TIMESTAMP,
    zanimanje VARCHAR(100) NOT NULL,
    pol VARCHAR(50) NOT NULL,       
    zlocinId INT NOT NULL,                   -- Spoljašnji ključ na Zlocin
    FOREIGN KEY (zlocinId) REFERENCES Zlocin(idZlocin)  -- Veza sa Zlocin tabelom
);

CREATE TABLE Zrtva (
    idZrtva INT AUTO_INCREMENT PRIMARY KEY,  -- Primarni ključ
    tipZrtve VARCHAR(50) NOT NULL,           -- Tip žrtve (osoba, objekat, fenomen...)
    detalji VARCHAR(150) NOT NULL,            -- Detalji o žrtvi
    statusZrtva VARCHAR(50) NOT NULL,        -- Status žrtve
    zlocinId INT NOT NULL,                   -- Spoljašnji ključ na Zlocin
    osobaId INT NOT NULL,
    FOREIGN KEY (zlocinId) REFERENCES Zlocin(idZlocin),  -- Veza sa Zlocin tabelom
    FOREIGN KEY (osobaId) REFERENCES Osoba(idOsoba) 
);

CREATE TABLE Dokaz (
    idDokaz INT AUTO_INCREMENT PRIMARY KEY,
    tipDokaza ENUM('fizicki', 'digitalni', 'svedok') NOT NULL,
    opis TEXT NOT NULL,
    zlocinId INT NOT NULL,
    zrtvaId INT NOT NULL,
    statusS INT NOT NULL,
    FOREIGN KEY (zlocinId) REFERENCES Zlocin(idZlocin),
    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
);


CREATE TABLE Svedok (
    idSvedok INT AUTO_INCREMENT PRIMARY KEY,
    izjava VARCHAR(255) NOT NULL,
    statusSvedok ENUM('aktivno', 'zasticen', 'nesaradnja') NOT NULL,
    statusIspitan INT NOT NULL,
    zlocinId INT NOT NULL,
    osobaId INT NOT NULL,
    FOREIGN KEY (zlocinId) REFERENCES Zlocin(idZlocin),
    FOREIGN KEY (osobaId) REFERENCES Osoba(idOsoba)
);

CREATE TABLE Motiv (
    idMotiv INT AUTO_INCREMENT PRIMARY KEY,
    opis TEXT NOT NULL 
);

CREATE TABLE Osumnjicen (
    idOsumnjicen INT AUTO_INCREMENT PRIMARY KEY,
    statusS INT NOT NULL,
    tipOsumnjicen ENUM('pojedinac', 'organizacija') NOT NULL, 
	motiv INT NOT NULL,
    zlocinId INT NOT NULL,
    kriv INT NOT NULL,
    osobaId INT NOT NULL,
    FOREIGN KEY (zlocinId) REFERENCES Zlocin(idZlocin),
    FOREIGN KEY (osobaId) REFERENCES Osoba(idOsoba),
    FOREIGN KEY (motiv) REFERENCES Motiv(idMotiv)
);

CREATE TABLE OdnosOsumnjicenZrtva (
    idOdnos INT AUTO_INCREMENT PRIMARY KEY,
    osumnjicenId INT NOT NULL,
    zrtvaId INT NOT NULL,
    tipOdnosa ENUM('poslovni', 'licni','porodicni','rivalski','slucajni','ljubavni') NOT NULL, 
    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva),
    FOREIGN KEY (osumnjicenId) REFERENCES Osumnjicen(idOsumnjicen)
);


CREATE TABLE DokazOsumnjicen (
    idDokazOsumnjicen INT AUTO_INCREMENT PRIMARY KEY,
    dokazId INT NOT NULL,
    osumnjicenId INT NOT NULL,
    FOREIGN KEY (dokazId) REFERENCES Dokaz(idDokaz),
    FOREIGN KEY (osumnjicenId) REFERENCES Osumnjicen(idOsumnjicen)
);

CREATE TABLE Alibi (
    idAlibi INT AUTO_INCREMENT PRIMARY KEY,
    osumnjicenId INT NOT NULL,
    svedokId INT,
    opis VARCHAR(255) NOT NULL,
    statusAlibija ENUM('potvrdjen', 'lazan', 'nepotvrdjen') NOT NULL, 
    FOREIGN KEY (osumnjicenId) REFERENCES Osumnjicen(idOsumnjicen),
    FOREIGN KEY (svedokId) REFERENCES Svedok(idSvedok)
);

CREATE TABLE Trag (
    idTrag INT AUTO_INCREMENT PRIMARY KEY,
    opis TEXT NOT NULL,
    idDokaz INT NOT NULL,
    idOsumnjicen INT NOT NULL,
    FOREIGN KEY (idDokaz) REFERENCES Dokaz(idDokaz),
    FOREIGN KEY (idOsumnjicen) REFERENCES Osumnjicen(idOsumnjicen)
);

CREATE TABLE Obdukcija (
    idObdukcija INT AUTO_INCREMENT PRIMARY KEY,
    izvestaj TEXT NOT NULL,
    datum DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    uzrokSmrti VARCHAR(255) NOT NULL,
    zrtvaId INT NOT NULL,
    informacije VARCHAR(255) NOT NULL,
    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
);

CREATE TABLE ForenzickiDokaz (
    idForenzickiDokaz INT AUTO_INCREMENT PRIMARY KEY,
    tipForenzickiDokaz ENUM('otisak', 'DNK', 'dokument') NOT NULL,
    opis TEXT NOT NULL,
	statusS INT NOT NULL,
    zrtvaId INT NOT NULL,
    veza VARCHAR(255) NOT NULL,
    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
);

CREATE TABLE Telefon (
    idTelefon INT AUTO_INCREMENT PRIMARY KEY,
    model VARCHAR(50) NOT NULL,
    os ENUM('IOS', 'Android') NOT NULL,
	zrtvaId INT NOT NULL,
    sifra VARCHAR(100) NOT NULL,
    informacije VARCHAR(255) NOT NULL,
    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
);

CREATE TABLE Ucena (
    idUcena INT AUTO_INCREMENT PRIMARY KEY,
    opis TEXT NOT NULL,
    idOsumnjicen INT NOT NULL,
    FOREIGN KEY (idOsumnjicen) REFERENCES Osumnjicen(idOsumnjicen)
);

CREATE TABLE TajnaPorodice (
    idTajna INT AUTO_INCREMENT PRIMARY KEY,
    opis TEXT NOT NULL,
    idOsumnjicen INT NOT NULL,
    FOREIGN KEY (idOsumnjicen) REFERENCES Osumnjicen(idOsumnjicen)
);

CREATE TABLE Organizacija (
    idOrganizacija INT AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(255) NOT NULL UNIQUE,
    tip VARCHAR(100) NOT NULL CHECK (tip IN ('Banda', 'Mafija', 'Kult'))
);

CREATE TABLE ClanOrganizacije (
    idClan INT AUTO_INCREMENT PRIMARY KEY,
    idOsumnjicen INT NOT NULL,
    idOrganizacija INT NOT NULL,
    FOREIGN KEY (idOsumnjicen) REFERENCES Osumnjicen(idOsumnjicen),
    FOREIGN KEY (idOrganizacija) REFERENCES Organizacija(idOrganizacija)
);

CREATE TABLE Misija (
    idMisija INT AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(255) NOT NULL,
    opis TEXT NOT NULL,
    cilj TEXT NOT NULL
);

CREATE TABLE MisijaPoruka (
    idMisija INT AUTO_INCREMENT PRIMARY KEY,
    zlocinId INT NOT NULL,
    naziv VARCHAR(255) NOT NULL,
    statusS INT NOT NULL,
    posiljalac VARCHAR(100) NOT NULL,
    poruka TEXT NOT NULL,
    FOREIGN KEY (zlocinId) REFERENCES Zlocin(idZlocin)
);

CREATE TABLE NapredakIstrage (
    idNapredak INT AUTO_INCREMENT PRIMARY KEY,
    idKorisnik INT NOT NULL,
    idZlocin INT NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('U toku', 'Završeno', 'Neuspelo')),
    datumPromene DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idKorisnik) REFERENCES Korisnik(idKorisnik),
    FOREIGN KEY (idZlocin) REFERENCES Zlocin(idZlocin)
);

CREATE TABLE ZabelezeniIzbor (
    idIzbor INT AUTO_INCREMENT PRIMARY KEY,
    idKorisnik INT NOT NULL,
    idZlocin INT NOT NULL,
    opisIzbora TEXT NOT NULL,
    datumIzbora DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idKorisnik) REFERENCES Korisnik(idKorisnik),
    FOREIGN KEY (idZlocin) REFERENCES Zlocin(idZlocin)
);

CREATE TABLE OneContact (
    idOneContact INT AUTO_INCREMENT PRIMARY KEY,
    zlocinId INT NOT NULL,
    ime VARCHAR(100) NOT NULL,
    broj VARCHAR(100) NOT NULL,
    slika INT,
    FOREIGN KEY (zlocinId) REFERENCES Zlocin(idZlocin)
);

CREATE TABLE Beleska (
    idBeleska INT AUTO_INCREMENT PRIMARY KEY,
    zlocinId INT NOT NULL,
    tekst VARCHAR(1000) NOT NULL,
	datum DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (zlocinId) REFERENCES Zlocin(idZlocin)
);

CREATE TABLE WhatsAppKontakt (
	idWhatsAppKontakt INT AUTO_INCREMENT PRIMARY KEY,
    zlocinId INT NOT NULL,
    ime VARCHAR(100) NOT NULL,
    broj VARCHAR(100) NOT NULL,
    slika INT,
    FOREIGN KEY (zlocinId) REFERENCES Zlocin(idZlocin)
);

CREATE TABLE WhatsAppPoruka (
	idWhatsAppPoruka INT AUTO_INCREMENT PRIMARY KEY,
    kontaktKoSalje INT NOT NULL,
    kontaktKomeSalje INT NOT NULL,
    tekst VARCHAR(1000) NOT NULL,
    datum DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    procitana TINYINT(0),
    FOREIGN KEY (kontaktKoSalje) REFERENCES WhatsAppKontakt(idWhatsAppKontakt),
    FOREIGN KEY (kontaktKomeSalje) REFERENCES WhatsAppKontakt(idWhatsAppKontakt)
);

CREATE TABLE OneCall (
	idOneCall INT AUTO_INCREMENT PRIMARY KEY,
    kontakt INT NOT NULL,
    datum DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    propusten TINYINT(0),
    dolazni TINYINT(0),
    zrtvaId INT NOT NULL,
    FOREIGN KEY (kontakt) REFERENCES OneContact(idOneContact),
    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
);

CREATE TABLE Gallery (
	idPhoto INT AUTO_INCREMENT PRIMARY KEY,
    zlocinId INT NOT NULL,
    slika INT,
    datum DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    mesto VARCHAR(100) NOT NULL,
    FOREIGN KEY (zlocinId) REFERENCES Zlocin(idZlocin)
);

CREATE TABLE ObicnaPoruka (
	idObicnaPoruka INT AUTO_INCREMENT PRIMARY KEY,
    kontaktKoSalje INT NOT NULL,
    kontaktKomeSalje INT NOT NULL,
    tekst VARCHAR(1000) NOT NULL,
    datum DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    procitana TINYINT(0),
    FOREIGN KEY (kontaktKoSalje) REFERENCES OneContact(idOneContact),
    FOREIGN KEY (kontaktKomeSalje) REFERENCES OneContact(idOneContact)
);

-- CREATE TABLE OdnosOsumnjicenZrtva (
--     idOdnos INT AUTO_INCREMENT PRIMARY KEY,
--     osumnjicenId INT,
--     zrtvaId INT,
--     tipOdnosa VARCHAR(1000) NOT NULL,
--     FOREIGN KEY (osumnjicenId) REFERENCES Osumnjicen(idOsumnjicen),
--     FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
-- );

CREATE TABLE PrijavljeniKorisnik (
	idKorisnik INT AUTO_INCREMENT PRIMARY KEY,
    korisnickoIme VARCHAR(100) NOT NULL,
    sifra VARCHAR(100) NOT NULL
);

CREATE TABLE Pitanje (
	idPitanje INT AUTO_INCREMENT PRIMARY KEY,
    zlocinId INT,
    tekst VARCHAR(1000) NOT NULL
);

CREATE TABLE Odgovor (
	idOdogovor INT AUTO_INCREMENT PRIMARY KEY,
    pitanjeId INT,
    tekstOdgovora VARCHAR(1000) NOT NULL,
    tacan TINYINT(0),
    bodovi INT NOT NULL,
    FOREIGN KEY (pitanjeId) REFERENCES Pitanje(idPitanje)
);

CREATE TABLE PitanjeIspitivanjeOsumnjicenog (
	idPitanjeIspitivanjeOsumnjicenog INT AUTO_INCREMENT PRIMARY KEY,
    kategorija ENUM('opsta', 'alibi', 'dokaz', 'kontradikcija') NOT NULL,
    tekst VARCHAR(1000) NOT NULL,
    odgovor VARCHAR(1000) NOT NULL,
    komentar VARCHAR(1000) NOT NULL,
    osumnjicenId INT NOT NULL,
    FOREIGN KEY (osumnjicenId) REFERENCES Osumnjicen(idOsumnjicen)
);

CREATE TABLE PitanjeIspitivanjeSvedoka (
	idPitanjeIspitivanjeSvedoka INT AUTO_INCREMENT PRIMARY KEY,
    tekst VARCHAR(1000) NOT NULL,
    odgovor VARCHAR(1000) NOT NULL,
    svedokId INT NOT NULL,
    nextPitanje INT NOT NULL,
	FOREIGN KEY (svedokId) REFERENCES Svedok(idSvedok)
);

CREATE TABLE Zadatak (
	idZadatak INT AUTO_INCREMENT PRIMARY KEY,
    tekst VARCHAR(1000) NOT NULL,
    korak VARCHAR(1000) NOT NULL,
    uradjen TINYINT(0),
    nextZadatak INT,
    zlocinId INT NOT NULL,
	FOREIGN KEY (nextZadatak) REFERENCES Zadatak(idZadatak)
);

CREATE TABLE DokazZadatak (
	idDokazZadatak INT AUTO_INCREMENT PRIMARY KEY,
    tekst VARCHAR(1000) NOT NULL,
    dokazId INT NOT NULL,
    uradjen TINYINT(0),
    zadatakId INT NOT NULL,
	FOREIGN KEY (dokazId) REFERENCES Dokaz(idDokaz),
    FOREIGN KEY (zadatakId) REFERENCES Zadatak(idZadatak)
);

CREATE TABLE IspitivanjeOsumnjicenogZadatak (
	idIspitivanjeOsumnjicenogZadatak INT AUTO_INCREMENT PRIMARY KEY,
    osumnjicenId INT NOT NULL,
    zadatakId INT NOT NULL,
    uradjen TINYINT(0),
	FOREIGN KEY (osumnjicenId) REFERENCES Osumnjicen(idOsumnjicen),
    FOREIGN KEY (zadatakId) REFERENCES Zadatak(idZadatak)
);

CREATE TABLE IspitivanjeSvedokaZadatak (
	idIspitivanjeSvedokaZadatak INT AUTO_INCREMENT PRIMARY KEY,
    svedokId INT NOT NULL,
    zadatakId INT NOT NULL,
    uradjen TINYINT(0),
    FOREIGN KEY (svedokId) REFERENCES Svedok(idSvedok),
    FOREIGN KEY (zadatakId) REFERENCES Zadatak(idZadatak)
);

CREATE TABLE TelefonZadatak (
	idTelefonZadatak INT AUTO_INCREMENT PRIMARY KEY,
    telefonId INT NOT NULL,
    zadatakId INT NOT NULL,
    uradjen TINYINT(0),
    FOREIGN KEY (telefonId) REFERENCES Telefon(idTelefon),
    FOREIGN KEY (zadatakId) REFERENCES Zadatak(idZadatak)
);

CREATE TABLE ForenzickiDokazZadatak (
	idForenzickiDokazZadatak INT AUTO_INCREMENT PRIMARY KEY,
    tekst VARCHAR(1000) NOT NULL,
    forenzickiDokazId INT NOT NULL,
    uradjen TINYINT(0),
    zadatakId INT NOT NULL,
    FOREIGN KEY (forenzickiDokazId) REFERENCES ForenzickiDokaz(idForenzickiDokaz),
    FOREIGN KEY (zadatakId) REFERENCES Zadatak(idZadatak)
);

-- CREATE TABLE PorukeZadatak (
-- 	idPorukeZadatak INT AUTO_INCREMENT PRIMARY KEY,
--     porukeId INT NOT NULL,
--     zadatakId INT NOT NULL,
--     uradjen TINYINT(0),
--     FOREIGN KEY (forenzickiDokazId) REFERENCES ForenzickiDokaz(idForenzickiDokaz),
--     FOREIGN KEY (zadatakId) REFERENCES Zadatak(idZadatak)
-- );

DROP TABLE IF EXISTS `Kontakt`;
-- Tabela za kontakte
CREATE TABLE Kontakt (
    idKontakt INT AUTO_INCREMENT PRIMARY KEY,
    ime varchar(255) NOT NULL,
    broj varchar(50) NOT NULL,
    statusS INT NOT NULL,
    zrtvaId INT NOT NULL,
    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
);

DROP TABLE IF EXISTS `Poruke`;
-- Tabela za poruke
CREATE TABLE Poruke (
    idPoruke INT AUTO_INCREMENT PRIMARY KEY,
    tipPoruke ENUM('SMS', 'WhatsApp', 'email') NOT NULL,
    sadrzaj varchar(255) NOT NULL,
    datumVreme DATETIME DEFAULT CURRENT_TIMESTAMP,
    zrtvaId INT NOT NULL,
    posiljalacId INT NOT NULL,
    statusPoruke ENUM('sent', 'read', 'delete') NOT NULL,
    sifrovana boolean NOT NULL,
    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva),
    FOREIGN KEY (posiljalacId) REFERENCES Kontakt(idKontakt)
);

DROP TABLE IF EXISTS `Pozivi`;
-- Tabela za pozive
CREATE TABLE Pozivi (
    idPoziv INT AUTO_INCREMENT PRIMARY KEY,
    tip INT NOT NULL,
    broj varchar(100) NOT NULL,
    datumVreme DATETIME DEFAULT CURRENT_TIMESTAMP,
    zrtvaId INT NOT NULL,
    statusS INT NOT NULL,
    kontaktId INT NOT NULL,
    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva),
    FOREIGN KEY (kontaktId) REFERENCES Kontakt(idKontakt)
);

DROP TABLE IF EXISTS `Galerija`;
-- Tabela za galeriju
CREATE TABLE Galerija (
    idGalerija INT AUTO_INCREMENT PRIMARY KEY,
    tip INT NOT NULL,
    putanja varchar(100) NOT NULL,
    zrtvaId INT NOT NULL,
	datumVreme DATETIME DEFAULT CURRENT_TIMESTAMP,
    lokacija varchar(100) NOT NULL,
    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
);

DROP TABLE IF EXISTS `Aplikacija`;
-- Tabela za aplikaciju
CREATE TABLE Aplikacija (
    idAplikacije INT AUTO_INCREMENT PRIMARY KEY,
    naziv varchar(100) NOT NULL,
    tip INT NOT NULL,
    zrtvaId INT NOT NULL,
    aktivna boolean NOT NULL,
    informacije varchar(100) NOT NULL,
    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
);

DROP TABLE IF EXISTS `Trag`;
-- Tabela za trag
CREATE TABLE Trag (
    idTrag INT AUTO_INCREMENT PRIMARY KEY,
    forenzickiDokazId INT NOT NULL,
    osumnjicenId INT NOT NULL,
    FOREIGN KEY (forenzickiDokazId) REFERENCES ForenzickiDokaz(idForenzickiDokaz),
    FOREIGN KEY (osumnjicenId) REFERENCES Osumnjicen(idOsumnjicen)
);

DROP TABLE IF EXISTS `DokazOsumnjicen`;
-- Tabela za dokazOsumnjicen
CREATE TABLE DokazOsumnjicen (
    idDokazOsumnjicen INT AUTO_INCREMENT PRIMARY KEY,
    dokazId INT NOT NULL,
    osumnjicenId INT NOT NULL,
    FOREIGN KEY (dokazId) REFERENCES Dokaz(idDokaz),
    FOREIGN KEY (osumnjicenId) REFERENCES Osumnjicen(idOsumnjicen)
);


DROP TABLE IF EXISTS `Pacijent`;
-- Tabela za Pacijent
CREATE TABLE Pacijent (
    idPacijent INT AUTO_INCREMENT PRIMARY KEY,
    simptomi varchar(255) NOT NULL,
    statusPacijenta ENUM('ziva', 'mrtva') NOT NULL,
    datumPrijave DATETIME DEFAULT CURRENT_TIMESTAMP,
    prijavio INT NOT NULL,
    zlocinId INT NOT NULL,
    zrtvaId INT NOT NULL,
    FOREIGN KEY (prijavio) REFERENCES Osoba(idOsoba),
    FOREIGN KEY (zlocinId) REFERENCES Zlocin(idZlocin),
    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
);


DROP TABLE IF EXISTS `MedicinskiIzvestaj`;
-- Tabela za MedicinskiIzvestaj
CREATE TABLE MedicinskiIzvestaj (
    idMedicinskiIzvestaj INT AUTO_INCREMENT PRIMARY KEY,
    rezime varchar(255) NOT NULL,
    CTnalaz varchar(255) NOT NULL,
    MRInalaz varchar(255) NOT NULL,
    krvnaSlika varchar(255) NOT NULL,
    toksikoloskeAnalize varchar(255) NOT NULL,
    zakljucak varchar(255) NOT NULL,
    pacijentId INT NOT NULL,
    FOREIGN KEY (pacijentId) REFERENCES Pacijent(idPacijent)
);

DROP TABLE IF EXISTS `IzjavaZaPacijenta`;
-- Tabela za IzjavaZaPacijenta
CREATE TABLE IzjavaZaPacijenta (
    idIzjavaZaPacijenta INT AUTO_INCREMENT PRIMARY KEY,
    izjava varchar(255) NOT NULL,
    pacijentId INT NOT NULL,
    osobaId INT NOT NULL,
    FOREIGN KEY (pacijentId) REFERENCES Pacijent(idPacijent),
    FOREIGN KEY (osobaId) REFERENCES Osoba(idOsoba)
);

DROP TABLE IF EXISTS `LekarskiTest`;
-- Tabela za LekarskiTest
CREATE TABLE LekarskiTest (
    idLekarskiTest INT AUTO_INCREMENT PRIMARY KEY,
    pacijentId INT NOT NULL,
    izjava varchar(255) NOT NULL,
    FOREIGN KEY (pacijentId) REFERENCES Pacijent(idPacijent)
);

DROP TABLE IF EXISTS `LokacijeIstrage`;
-- Tabela za LokacijeIstrage
CREATE TABLE LokacijeIstrage (
    idLokacijeIstrage INT AUTO_INCREMENT PRIMARY KEY,
    mesto varchar(100) NOT NULL,
    naziv varchar(100) NOT NULL,
    opis varchar(100) NOT NULL,
    zlocinId INT NOT NULL,
    geoTackaALatitude DOUBLE NOT NULL,
    geoTackaALongitude DOUBLE NOT NULL,
    FOREIGN KEY (zlocinId) REFERENCES Zlocin(idZlocin)
);
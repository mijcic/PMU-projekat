CREATE DATABASE IF NOT EXISTS whodunit;

-- Korišćenje baze
USE whodunit;

DROP TABLE IF EXISTS `Korisnik`;

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
DROP TABLE IF EXISTS `Zlocin`;
DROP TABLE IF EXISTS `TipZlocina`;

-- Tabela za tipove zločina (statični podaci)
CREATE TABLE `TipZlocina` (
    `idTipZlocina` INT AUTO_INCREMENT PRIMARY KEY,
    `naziv` VARCHAR(255) NOT NULL
);

-- Tabela za zločine (globalni podaci)
CREATE TABLE Zlocin (
    idZlocin INT AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(255) NOT NULL,
    opis TEXT NOT NULL,
    idTipZlocina INT NOT NULL,
    datum DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idTipZlocina) REFERENCES TipZlocina(idTipZlocina)
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


DROP TABLE IF EXISTS `Dokaz`;
-- Tabela za dokaze (globalni podaci)
CREATE TABLE Dokaz (
    idDokaz INT AUTO_INCREMENT PRIMARY KEY,
    opis TEXT NOT NULL,
    lokacija VARCHAR(255) NOT NULL,
    idZlocin INT NOT NULL,
    FOREIGN KEY (idZlocin) REFERENCES Zlocin(idZlocin)
);

DROP TABLE IF EXISTS `Svedok`;
-- Tabela za svedoke (globalni podaci)
CREATE TABLE Svedok (
    idSvedok INT AUTO_INCREMENT PRIMARY KEY,
    ime VARCHAR(255) NOT NULL,
    prezime VARCHAR(255) NOT NULL,
    iskaz TEXT NOT NULL,
    idZlocin INT NOT NULL,
    FOREIGN KEY (idZlocin) REFERENCES Zlocin(idZlocin)
);

DROP TABLE IF EXISTS `Osumnjicen`;
-- Tabela za osumnjičene (globalni podaci)
CREATE TABLE Osumnjicen (
    idOsumnjicen INT AUTO_INCREMENT PRIMARY KEY,
    ime VARCHAR(255) NOT NULL,
    prezime VARCHAR(255) NOT NULL,
    alibi TEXT,
    idZlocin INT NOT NULL,
    FOREIGN KEY (idZlocin) REFERENCES Zlocin(idZlocin)
);

DROP TABLE IF EXISTS `Trag`;
-- Tabela za tragove (globalni podaci)
CREATE TABLE Trag (
    idTrag INT AUTO_INCREMENT PRIMARY KEY,
    opis TEXT NOT NULL,
    idDokaz INT NOT NULL,
    idOsumnjicen INT NOT NULL,
    FOREIGN KEY (idDokaz) REFERENCES Dokaz(idDokaz),
    FOREIGN KEY (idOsumnjicen) REFERENCES Osumnjicen(idOsumnjicen)
);

DROP TABLE IF EXISTS `Obdukcija`;
-- Tabela za obdukcije (globalni podaci)
CREATE TABLE Obdukcija (
    idObdukcija INT AUTO_INCREMENT PRIMARY KEY,
    uzrokSmrti TEXT NOT NULL,
    zakljucak TEXT NOT NULL,
    idZlocin INT NOT NULL,
    FOREIGN KEY (idZlocin) REFERENCES Zlocin(idZlocin)
);

DROP TABLE IF EXISTS `ForenzickiDokaz`;
-- Tabela za forenzičke dokaze (globalni podaci)
CREATE TABLE ForenzickiDokaz (
    idForenzickiDokaz INT AUTO_INCREMENT PRIMARY KEY,
    opis TEXT NOT NULL,
    tip VARCHAR(100) NOT NULL,
    idZlocin INT NOT NULL,
    FOREIGN KEY (idZlocin) REFERENCES Zlocin(idZlocin)
);

DROP TABLE IF EXISTS `Motiv`;
-- Tabela za motive (statični podaci)
CREATE TABLE Motiv (
    idMotiv INT AUTO_INCREMENT PRIMARY KEY,
    opis TEXT NOT NULL 
);

DROP TABLE IF EXISTS `Ucena`;
-- Tabela za ucene (globalni podaci)
CREATE TABLE Ucena (
    idUcena INT AUTO_INCREMENT PRIMARY KEY,
    opis TEXT NOT NULL,
    idOsumnjicen INT NOT NULL,
    FOREIGN KEY (idOsumnjicen) REFERENCES Osumnjicen(idOsumnjicen)
);

DROP TABLE IF EXISTS `TajnaPorodice`;
-- Tabela za tajne porodice (globalni podaci)
CREATE TABLE TajnaPorodice (
    idTajna INT AUTO_INCREMENT PRIMARY KEY,
    opis TEXT NOT NULL,
    idOsumnjicen INT NOT NULL,
    FOREIGN KEY (idOsumnjicen) REFERENCES Osumnjicen(idOsumnjicen)
);

DROP TABLE IF EXISTS `Organizacija`;
-- Tabela za bande, mafije i kultove (globalni podaci)
CREATE TABLE Organizacija (
    idOrganizacija INT AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(255) NOT NULL UNIQUE,
    tip VARCHAR(100) NOT NULL CHECK (tip IN ('Banda', 'Mafija', 'Kult'))
);


DROP TABLE IF EXISTS `ClanOrganizacije`;
-- Tabela za povezivanje osumnjičenih sa organizacijama
CREATE TABLE ClanOrganizacije (
    idClan INT AUTO_INCREMENT PRIMARY KEY,
    idOsumnjicen INT NOT NULL,
    idOrganizacija INT NOT NULL,
    FOREIGN KEY (idOsumnjicen) REFERENCES Osumnjicen(idOsumnjicen),
    FOREIGN KEY (idOrganizacija) REFERENCES Organizacija(idOrganizacija)
);

DROP TABLE IF EXISTS `Misija`;
-- Tabela za misije (globalni podaci)
CREATE TABLE Misija (
    idMisija INT AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(255) NOT NULL,
    opis TEXT NOT NULL,
    cilj TEXT NOT NULL
);

DROP TABLE IF EXISTS `NapredakIstrage`;
-- Tabela za napredak istrage po igračima (sinhronizacija globalna)
CREATE TABLE NapredakIstrage (
    idNapredak INT AUTO_INCREMENT PRIMARY KEY,
    idKorisnik INT NOT NULL,
    idZlocin INT NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('U toku', 'Završeno', 'Neuspelo')),
    datumPromene DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idKorisnik) REFERENCES Korisnik(idKorisnik),
    FOREIGN KEY (idZlocin) REFERENCES Zlocin(idZlocin)
);

DROP TABLE IF EXISTS `ZabelezeniIzbor`;
-- Tabela za beleženje izbora igrača (sinhronizacija globalna)
CREATE TABLE ZabelezeniIzbor (
    idIzbor INT AUTO_INCREMENT PRIMARY KEY,
    idKorisnik INT NOT NULL,
    idZlocin INT NOT NULL,
    opisIzbora TEXT NOT NULL,
    datumIzbora DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idKorisnik) REFERENCES Korisnik(idKorisnik),
    FOREIGN KEY (idZlocin) REFERENCES Zlocin(idZlocin)
);
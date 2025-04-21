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
    tipZlocinaId INT NOT NULL,
    naziv VARCHAR(255) NOT NULL,
    datum DATETIME DEFAULT CURRENT_TIMESTAMP,
    mesto VARCHAR(255) NOT NULL,
    opis TEXT NOT NULL,
    statusS ENUM('u_istrazi', 'resen') NOT NULL,
    FOREIGN KEY (tipZlocinaId) REFERENCES TipZlocina(idTipZlocina)
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

DROP TABLE IF EXISTS `Osoba`;
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

DROP TABLE IF EXISTS `Zrtva`;
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

DROP TABLE IF EXISTS `Dokaz`;
-- Tabela za dokaze (globalni podaci)
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

DROP TABLE IF EXISTS `Svedok`;
-- Tabela za svedoke (globalni podaci)
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

DROP TABLE IF EXISTS `Motiv`;
-- Tabela za motive (statični podaci)
CREATE TABLE Motiv (
    idMotiv INT AUTO_INCREMENT PRIMARY KEY,
    opis TEXT NOT NULL 
);

DROP TABLE IF EXISTS `Osumnjicen`;
-- Tabela za osumnjičene (globalni podaci)
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

DROP TABLE IF EXISTS `OdnosOsumnjicenZrtva`;
-- Tabela za odnos osumnjicen zrtva (globalni podaci)
CREATE TABLE OdnosOsumnjicenZrtva (
    idOdnos INT AUTO_INCREMENT PRIMARY KEY,
    osumnjicenId INT NOT NULL,
    zrtvaId INT NOT NULL,
    tipOdnosa ENUM('poslovni', 'licni','porodicni','rivalski','slucajni','ljubavni') NOT NULL, 
    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva),
    FOREIGN KEY (osumnjicenId) REFERENCES Osumnjicen(idOsumnjicen)
);


DROP TABLE IF EXISTS `DokazOsumnjicen`;
-- Tabela za dokaze (globalni podaci)
CREATE TABLE DokazOsumnjicen (
    idDokazOsumnjicen INT AUTO_INCREMENT PRIMARY KEY,
    dokazId INT NOT NULL,
    osumnjicenId INT NOT NULL,
    FOREIGN KEY (dokazId) REFERENCES Dokaz(idDokaz),
    FOREIGN KEY (osumnjicenId) REFERENCES Osumnjicen(idOsumnjicen)
);

DROP TABLE IF EXISTS `Alibi`;
-- Tabela za osumnjičene (globalni podaci)
CREATE TABLE Alibi (
    idAlibi INT AUTO_INCREMENT PRIMARY KEY,
    osumnjicenId INT NOT NULL,
    svedokId INT,
    opis VARCHAR(255) NOT NULL,
    statusAlibija ENUM('potvrdjen', 'lazan', 'nepotvrdjen') NOT NULL, 
    FOREIGN KEY (osumnjicenId) REFERENCES Osumnjicen(idOsumnjicen),
    FOREIGN KEY (svedokId) REFERENCES Svedok(idSvedok)
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
    izvestaj TEXT NOT NULL,
    datum DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    uzrokSmrti VARCHAR(255) NOT NULL,
    zrtvaId INT NOT NULL,
    informacije VARCHAR(255) NOT NULL,
    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
);

DROP TABLE IF EXISTS `ForenzickiDokaz`;
-- Tabela za forenzičke dokaze (globalni podaci)
CREATE TABLE ForenzickiDokaz (
    idForenzickiDokaz INT AUTO_INCREMENT PRIMARY KEY,
    tipForenzickiDokaz ENUM('otisak', 'DNK', 'dokument') NOT NULL,
    opis TEXT NOT NULL,
	statusS INT NOT NULL,
    zrtvaId INT NOT NULL,
    veza VARCHAR(255) NOT NULL,
    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
);

DROP TABLE IF EXISTS `Telefon`;
-- Tabela za telefon (globalni podaci)
CREATE TABLE Telefon (
    idTelefon INT AUTO_INCREMENT PRIMARY KEY,
    model VARCHAR(50) NOT NULL,
    os ENUM('IOS', 'Android') NOT NULL,
	zrtvaId INT NOT NULL,
    sifra VARCHAR(100) NOT NULL,
    informacije VARCHAR(255) NOT NULL,
    FOREIGN KEY (zrtvaId) REFERENCES Zrtva(idZrtva)
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

DROP TABLE IF EXISTS `MisijaPoruka`;
-- Tabela za misije poruka (globalni podaci)
CREATE TABLE MisijaPoruka (
    idMisija INT AUTO_INCREMENT PRIMARY KEY,
    zlocinId INT NOT NULL,
    naziv VARCHAR(255) NOT NULL,
    statusS INT NOT NULL,
    posiljalac VARCHAR(100) NOT NULL,
    poruka TEXT NOT NULL,
    FOREIGN KEY (zlocinId) REFERENCES Zlocin(idZlocin)
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
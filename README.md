DROP DATABASE IF EXISTS system_zarzadzania_budynkiem_db;
CREATE DATABASE system_zarzadzania_budynkiem_db;
USE system_zarzadzania_budynkiem_db;

-- Dane logowania i rola użytkownika
CREATE TABLE dane_logowania (
    id_logowania INT PRIMARY KEY AUTO_INCREMENT,
    login VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rola VARCHAR(20) NOT NULL DEFAULT 'user'
);

-- Dane osobowe osoby, powiązane z kontem
CREATE TABLE dane_osobowe (
    id_osoby INT PRIMARY KEY AUTO_INCREMENT,
    imie VARCHAR(50) NOT NULL,
    nazwisko VARCHAR(50) NOT NULL,
    pesel VARCHAR(11) UNIQUE,
    data_urodzenia DATE,
    telefon VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE,
    id_logowania INT NOT NULL,
    FOREIGN KEY (id_logowania) REFERENCES dane_logowania(id_logowania) ON DELETE CASCADE
);

-- Budynki
CREATE TABLE budynki (
    id_budynku INT PRIMARY KEY AUTO_INCREMENT,
    nazwa_budynku VARCHAR(100) NOT NULL,
    adres VARCHAR(200) NOT NULL,
    typ_budynku VARCHAR(50) NOT NULL
);

-- Pokoje/pomieszczenia powiązane z budynkiem
CREATE TABLE pokoje (
    id_pokoju INT PRIMARY KEY AUTO_INCREMENT,
    id_budynku INT NOT NULL,
    typ_pomieszczenia VARCHAR(50) NOT NULL,
    czy_zajete VARCHAR(3) DEFAULT 'nie',
    cena_czynszu INT NOT NULL,
    cena_zakupu INT,
    FOREIGN KEY (id_budynku) REFERENCES budynki(id_budynku) ON DELETE CASCADE
);

-- Każda osoba (która jest lokatorem) może mieć przypisany pokój
CREATE TABLE lokatorzy (
    id_lokatora INT PRIMARY KEY AUTO_INCREMENT,
    id_osoby INT NOT NULL,
    id_pokoju INT NOT NULL,
    najblizsza_zaplata DATE,
    ostatnia_zaplata DATE,
    FOREIGN KEY (id_osoby) REFERENCES dane_osobowe(id_osoby) ON DELETE CASCADE,
    FOREIGN KEY (id_pokoju) REFERENCES pokoje(id_pokoju) ON DELETE CASCADE
);

-- Rachunki powiązane z lokatorem
CREATE TABLE rachunki (
    id_rachunku INT PRIMARY KEY AUTO_INCREMENT,
    id_lokatora INT NOT NULL,
    id_pokoju INT NOT NULL,
    opis VARCHAR(50),
    kwota DECIMAL(10,2) NOT NULL,
    termin_platnosci DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'NIEOPŁACONY',
    FOREIGN KEY (id_lokatora) REFERENCES lokatorzy(id_lokatora) ON DELETE CASCADE,
    FOREIGN KEY (id_pokoju) REFERENCES pokoje(id_pokoju) ON DELETE CASCADE
);

-- Tworzenie konta administratora w tabeli dane_logowania
INSERT INTO dane_logowania (login, password, rola)
VALUES ('admin', 'admin', 'admin');

-- Dodanie danych osobowych administratora
INSERT INTO dane_osobowe (imie, nazwisko, pesel, data_urodzenia, telefon, email, id_logowania)
VALUES ('Jan', 'Kowalski', '12345678910', '2005-07-01', '500123456', 'admin@gmail.com', 1);

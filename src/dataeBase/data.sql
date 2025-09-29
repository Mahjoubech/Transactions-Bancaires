create database Transactions;
use Transactions;
CREATE TABLE Client (
   id VARCHAR(30) PRIMARY KEY,,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);
CREATE TABLE Compte (
    id VARCHAR(30) PRIMARY KEY,,
    numero VARCHAR(20) NOT NULL UNIQUE,
    solde DECIMAL(15,2) NOT NULL DEFAULT 0,
    idClient VARCHAR(30) NOT NULL,
    typeCompte ENUM('COURANT', 'EPARGNE') NOT NULL,
    decouvertAutorise DECIMAL(15,2), -- pour les comptes courants
    tauxInteret DECIMAL(5,2),        -- pour les comptes épargne
    FOREIGN KEY (idClient) REFERENCES Client(id)
);
CREATE TABLE Transaction (
    id VARCHAR(30) PRIMARY KEY,,
    date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    montant DECIMAL(15,2) NOT NULL,
    typeTransaction ENUM('VERSEMENT', 'RETRAIT', 'VIREMENT') NOT NULL,
    lieu VARCHAR(100),
    idCompte VARCHAR(30) NOT NULL,
    FOREIGN KEY (idCompte) REFERENCES Compte(id)
);
DROP TABLE IF EXISTS BankAccountPivot;
DROP TABLE IF EXISTS BankAccount;
DROP TABLE IF EXISTS BankAccountParameter;
DROP TABLE IF EXISTS Types;

-- Table BankAccountParameter
CREATE TABLE BankAccountParameter (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      overdraft_limit DOUBLE NOT NULL DEFAULT 0.00,
                                      state ENUM('active', 'inactive','bloqued','closed') NOT NULL DEFAULT 'active'
);

-- Table Types (types de comptes bancaires)
CREATE TABLE Types (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL UNIQUE
);

-- Table BankAccount (avec ID en VARCHAR pour compatibilité)
CREATE TABLE BankAccount (
                             id VARCHAR(50) PRIMARY KEY,
                             parameter_id INT NOT NULL,
                             type_id INT NOT NULL,
                             sold DOUBLE NOT NULL DEFAULT 0.00,
                             iban VARCHAR(34) NOT NULL UNIQUE,
                             FOREIGN KEY (parameter_id) REFERENCES BankAccountParameter(id),
                             FOREIGN KEY (type_id) REFERENCES Types(id)
);

-- Table BankAccountPivot (relation many-to-many entre Account et BankAccount)
CREATE TABLE BankAccountPivot (
                                  bank_account_id VARCHAR(50) NOT NULL,
                                  account_id VARCHAR(50) NOT NULL,
                                  PRIMARY KEY (bank_account_id, account_id),
                                  FOREIGN KEY (bank_account_id) REFERENCES BankAccount(id) ON DELETE CASCADE
);

-- Insertion de données de test pour Types
INSERT INTO Types (name) VALUES ('Compte Courant');
INSERT INTO Types (name) VALUES ('Livret A');
INSERT INTO Types (name) VALUES ('PEL');
INSERT INTO Types (name) VALUES ('Compte Épargne');

-- Insertion de données de test pour BankAccountParameter
INSERT INTO BankAccountParameter (overdraft_limit, state) VALUES (500.00, 'active');
INSERT INTO BankAccountParameter (overdraft_limit, state) VALUES (1000.00, 'active');
INSERT INTO BankAccountParameter (overdraft_limit, state) VALUES (0.00, 'active');
INSERT INTO BankAccountParameter (overdraft_limit, state) VALUES (2000.00, 'inactive');

-- Insertion de données de test pour BankAccount
INSERT INTO BankAccount (id, parameter_id, type_id, sold, iban)
VALUES ('BA001', 1, 1, 1500.50, 'FR7612345678901234567890123');

INSERT INTO BankAccount (id, parameter_id, type_id, sold, iban)
VALUES ('BA002', 2, 2, 5000.00, 'FR7698765432109876543210987');

INSERT INTO BankAccount (id, parameter_id, type_id, sold, iban)
VALUES ('BA003', 3, 3, 10000.00, 'FR7611111111111111111111111');

-- Note: Les données pour BankAccountPivot seront insérées après la création des Accounts
INSERT INTO BankAccountPivot (bank_account_id, account_id) VALUES ('BA001', 'admin');
INSERT INTO BankAccountPivot (bank_account_id, account_id) VALUES ('BA002', 'admin');
INSERT INTO BankAccountPivot (bank_account_id, account_id) VALUES ('BA003', 'admin');
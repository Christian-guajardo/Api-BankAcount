DROP TABLE IF EXISTS BankAccountPivot;
DROP TABLE IF EXISTS BankAccount;
DROP TABLE IF EXISTS BankAccountParameter;
DROP TABLE IF EXISTS Types;

-- Table BankAccountParameter
CREATE TABLE BankAccountParameter (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      overdraft_limit DOUBLE NOT NULL DEFAULT 0.00,
                                      state ENUM('ACTIVE', 'INACTIVE','BLOQUED','CLOSED') NOT NULL DEFAULT 'ACTIVE'
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
INSERT INTO BankAccountParameter (overdraft_limit, state) VALUES (500.00, 'ACTIVE');
INSERT INTO BankAccountParameter (overdraft_limit, state) VALUES (1000.00, 'ACTIVE');
INSERT INTO BankAccountParameter (overdraft_limit, state) VALUES (0.00, 'ACTIVE');
INSERT INTO BankAccountParameter (overdraft_limit, state) VALUES (2000.00, 'ACTIVE');
INSERT INTO BankAccountParameter (overdraft_limit, state) VALUES (300.00, 'ACTIVE');
INSERT INTO BankAccountParameter (overdraft_limit, state) VALUES (800.00, 'ACTIVE');
INSERT INTO BankAccountParameter (overdraft_limit, state) VALUES (1500.00, 'ACTIVE');
INSERT INTO BankAccountParameter (overdraft_limit, state) VALUES (0.00, 'INACTIVE');
INSERT INTO BankAccountParameter (overdraft_limit, state) VALUES (2500.00, 'ACTIVE');
INSERT INTO BankAccountParameter (overdraft_limit, state) VALUES (100.00, 'ACTIVE');

-- Insertion de données de test pour BankAccount
INSERT INTO BankAccount (id, parameter_id, type_id, sold, iban)
VALUES ('BA001', 1, 1, 1500.50, 'FR7612345678901234567890123');

INSERT INTO BankAccount (id, parameter_id, type_id, sold, iban)
VALUES ('BA002', 2, 2, 5000.00, 'FR7698765432109876543210987');

INSERT INTO BankAccount (id, parameter_id, type_id, sold, iban)
VALUES ('BA003', 3, 3, 10000.00, 'FR7611111111111111111111111');

INSERT INTO BankAccount (id, parameter_id, type_id, sold, iban)
VALUES ('BA004', 4, 1, 12000.00, 'FR7611111111111111111111112');

INSERT INTO BankAccount (id, parameter_id, type_id, sold, iban)
VALUES ('BA005', 5, 1, 2500.75, 'FR7612345678901234567890124');

INSERT INTO BankAccount (id, parameter_id, type_id, sold, iban)
VALUES ('BA006', 6, 2, 800.00, 'FR7612345678901234567890125');

INSERT INTO BankAccount (id, parameter_id, type_id, sold, iban)
VALUES ('BA007', 7, 3, 15000.00, 'FR7612345678901234567890126');

INSERT INTO BankAccount (id, parameter_id, type_id, sold, iban)
VALUES ('BA008', 8, 4, 300.00, 'FR7612345678901234567890127');

INSERT INTO BankAccount (id, parameter_id, type_id, sold, iban)
VALUES ('BA009', 9, 1, 4200.40, 'FR7612345678901234567890128');

INSERT INTO BankAccount (id, parameter_id, type_id, sold, iban)
VALUES ('BA010', 10, 2, 9800.00, 'FR7612345678901234567890129');

-- Note: Les données pour BankAccountPivot seront insérées après la création des Accounts
INSERT INTO BankAccountPivot (bank_account_id, account_id) VALUES ('BA001', '1');
INSERT INTO BankAccountPivot (bank_account_id, account_id) VALUES ('BA002', '1');
INSERT INTO BankAccountPivot (bank_account_id, account_id) VALUES ('BA005', '1');
INSERT INTO BankAccountPivot (bank_account_id, account_id) VALUES ('BA006', '1');

INSERT INTO BankAccountPivot (bank_account_id, account_id) VALUES ('BA003', '2');
INSERT INTO BankAccountPivot (bank_account_id, account_id) VALUES ('BA007', '2');
INSERT INTO BankAccountPivot (bank_account_id, account_id) VALUES ('BA008', '2');

INSERT INTO BankAccountPivot (bank_account_id, account_id) VALUES ('BA004', '3');
INSERT INTO BankAccountPivot (bank_account_id, account_id) VALUES ('BA009', '3');
INSERT INTO BankAccountPivot (bank_account_id, account_id) VALUES ('BA010', '3');
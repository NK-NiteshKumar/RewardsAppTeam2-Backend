-- =========================================================
-- DATABASE
-- =========================================================
CREATE DATABASE IF NOT EXISTS rewards_db;
USE rewards_db;

-- =========================================================
-- USERS (ADMIN CES + CES USERS)
-- =========================================================
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL,
                       active TINYINT(1) DEFAULT 1,

                       created_by VARCHAR(50),
                       created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                       modified_by VARCHAR(50),
                       modified_date DATETIME DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP
);

-- =========================================================
-- CUSTOMERS
-- =========================================================
CREATE TABLE customers (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           first_name VARCHAR(50) NOT NULL,
                           last_name VARCHAR(50) NOT NULL,
                           dob DATE NOT NULL,
                           phone_no VARCHAR(10) NOT NULL UNIQUE,
                           email VARCHAR(100) NOT NULL,
                           doj DATE NOT NULL,

                           customer_type VARCHAR(20) NOT NULL,   -- REGULAR / PREMIUM
                           status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

                           created_by VARCHAR(50) NOT NULL,
                           created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                           modified_by VARCHAR(50),
                           modified_date DATETIME DEFAULT CURRENT_TIMESTAMP
                               ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_customer_name
    ON customers(first_name, last_name);

-- =========================================================
-- CREDIT CARDS
-- =========================================================
CREATE TABLE credit_cards (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              card_number VARCHAR(16) NOT NULL UNIQUE,
                              expiry_date DATE NOT NULL,
                              cvv VARCHAR(255) NOT NULL,
                              status VARCHAR(20) NOT NULL, -- LINKED / BLOCKED

                              customer_id BIGINT NOT NULL,

                              created_by VARCHAR(50) NOT NULL,
                              created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                              modified_by VARCHAR(50),
                              modified_date DATETIME DEFAULT CURRENT_TIMESTAMP
                                  ON UPDATE CURRENT_TIMESTAMP,

                              CONSTRAINT fk_credit_card_customer
                                  FOREIGN KEY (customer_id)
                                      REFERENCES customers(id)
);

CREATE INDEX idx_card_customer
    ON credit_cards(customer_id);

-- =========================================================
-- TRANSACTIONS
-- =========================================================
CREATE TABLE transactions (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              transaction_id VARCHAR(12) NOT NULL UNIQUE,
                              amount INT NOT NULL CHECK (amount BETWEEN 500 AND 50000),
                              transaction_date DATETIME NOT NULL,
                              merchant VARCHAR(100),
                              status VARCHAR(20) NOT NULL,  -- NEW / PROCESSED

                              card_id BIGINT NOT NULL,

                              reward_points INT,
                              processed_date DATETIME,

                              created_by VARCHAR(50) NOT NULL,
                              created_date DATETIME DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_transaction_card
                                  FOREIGN KEY (card_id)
                                      REFERENCES credit_cards(id)
);

CREATE INDEX idx_tx_card ON transactions(card_id);
CREATE INDEX idx_tx_status ON transactions(status);

-- =========================================================
-- REWARD ITEMS (CATALOG)
-- =========================================================
CREATE TABLE reward_items (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              name VARCHAR(100) NOT NULL,
                              points_required INT NOT NULL,
                              category VARCHAR(50) NOT NULL,
                              image_url VARCHAR(255)
);

-- =========================================================
-- REWARD CARTS (ONE PER CUSTOMER)
-- =========================================================
CREATE TABLE reward_carts (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              customer_id BIGINT NOT NULL UNIQUE,
                              created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                              modified_date DATETIME DEFAULT CURRENT_TIMESTAMP
                                  ON UPDATE CURRENT_TIMESTAMP,

                              CONSTRAINT fk_cart_customer
                                  FOREIGN KEY (customer_id)
                                      REFERENCES customers(id)
);

-- =========================================================
-- REWARD CART ITEMS
-- =========================================================
CREATE TABLE reward_cart_items (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   cart_id BIGINT NOT NULL,
                                   reward_item_id BIGINT NOT NULL,
                                   quantity INT NOT NULL CHECK (quantity > 0),

                                   CONSTRAINT fk_cart_item_cart
                                       FOREIGN KEY (cart_id)
                                           REFERENCES reward_carts(id),

                                   CONSTRAINT fk_cart_item_reward
                                       FOREIGN KEY (reward_item_id)
                                           REFERENCES reward_items(id),

                                   UNIQUE (cart_id, reward_item_id)
);

-- =========================================================
-- REDEMPTIONS (PARENT)
-- =========================================================
CREATE TABLE redemptions (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             customer_id BIGINT NOT NULL,
                             points_used INT NOT NULL,
                             redeemed_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                             redeemed_by VARCHAR(50),

                             CONSTRAINT fk_redemption_customer
                                 FOREIGN KEY (customer_id)
                                     REFERENCES customers(id)
);

CREATE INDEX idx_redemption_customer
    ON redemptions(customer_id);

-- =========================================================
-- REDEMPTION ITEMS (CHILD)
-- =========================================================
CREATE TABLE redemption_items (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  redemption_id BIGINT NOT NULL,
                                  reward_item_id BIGINT NOT NULL,
                                  quantity INT NOT NULL,
                                  points_cost INT NOT NULL,

                                  CONSTRAINT fk_redemption_item_redemption
                                      FOREIGN KEY (redemption_id)
                                          REFERENCES redemptions(id),

                                  CONSTRAINT fk_redemption_item_reward
                                      FOREIGN KEY (reward_item_id)
                                          REFERENCES reward_items(id)
);

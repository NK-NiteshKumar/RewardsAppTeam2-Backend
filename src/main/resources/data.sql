USE rewards_db;

INSERT INTO users (username, password, role, active, created_by) VALUES ('admin','$2a$10$FUYaqOVA8leF1PZ1h4E4G.lHyMRslOSkeF1Hi4HkqtDdX0PdyxBBK','ADMIN',true,'SYSTEM');

INSERT INTO reward_categories (name) VALUES
                                         ('Gift Cards'),
                                         ('Travel & Holidays'),
                                         ('Shopping & Electronics'),
                                         ('Dining & Lifestyle'),
                                         ('Health & Fitness'),
                                         ('Learning & Subscriptions');

-- Gift Cards
INSERT INTO reward_items (name, points_required, image_url, category_id) VALUES
                                                                             ('Google Play Gift Card', 5000, 'dummy.png', 1),
                                                                             ('Apple Gift Card', 6000, 'dummy.png', 1),
                                                                             ('Amazon Gift Card', 4500, 'dummy.png', 1),
                                                                             ('Flipkart Gift Card', 4500, 'dummy.png', 1),
                                                                             ('Swiggy Gift Card', 3500, 'dummy.png', 1),
                                                                             ('Zomato Gift Card', 3500, 'dummy.png', 1);

-- Travel & Holidays
INSERT INTO reward_items VALUES
                             (NULL, 'Trip to Manali', 40000, 'dummy.png', 2),
                             (NULL, 'Trip to Kanyakumari', 30000, 'dummy.png', 2),
                             (NULL, 'Goa Beach Holiday', 45000, 'dummy.png', 2),
                             (NULL, 'Jaipur Heritage Trip', 28000, 'dummy.png', 2),
                             (NULL, 'Ooty Hill Station Trip', 38000, 'dummy.png', 2);

-- Shopping & Electronics
INSERT INTO reward_items VALUES
                             (NULL, 'Bluetooth Headphones', 12000, 'dummy.png', 3),
                             (NULL, 'Smart Watch', 18000, 'dummy.png', 3),
                             (NULL, 'Wireless Earbuds', 15000, 'dummy.png', 3),
                             (NULL, 'Smartphone Voucher', 22000, 'dummy.png', 3),
                             (NULL, 'Laptop Bag', 6000, 'dummy.png', 3);

-- Dining & Lifestyle
INSERT INTO reward_items VALUES
                             (NULL, 'Dinner for Two', 8000, 'dummy.png', 4),
                             (NULL, 'Cafe Voucher', 4000, 'dummy.png', 4),
                             (NULL, 'Movie Tickets', 5000, 'dummy.png', 4),
                             (NULL, 'Spa Voucher', 10000, 'dummy.png', 4);

-- Health & Fitness
INSERT INTO reward_items VALUES
                             (NULL, 'Gym Membership (3 months)', 20000, 'dummy.png', 5),
                             (NULL, 'Yoga Classes', 7000, 'dummy.png', 5),
                             (NULL, 'Fitness Band', 9000, 'dummy.png', 5),
                             (NULL, 'Nutrition Consultation', 6000, 'dummy.png', 5);

-- Learning & Subscriptions
INSERT INTO reward_items VALUES
                             (NULL, 'Online Course Voucher', 10000, 'dummy.png', 6),
                             (NULL, 'E-Book Subscription', 5000, 'dummy.png', 6),
                             (NULL, 'Coding Platform Access', 12000, 'dummy.png', 6),
                             (NULL, 'Music Subscription', 4000, 'dummy.png', 6);


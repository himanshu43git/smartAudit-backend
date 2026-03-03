--INSERT INTO users (id, email, password, phone_number, hashed_otp, otp_generated_at, otp_expiry, otp_attempts, otp_used, otp_channel, otp_verified_at, is_verified, created_at, updated_at)
--VALUES ('11111111-1111-1111-1111-111111111111', 'alice@example.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5G1v0c2mYzu3d6PvJEa3TBUnIFJ/u', '+911111111111', NULL, NULL, NULL, 0, FALSE, NULL, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
--
--INSERT INTO users (id, email, password, phone_number, hashed_otp, otp_generated_at, otp_expiry, otp_attempts, otp_used, otp_channel, otp_verified_at, is_verified, created_at, updated_at)
--VALUES ('22222222-2222-2222-2222-222222222222', 'bob@example.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5G1v0c2mYzu3d6PvJEa3TBUnIFJ/u', '+912222222222', NULL, NULL, NULL, 0, FALSE, NULL, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
--
--INSERT INTO users (id, email, password, phone_number, hashed_otp, otp_generated_at, otp_expiry, otp_attempts, otp_used, otp_channel, otp_verified_at, is_verified, created_at, updated_at)
--VALUES ('33333333-3333-3333-3333-333333333333', 'carol@example.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5G1v0c2mYzu3d6PvJEa3TBUnIFJ/u', '+913333333333', NULL, NULL, NULL, 0, FALSE, NULL, NULL, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
--
--INSERT INTO users (id, email, password, phone_number, hashed_otp, otp_generated_at, otp_expiry, otp_attempts, otp_used, otp_channel, otp_verified_at, is_verified, created_at, updated_at)
--VALUES ('44444444-4444-4444-4444-444444444444', 'dave@example.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5G1v0c2mYzu3d6PvJEa3TBUnIFJ/u', '+914444444444', NULL, NULL, NULL, 0, FALSE, NULL, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
--
--INSERT INTO users (id, email, password, phone_number, hashed_otp, otp_generated_at, otp_expiry, otp_attempts, otp_used, otp_channel, otp_verified_at, is_verified, created_at, updated_at)
--VALUES ('55555555-5555-5555-5555-555555555555', 'eve@example.com', '$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5G1v0c2mYzu3d6PvJEa3TBUnIFJ/u', '+915555555555', NULL, NULL, NULL, 0, FALSE, NULL, NULL, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- Plain password for all users: Strong@123

INSERT INTO users (id, email, password, phone_number, hashed_otp, otp_generated_at, otp_expiry, otp_attempts, otp_used, otp_channel, otp_verified_at, is_verified, created_at, updated_at)
VALUES
('11111111-1111-1111-1111-111111111111',
 'alice@example.com',
 '$2a$10$Dow1x6vKkS5uR1lQvJ8b9eJY8m3n8GZ0KpQeF9wZ2Lk7vYcQ2t6yK',
 '9876543210',
 NULL, NULL, NULL, 0, FALSE, NULL, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('22222222-2222-2222-2222-222222222222',
 'bob@example.com',
 '$2a$10$Dow1x6vKkS5uR1lQvJ8b9eJY8m3n8GZ0KpQeF9wZ2Lk7vYcQ2t6yK',
 '9876543211',
 NULL, NULL, NULL, 0, FALSE, NULL, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('33333333-3333-3333-3333-333333333333',
 'carol@example.com',
 '$2a$10$Dow1x6vKkS5uR1lQvJ8b9eJY8m3n8GZ0KpQeF9wZ2Lk7vYcQ2t6yK',
 '9876543212',
 NULL, NULL, NULL, 0, FALSE, NULL, NULL, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('44444444-4444-4444-4444-444444444444',
 'dave@example.com',
 '$2a$10$Dow1x6vKkS5uR1lQvJ8b9eJY8m3n8GZ0KpQeF9wZ2Lk7vYcQ2t6yK',
 '9876543213',
 NULL, NULL, NULL, 0, FALSE, NULL, NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('55555555-5555-5555-5555-555555555555',
 'eve@example.com',
 '$2a$10$Dow1x6vKkS5uR1lQvJ8b9eJY8m3n8GZ0KpQeF9wZ2Lk7vYcQ2t6yK',
 '9876543214',
 NULL, NULL, NULL, 0, FALSE, NULL, NULL, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

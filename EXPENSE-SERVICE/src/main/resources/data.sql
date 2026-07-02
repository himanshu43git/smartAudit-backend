------ ==========================================================
------ DATA.SQL for Expense Service (expense_db)
------ ==========================================================
----
------ 1. Insert 5 Categories
----INSERT INTO categories (id, user_id, name, type, icon_url, created_at) VALUES
----('c001ec01-1111-4444-8888-000000000001', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'Food & Dining', 'EXPENSE', 'https://cdn-icons/food.png', NOW()),
----('c001ec01-1111-4444-8888-000000000002', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'Transportation', 'EXPENSE', 'https://cdn-icons/car.png', NOW()),
----('c001ec01-1111-4444-8888-000000000003', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'Salary', 'INCOME', 'https://cdn-icons/money.png', NOW()),
----('c001ec01-1111-4444-8888-000000000004', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'Entertainment', 'EXPENSE', 'https://cdn-icons/movie.png', NOW()),
----('c001ec01-1111-4444-8888-000000000005', 'f47ac10b-58cc-4372-a567-0e02b2c3d479', 'Health', 'EXPENSE', 'https://cdn-icons/med.png', NOW());
----
------ 2. Insert 5 Expenses
------ Ensure your table has columns: id, user_id, category_id, title, description,
------ merchant_name, transaction_date, original_amount, currency_code, payment_status,
------ created_at, updated_at
----
----INSERT INTO expenses (
----    id, user_id, category_id, title, description, merchant_name,
----    transaction_date, original_amount, payment_status, created_at, updated_at
----) VALUES
----('e001ee01-2222-4444-9999-000000000001', 'f47ac10b-58cc-4372-a567-0e02b2c3d479',
---- 'c001ec01-1111-4444-8888-000000000001', 'Dinner at Italian Place', 'Team dinner for project launch',
---- 'Luigi Bistro', '2023-10-01 20:00:00', 120.50,   'PAID', NOW(), NOW()),
----
----('e001ee01-2222-4444-9999-000000000002', 'f47ac10b-58cc-4372-a567-0e02b2c3d479',
---- 'c001ec01-1111-4444-8888-000000000002', 'Uber Ride', 'Commute to office',
---- 'Uber Technologies', '2023-10-02 09:15:00', 15.00,   'PAID', NOW(), NOW()),
----
----('e001ee01-2222-4444-9999-000000000003', 'f47ac10b-58cc-4372-a567-0e02b2c3d479',
---- 'c001ec01-1111-4444-8888-000000000003', 'Monthly Salary', 'September paycheck',
---- 'Tech Corp Inc', '2023-09-30 00:00:00', 5000.00,   'PAID', NOW(), NOW()),
----
----('e001ee01-2222-4444-9999-000000000004', 'f47ac10b-58cc-4372-a567-0e02b2c3d479',
---- 'c001ec01-1111-4444-8888-000000000004', 'Netflix Subscription', 'Monthly streaming fee',
---- 'Netflix', '2023-10-05 12:00:00', 19.99,   'PENDING', NOW(), NOW()),
----
----('e001ee01-2222-4444-9999-000000000005', 'f47ac10b-58cc-4372-a567-0e02b2c3d479',
---- 'c001ec01-1111-4444-8888-000000000005', 'Pharmacy Store', 'Vitamins and supplements',
---- 'CVS Pharmacy', '2023-10-06 15:30:00', 45.25,   'PAID', NOW(), NOW());
--
--
---- ==========================================================
---- DATA.SQL for Expense Service (expense_db)
---- ==========================================================
--
---- ==========================================================
---- Categories
---- ==========================================================
--
INSERT INTO categories (
    id,
    user_id,
    name,
    type,
    icon_url,
    created_at
)
VALUES
(
    'c001ec01-1111-4444-8888-000000000001',
    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
    'Food & Dining',
    'EXPENSE',
    'https://cdn-icons/food.png',
    NOW()
),
(
    'c001ec01-1111-4444-8888-000000000002',
    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
    'Transportation',
    'EXPENSE',
    'https://cdn-icons/car.png',
    NOW()
),
(
    'c001ec01-1111-4444-8888-000000000003',
    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
    'Salary',
    'INCOME',
    'https://cdn-icons/money.png',
    NOW()
),
(
    'c001ec01-1111-4444-8888-000000000004',
    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
    'Entertainment',
    'EXPENSE',
    'https://cdn-icons/movie.png',
    NOW()
),
(
    'c001ec01-1111-4444-8888-000000000005',
    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
    'Health',
    'EXPENSE',
    'https://cdn-icons/med.png',
    NOW()
);
--
---- ==========================================================
---- Expenses
---- ==========================================================
--
--INSERT INTO expenses (
--    id,
--    user_id,
--    category_id,
--    title,
--    description,
--    merchant_name,
--    no_of_units,
--    transaction_date,
--    original_amount,
--    receipt_reference_id,
--    payment_status,
--    created_at,
--    updated_at
--)
--VALUES
--(
--    'e001ee01-2222-4444-9999-000000000001',
--    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
--    'c001ec01-1111-4444-8888-000000000001',
--    'Dinner at Italian Place',
--    'Team dinner for project launch',
--    'Luigi Bistro',
--    '2',
--    '2023-10-01T20:00:00Z',
--    120.5000,
--    'receipt-food-001',
--    'PAID',
--    NOW(),
--    NOW()
--),
--
--(
--    'e001ee01-2222-4444-9999-000000000002',
--    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
--    'c001ec01-1111-4444-8888-000000000002',
--    'Uber Ride',
--    'Commute to office',
--    'Uber Technologies',
--    '1',
--    '2023-10-02T09:15:00Z',
--    15.0000,
--    'receipt-transport-001',
--    'PAID',
--    NOW(),
--    NOW()
--),
--
--(
--    'e001ee01-2222-4444-9999-000000000003',
--    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
--    'c001ec01-1111-4444-8888-000000000003',
--    'Monthly Salary',
--    'September paycheck',
--    'Tech Corp Inc',
--    '1',
--    '2023-09-30T00:00:00Z',
--    5000.0000,
--    'receipt-income-001',
--    'PAID',
--    NOW(),
--    NOW()
--),
--
--(
--    'e001ee01-2222-4444-9999-000000000004',
--    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
--    'c001ec01-1111-4444-8888-000000000004',
--    'Netflix Subscription',
--    'Monthly streaming subscription',
--    'Netflix',
--    '1',
--    '2023-10-05T12:00:00Z',
--    19.9900,
--    'receipt-ent-001',
--    'PENDING',
--    NOW(),
--    NOW()
--),
--
--(
--    'e001ee01-2222-4444-9999-000000000005',
--    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
--    'c001ec01-1111-4444-8888-000000000005',
--    'Pharmacy Store',
--    'Vitamins and supplements',
--    'CVS Pharmacy',
--    '3',
--    '2023-10-06T15:30:00Z',
--    45.2500,
--    'receipt-health-001',
--    'PARTIAL',
--    NOW(),
--    NOW()
--);

INSERT INTO expenses (
    id,
    user_id,
    category_id,
    title,
    description,
    merchant_name,
    no_of_units,
    transaction_date,
    original_amount,
    receipt_reference_id,
    payment_status,
    created_at,
    updated_at
)
VALUES
(
    'e001ee01-2222-4444-9999-000000000001',
    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
    'c001ec01-1111-4444-8888-000000000001',
    'Dinner at Italian Place',
    'Team dinner for project launch',
    'Luigi Bistro',
    '2',
    '2026-06-03T20:00:00Z',
    120.5000,
    'receipt-food-001',
    'PAID',
    NOW(),
    NOW()
),

(
    'e001ee01-2222-4444-9999-000000000002',
    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
    'c001ec01-1111-4444-8888-000000000002',
    'Uber Ride',
    'Commute to office',
    'Uber Technologies',
    '1',
    '2026-06-08T09:15:00Z',
    15.0000,
    'receipt-transport-001',
    'PAID',
    NOW(),
    NOW()
),

(
    'e001ee01-2222-4444-9999-000000000003',
    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
    'c001ec01-1111-4444-8888-000000000003',
    'Monthly Salary',
    'June paycheck',
    'Tech Corp Inc',
    '1',
    '2026-06-12T10:00:00Z',
    5000.0000,
    'receipt-income-001',
    'PAID',
    NOW(),
    NOW()
),

(
    'e001ee01-2222-4444-9999-000000000004',
    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
    'c001ec01-1111-4444-8888-000000000004',
    'Netflix Subscription',
    'Monthly streaming subscription',
    'Netflix',
    '1',
    '2026-06-18T12:00:00Z',
    19.9900,
    'receipt-ent-001',
    'PENDING',
    NOW(),
    NOW()
),

(
    'e001ee01-2222-4444-9999-000000000005',
    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
    'c001ec01-1111-4444-8888-000000000005',
    'Pharmacy Store',
    'Vitamins and supplements',
    'CVS Pharmacy',
    '3',
    '2026-06-24T15:30:00Z',
    45.2500,
    'receipt-health-001',
    'PARTIAL',
    NOW(),
    NOW()
);
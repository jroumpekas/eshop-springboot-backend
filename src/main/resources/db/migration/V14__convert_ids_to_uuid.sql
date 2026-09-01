-- 1. Ενεργοποίηση της επέκτασης pgcrypto για τη παραγωγή UUID
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =========================================================================
-- 2. ΑΦΑΙΡΕΣΗ ΥΠΑΡΧΟΝΤΩΝ FOREIGN KEYS
-- =========================================================================
ALTER TABLE orders DROP CONSTRAINT IF EXISTS fk_orders_user;
ALTER TABLE order_items DROP CONSTRAINT IF EXISTS fk_order_items_order;
ALTER TABLE order_items DROP CONSTRAINT IF EXISTS fk_order_items_product;
ALTER TABLE cart_items DROP CONSTRAINT IF EXISTS fk_cart_items_user;
ALTER TABLE cart_items DROP CONSTRAINT IF EXISTS fk_cart_items_product;

-- =========================================================================
-- 3. ΜΕΤΑΤΡΟΠΗ ΠΙΝΑΚΑ: USERS
-- =========================================================================
ALTER TABLE users ADD COLUMN new_id UUID DEFAULT gen_random_uuid();
ALTER TABLE users DROP CONSTRAINT IF EXISTS users_pkey CASCADE;
ALTER TABLE users RENAME COLUMN id TO old_id;
ALTER TABLE users RENAME COLUMN new_id TO id;
ALTER TABLE users ADD PRIMARY KEY (id);

-- =========================================================================
-- 4. ΜΕΤΑΤΡΟΠΗ ΠΙΝΑΚΑ: CATEGORIES
-- =========================================================================
ALTER TABLE categories ADD COLUMN new_id UUID DEFAULT gen_random_uuid();
ALTER TABLE categories DROP CONSTRAINT IF EXISTS categories_pkey CASCADE;
ALTER TABLE categories RENAME COLUMN id TO old_id;
ALTER TABLE categories RENAME COLUMN new_id TO id;
ALTER TABLE categories ADD PRIMARY KEY (id);

-- =========================================================================
-- 5. ΜΕΤΑΤΡΟΠΗ ΠΙΝΑΚΑ: PRODUCTS (Χωρίς category_id FK mapping)
-- =========================================================================
ALTER TABLE products ADD COLUMN new_id UUID DEFAULT gen_random_uuid();
ALTER TABLE products DROP CONSTRAINT IF EXISTS products_pkey CASCADE;
ALTER TABLE products RENAME COLUMN id TO old_id;
ALTER TABLE products RENAME COLUMN new_id TO id;
ALTER TABLE products ADD PRIMARY KEY (id);

-- =========================================================================
-- 6. ΜΕΤΑΤΡΟΠΗ ΠΙΝΑΚΑ: ORDERS
-- =========================================================================
ALTER TABLE orders ADD COLUMN new_id UUID DEFAULT gen_random_uuid();
ALTER TABLE orders ADD COLUMN new_user_id UUID;

UPDATE orders o
SET new_user_id = u.id
FROM users u
WHERE o.user_id = u.old_id;

ALTER TABLE orders DROP CONSTRAINT IF EXISTS orders_pkey CASCADE;
ALTER TABLE orders RENAME COLUMN id TO old_id;
ALTER TABLE orders RENAME COLUMN new_id TO id;
ALTER TABLE orders RENAME COLUMN user_id TO old_user_id;
ALTER TABLE orders RENAME COLUMN new_user_id TO user_id;
ALTER TABLE orders ADD PRIMARY KEY (id);

-- =========================================================================
-- 7. ΜΕΤΑΤΡΟΠΗ ΠΙΝΑΚΑ: ORDER_ITEMS
-- =========================================================================
ALTER TABLE order_items ADD COLUMN new_id UUID DEFAULT gen_random_uuid();
ALTER TABLE order_items ADD COLUMN new_order_id UUID;
ALTER TABLE order_items ADD COLUMN new_product_id UUID;

UPDATE order_items oi
SET new_order_id = o.id
FROM orders o
WHERE oi.order_id = o.old_id;

UPDATE order_items oi
SET new_product_id = p.id
FROM products p
WHERE oi.product_id = p.old_id;

ALTER TABLE order_items DROP CONSTRAINT IF EXISTS order_items_pkey CASCADE;
ALTER TABLE order_items RENAME COLUMN id TO old_id;
ALTER TABLE order_items RENAME COLUMN new_id TO id;
ALTER TABLE order_items RENAME COLUMN order_id TO old_order_id;
ALTER TABLE order_items RENAME COLUMN new_order_id TO order_id;
ALTER TABLE order_items RENAME COLUMN product_id TO old_product_id;
ALTER TABLE order_items RENAME COLUMN new_product_id TO product_id;
ALTER TABLE order_items ADD PRIMARY KEY (id);

-- =========================================================================
-- 8. ΜΕΤΑΤΡΟΠΗ ΠΙΝΑΚΑ: CART_ITEMS
-- =========================================================================
ALTER TABLE cart_items ADD COLUMN new_id UUID DEFAULT gen_random_uuid();
ALTER TABLE cart_items ADD COLUMN new_user_id UUID;
ALTER TABLE cart_items ADD COLUMN new_product_id UUID;

UPDATE cart_items ci
SET new_user_id = u.id
FROM users u
WHERE ci.user_id = u.old_id;

UPDATE cart_items ci
SET new_product_id = p.id
FROM products p
WHERE ci.product_id = p.old_id;

ALTER TABLE cart_items DROP CONSTRAINT IF EXISTS cart_items_pkey CASCADE;
ALTER TABLE cart_items RENAME COLUMN id TO old_id;
ALTER TABLE cart_items RENAME COLUMN new_id TO id;
ALTER TABLE cart_items RENAME COLUMN user_id TO old_user_id;
ALTER TABLE cart_items RENAME COLUMN new_user_id TO user_id;
ALTER TABLE cart_items RENAME COLUMN product_id TO old_product_id;
ALTER TABLE cart_items RENAME COLUMN new_product_id TO product_id;
ALTER TABLE cart_items ADD PRIMARY KEY (id);

-- =========================================================================
-- 9. ΔΙΑΓΡΑΦΗ ΠΑΛΙΩΝ ΣΤΗΛΩΝ (old_id) & ΕΠΑΝΑΦΟΡΑ FOREIGN KEYS
-- =========================================================================
ALTER TABLE users DROP COLUMN IF EXISTS old_id;

ALTER TABLE categories DROP COLUMN IF EXISTS old_id;

ALTER TABLE products DROP COLUMN IF EXISTS old_id;

ALTER TABLE orders DROP COLUMN IF EXISTS old_id;
ALTER TABLE orders DROP COLUMN IF EXISTS old_user_id;
ALTER TABLE orders ADD CONSTRAINT fk_orders_user
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE order_items DROP COLUMN IF EXISTS old_id;
ALTER TABLE order_items DROP COLUMN IF EXISTS old_order_id;
ALTER TABLE order_items DROP COLUMN IF EXISTS old_product_id;
ALTER TABLE order_items ADD CONSTRAINT fk_order_items_order
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE;
ALTER TABLE order_items ADD CONSTRAINT fk_order_items_product
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT;

ALTER TABLE cart_items DROP COLUMN IF EXISTS old_id;
ALTER TABLE cart_items DROP COLUMN IF EXISTS old_user_id;
ALTER TABLE cart_items DROP COLUMN IF EXISTS old_product_id;
ALTER TABLE cart_items ADD CONSTRAINT fk_cart_items_user
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE cart_items ADD CONSTRAINT fk_cart_items_product
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE;
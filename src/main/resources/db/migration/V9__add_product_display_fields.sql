ALTER TABLE products
ADD COLUMN IF NOT EXISTS image_url VARCHAR(255),
ADD COLUMN IF NOT EXISTS old_price NUMERIC(10, 2),
ADD COLUMN IF NOT EXISTS category VARCHAR(100),
ADD COLUMN IF NOT EXISTS rating NUMERIC(2, 1);

UPDATE products
SET image_url = '/products/logitech-mouse.webp',
    old_price = 42.90,
    category = 'Accessories',
    rating = 4.7
WHERE id = 2;

UPDATE products
SET image_url = '/products/laptop-lenovo.webp',
    old_price = 794.99,
    category = 'Laptops',
    rating = 4.6
WHERE id = 3;

UPDATE products
SET image_url = '/products/keyboard-mechanical.webp',
    old_price = 110.99,
    category = 'Accessories',
    rating = 4.5
WHERE id = 5;

UPDATE products
SET image_url = '/products/monitor-samsung.webp',
    old_price = 193.90,
    category = 'Monitors',
    rating = 4.6
WHERE id = 6;

UPDATE products
SET image_url = '/products/iphone-15.webp',
    old_price = NULL,
    category = 'Smartphones',
    rating = 4.8
WHERE id = 7;

UPDATE products
SET image_url = '/products/samsung-s24.webp',
    old_price = NULL,
    category = 'Smartphones',
    rating = 4.7
WHERE id = 8;

UPDATE products
SET image_url = '/products/sony-headers-m5.webp',
    old_price = 399.90,
    category = 'Headphones',
    rating = 4.8
WHERE id = 9;

UPDATE products
SET image_url = '/products/apple-airpods2.webp',
    old_price = 299.90,
    category = 'Headphones',
    rating = 4.7
WHERE id = 10;

UPDATE products
SET image_url = '/products/logitech-keyboard.webp',
    old_price = NULL,
    category = 'Accessories',
    rating = 4.5
WHERE id = 11;

UPDATE products
SET image_url = '/products/Dell-monitor.webp',
    old_price = NULL,
    category = 'Monitors',
    rating = 4.6
WHERE id = 12;

UPDATE products
SET image_url = '/products/HP-printer.webp',
    old_price = 229.90,
    category = 'Printers',
    rating = 4.4
WHERE id = 13;

UPDATE products
SET image_url = '/products/External-ssd.webp',
    old_price = NULL,
    category = 'Storage',
    rating = 4.6
WHERE id = 14;
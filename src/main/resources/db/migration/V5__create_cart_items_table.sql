CREATE TABLE cart_items (
    id BIGSERIAL PRIMARY KEY,
    quantity INTEGER NOT NULL,

    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,

    CONSTRAINT fk_cart_items_users
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_cart_items_products
        FOREIGN KEY (product_id)
        REFERENCES products(id),

    CONSTRAINT uq_cart_items_user_product
        UNIQUE (user_id, product_id)
);
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    quantity INTEGER NOT NULL,
    price DOUBLE PRECISION NOT NULL,

    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,

    CONSTRAINT fk_order_items_orders
        FOREIGN KEY (order_id)
        REFERENCES orders(id),

    CONSTRAINT fk_order_items_products
        FOREIGN KEY (product_id)
        REFERENCES products(id)
);
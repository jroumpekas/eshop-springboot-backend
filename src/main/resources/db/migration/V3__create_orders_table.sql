CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    order_date TIMESTAMP NOT NULL,
    total_amount NUMERIC(10, 2) NOT NULL,
    status VARCHAR(255) NOT NULL,

    CONSTRAINT fk_orders_users
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);
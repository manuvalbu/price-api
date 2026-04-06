CREATE TABLE prices (
    price_id                INTEGER PRIMARY KEY,
    brand_id                INTEGER,
    start_date              DATETIME,
    end_date                DATETIME,
    price_list              INTEGER,
    product_id              INTEGER,
    priority                INTEGER,
    price                   DECIMAL(6, 2),
    curr                    VARCHAR(3)
);
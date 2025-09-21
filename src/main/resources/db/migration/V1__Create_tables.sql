CREATE TABLE location (
    id BIGSERIAL PRIMARY KEY,
    x INTEGER NOT NULL,
    y REAL NOT NULL,
    z BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE coordinates (
    id BIGSERIAL PRIMARY KEY,
    x INTEGER NOT NULL CHECK (x <= 882),
    y DOUBLE PRECISION NOT NULL CHECK (y > -540)
);

CREATE TABLE address (
    id BIGSERIAL PRIMARY KEY,
    zip_code VARCHAR(255) CHECK (zip_code IS NULL OR LENGTH(zip_code) >= 7),
    town_id BIGINT NOT NULL REFERENCES location(id)
);

CREATE TABLE organization (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    coordinates_id BIGINT NOT NULL REFERENCES coordinates(id),
    creation_date TIMESTAMP NOT NULL,
    official_address_id BIGINT REFERENCES address(id),
    annual_turnover BIGINT CHECK (annual_turnover IS NULL OR annual_turnover > 0),
    employees_count BIGINT NOT NULL CHECK (employees_count >= 0),
    rating INTEGER NOT NULL CHECK (rating > 0),
    full_name VARCHAR(255) UNIQUE,
    type VARCHAR(50),
    postal_address_id BIGINT NOT NULL REFERENCES address(id),
    version BIGINT DEFAULT 0
);

CREATE INDEX idx_organization_name ON organization(name);
CREATE INDEX idx_organization_rating ON organization(rating);
CREATE INDEX idx_organization_type ON organization(type);
CREATE INDEX idx_organization_coordinates ON organization(coordinates_id);
CREATE INDEX idx_coordinates_x_y ON coordinates(x, y);
CREATE INDEX idx_address_town ON address(town_id);

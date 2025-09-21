-- Создание таблицы location
CREATE TABLE s408194.location (
    id BIGSERIAL PRIMARY KEY,
    x INTEGER NOT NULL,
    y REAL NOT NULL,
    z BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL
);

-- Создание таблицы coordinates
CREATE TABLE s408194.coordinates (
    id BIGSERIAL PRIMARY KEY,
    x INTEGER NOT NULL CHECK (x <= 882),
    y DOUBLE PRECISION NOT NULL CHECK (y > -540)
);

-- Создание таблицы address
CREATE TABLE s408194.address (
    id BIGSERIAL PRIMARY KEY,
    zip_code VARCHAR(255) CHECK (zip_code IS NULL OR LENGTH(zip_code) >= 7),
    town_id BIGINT NOT NULL REFERENCES location(id)
);

-- Создание таблицы organization
CREATE TABLE s408194.organization (
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

-- Индексы для улучшения производительности
CREATE INDEX idx_organization_name ON s408194.organization(name);
CREATE INDEX idx_organization_rating ON s408194.organization(rating);
CREATE INDEX idx_organization_type ON s408194.organization(type);
CREATE INDEX idx_organization_coordinates ON s408194.organization(coordinates_id);
CREATE INDEX idx_coordinates_x_y ON s408194.coordinates(x, y);
CREATE INDEX idx_address_town ON s408194.address(town_id);

CREATE DATABASE IF NOT EXISTS koperasi_desa
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE koperasi_desa;

CREATE TABLE IF NOT EXISTS products (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(40) NOT NULL UNIQUE,
  name VARCHAR(140) NOT NULL,
  category VARCHAR(80) NOT NULL DEFAULT 'Umum',
  price DECIMAL(14,2) NOT NULL,
  stock INT NOT NULL DEFAULT 0,
  active TINYINT(1) NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT chk_products_price CHECK (price >= 0),
  CONSTRAINT chk_products_stock CHECK (stock >= 0)
);

CREATE TABLE IF NOT EXISTS sales (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  invoice_no VARCHAR(50) NOT NULL UNIQUE,
  buyer_name VARCHAR(140) NOT NULL,
  total DECIMAL(14,2) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Snapshot data barang sengaja disimpan di sale_items.
-- product_id hanya referensi opsional, tanpa FK; transaksi lama tetap ada walau barang dihapus.
CREATE TABLE IF NOT EXISTS sale_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  sale_id BIGINT NOT NULL,
  product_id BIGINT NULL,
  product_code_snapshot VARCHAR(40) NOT NULL,
  product_name_snapshot VARCHAR(140) NOT NULL,
  unit_price_snapshot DECIMAL(14,2) NOT NULL,
  quantity INT NOT NULL,
  subtotal DECIMAL(14,2) NOT NULL,
  CONSTRAINT fk_sale_items_sale FOREIGN KEY (sale_id) REFERENCES sales(id) ON DELETE CASCADE,
  INDEX idx_sale_items_sale (sale_id),
  INDEX idx_sales_buyer (buyer_name),
  CONSTRAINT chk_sale_items_quantity CHECK (quantity > 0)
);

INSERT IGNORE INTO products(code,name,category,price,stock) VALUES
('BRG-001','Beras 5 Kg','Sembako',72000,20),
('BRG-002','Minyak Goreng 1 L','Sembako',18000,35),
('BRG-003','Gula Pasir 1 Kg','Sembako',16500,25),
('BRG-004','Sabun Mandi','Kebutuhan Rumah',4500,50);

-- Inserir produtos de exemplo com IDs explícitos
INSERT INTO products (id, name, product_value) VALUES
(1, 'Cadeira de Escritório Premium', 450.00),
(2, 'Mesa de Computador', 350.00),
(3, 'Estante de Livros', 280.00),
(4, 'Sofá 3 Lugares', 1200.00),
(5, 'Rack para TV', 320.00),
(6, 'Cama de Casal', 800.00),
(7, 'Guarda-Roupa 4 Portas', 950.00),
(8, 'Mesa de Jantar 6 Lugares', 680.00),
(9, 'Cadeira de Jantar (Unidade)', 120.00),
(10, 'Criado-Mudo', 180.00);

-- Atualizar sequence para próximo ID
ALTER TABLE products ALTER COLUMN id RESTART WITH 11;

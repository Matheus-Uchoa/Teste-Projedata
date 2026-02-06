-- Inserir matérias-primas com IDs explícitos
INSERT INTO raw_materials (id, name, stock_quantity) VALUES
(1, 'Madeira MDF 18mm', 500.00),
(2, 'Madeira Pinus', 350.00),
(3, 'Parafusos', 5000.00),
(4, 'Dobradiças', 800.00),
(5, 'Puxadores', 600.00),
(6, 'Verniz', 120.00),
(7, 'Cola para Madeira', 80.00),
(8, 'Lixa', 300.00),
(9, 'Tecido para Estofado', 200.00),
(10, 'Espuma D28', 150.00),
(11, 'Tinta Branca', 90.00),
(12, 'Tinta Marrom', 75.00),
(13, 'Rodízios', 400.00),
(14, 'Trilhos para Gaveta', 250.00),
(15, 'Vidro Temperado', 50.00);

-- Atualizar sequence para próximo ID
ALTER TABLE raw_materials ALTER COLUMN id RESTART WITH 16;

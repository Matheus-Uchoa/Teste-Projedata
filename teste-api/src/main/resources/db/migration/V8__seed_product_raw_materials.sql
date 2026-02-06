-- Associar produtos com matérias-primas necessárias
-- Agora com IDs corretos baseados em V6 (produtos 1-10) e V7 (matérias-primas 1-15)

-- Produto 1: Cadeira de Escritório Premium (R$ 450)
INSERT INTO product_raw_materials (product_id, raw_material_id, quantity_needed) VALUES
(1, 1, 2.5),  -- Madeira MDF 18mm
(1, 3, 20.0), -- Parafusos
(1, 4, 4.0),  -- Dobradiças
(1, 5, 1.0),  -- Puxadores
(1, 6, 0.5),  -- Verniz
(1, 13, 5.0); -- Rodízios

-- Produto 2: Mesa de Computador (R$ 350)
INSERT INTO product_raw_materials (product_id, raw_material_id, quantity_needed) VALUES
(2, 1, 4.0),  -- Madeira MDF 18mm
(2, 2, 2.0),  -- Madeira Pinus
(2, 3, 30.0), -- Parafusos
(2, 6, 1.0);  -- Verniz

-- Produto 3: Estante de Livros (R$ 280)
INSERT INTO product_raw_materials (product_id, raw_material_id, quantity_needed) VALUES
(3, 1, 6.0),  -- Madeira MDF 18mm
(3, 3, 40.0), -- Parafusos
(3, 6, 1.5);  -- Verniz

-- Produto 4: Sofá 3 Lugares (R$ 1200)
INSERT INTO product_raw_materials (product_id, raw_material_id, quantity_needed) VALUES
(4, 2, 5.0),  -- Madeira Pinus
(4, 3, 50.0), -- Parafusos
(4, 9, 8.0),  -- Tecido para Estofado
(4, 10, 6.0); -- Espuma D28

-- Produto 5: Rack para TV (R$ 320)
INSERT INTO product_raw_materials (product_id, raw_material_id, quantity_needed) VALUES
(5, 1, 3.0),  -- Madeira MDF 18mm
(5, 3, 25.0), -- Parafusos
(5, 4, 2.0),  -- Dobradiças
(5, 6, 0.8),  -- Verniz
(5, 15, 1.0); -- Vidro Temperado

-- Produto 6: Cama de Casal (R$ 800)
INSERT INTO product_raw_materials (product_id, raw_material_id, quantity_needed) VALUES
(6, 2, 8.0),  -- Madeira Pinus
(6, 3, 60.0), -- Parafusos
(6, 7, 0.5),  -- Cola para Madeira
(6, 6, 2.0);  -- Verniz

-- Produto 7: Guarda-Roupa 4 Portas (R$ 950)
INSERT INTO product_raw_materials (product_id, raw_material_id, quantity_needed) VALUES
(7, 1, 12.0), -- Madeira MDF 18mm
(7, 3, 80.0), -- Parafusos
(7, 4, 8.0),  -- Dobradiças
(7, 5, 4.0),  -- Puxadores
(7, 6, 3.0);  -- Verniz

-- Produto 8: Mesa de Jantar 6 Lugares (R$ 680)
INSERT INTO product_raw_materials (product_id, raw_material_id, quantity_needed) VALUES
(8, 2, 6.0),  -- Madeira Pinus
(8, 3, 40.0), -- Parafusos
(8, 6, 1.5),  -- Verniz
(8, 11, 0.5); -- Tinta Branca

-- Produto 9: Cadeira de Jantar (R$ 120)
INSERT INTO product_raw_materials (product_id, raw_material_id, quantity_needed) VALUES
(9, 2, 1.5),  -- Madeira Pinus
(9, 3, 15.0), -- Parafusos
(9, 6, 0.3);  -- Verniz

-- Produto 10: Criado-Mudo (R$ 180)
INSERT INTO product_raw_materials (product_id, raw_material_id, quantity_needed) VALUES
(10, 1, 1.5), -- Madeira MDF 18mm
(10, 3, 20.0),-- Parafusos
(10, 4, 1.0), -- Dobradiças
(10, 5, 1.0), -- Puxadores
(10, 6, 0.5); -- Verniz

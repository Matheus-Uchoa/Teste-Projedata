-- View para calcular sugestões de produção baseado no estoque disponível
-- Prioriza produtos pelo VALOR TOTAL de produção (valor unitário × quantidade produzível)

CREATE OR REPLACE VIEW production_suggestions AS
WITH product_max_quantity AS (
    -- Calcula a quantidade máxima de cada produto baseado no estoque de cada matéria-prima
    SELECT
        p.id as product_id,
        p.name as product_name,
        p.product_value as product_value,
        MIN(FLOOR(rm.stock_quantity / prm.quantity_needed)) as max_quantity_by_material
    FROM products p
    INNER JOIN product_raw_materials prm ON p.id = prm.product_id
    INNER JOIN raw_materials rm ON prm.raw_material_id = rm.id
    GROUP BY p.id, p.name, p.product_value
    HAVING MIN(FLOOR(rm.stock_quantity / prm.quantity_needed)) > 0
),
product_with_total AS (
    -- Calcula o valor total de produção (valor unitário × quantidade produzível)
    SELECT
        product_id,
        product_name,
        product_value,
        max_quantity_by_material,
        (product_value * max_quantity_by_material) as total_value
    FROM product_max_quantity
),
ranked_products AS (
    -- Ordena produtos por VALOR TOTAL de produção (maior para menor)
    SELECT
        product_id,
        product_name,
        product_value,
        max_quantity_by_material,
        total_value,
        ROW_NUMBER() OVER (ORDER BY total_value DESC, product_value DESC, product_id) as priority_rank
    FROM product_with_total
)
SELECT
    product_id,
    product_name,
    product_value,
    max_quantity_by_material as suggested_quantity,
    total_value,
    priority_rank
FROM ranked_products
ORDER BY priority_rank;

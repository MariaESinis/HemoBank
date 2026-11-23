CREATE SCHEMA IF NOT EXISTS reporting;

-- Doações detalhadas
CREATE OR REPLACE VIEW reporting.vw_donation_detail
WITH (security_invoker = true) AS
SELECT
    d.dnt_id,
    d.dnt_donation_date,
    d.dnt_quantity_ml,
    d.dnt_donation_type,
    ds.dts_name   AS donation_status,
    dn.don_id,
    dn.don_name   AS donor_name,
    e.emp_id,
    e.emp_name    AS employee_name
FROM donation.donation d
JOIN donation.donation_status ds ON ds.dts_id = d.dnt_dts_id
JOIN donor_mgmt.donor         dn ON dn.don_id = d.dnt_don_id
JOIN hr.employee              e  ON e.emp_id  = d.dnt_emp_id
WHERE d.dnt_deleted_at IS NULL;

-- Estoque disponível (bolsas válidas e que ainda não saíram)
CREATE OR REPLACE VIEW reporting.vw_stock_available
WITH (security_invoker = true) AS
SELECT
    i.inv_labeling,
    i.inv_type,
    COUNT(*)                         AS total_bags,
    COALESCE(SUM(b.bbg_quantity_ml), 0) AS total_ml,
    MIN(b.bbg_expiration_date)       AS nearest_expiration
FROM stock.blood_bag b
JOIN stock.inventory i ON i.inv_id = b.bbg_inv_id
WHERE b.bbg_deleted_at IS NULL
  AND (b.bbg_expiration_date IS NULL OR b.bbg_expiration_date > CURRENT_TIMESTAMP)
  AND NOT EXISTS (
        SELECT 1
        FROM stock.bag_history h
        WHERE h.bgh_bbg_id = b.bbg_id
          AND h.bgh_deleted_at IS NULL
          AND h.bgh_exit_date IS NOT NULL
      )
GROUP BY i.inv_labeling, i.inv_type;

-- Bolsas que vencem nos próximos 7 dias
CREATE OR REPLACE VIEW reporting.vw_bags_expiring_soon
WITH (security_invoker = true) AS
SELECT
    b.bbg_id,
    b.bbg_labeling,
    b.bbg_type,
    b.bbg_quantity_ml,
    b.bbg_expiration_date,
    (b.bbg_expiration_date::date - CURRENT_DATE) AS days_to_expire
FROM stock.blood_bag b
WHERE b.bbg_deleted_at IS NULL
  AND b.bbg_expiration_date >  CURRENT_TIMESTAMP
  AND b.bbg_expiration_date <= CURRENT_TIMESTAMP + INTERVAL '7 days'
  AND NOT EXISTS (
        SELECT 1
        FROM stock.bag_history h
        WHERE h.bgh_bbg_id = b.bbg_id
          AND h.bgh_deleted_at IS NULL
          AND h.bgh_exit_date IS NOT NULL
      )
ORDER BY b.bbg_expiration_date;

-- Última doação por doador
CREATE OR REPLACE VIEW reporting.vw_donor_last_donation
WITH (security_invoker = true) AS
SELECT
    dn.don_id,
    dn.don_name,
    MAX(d.dnt_donation_date)                              AS last_donation_date,
    COUNT(d.dnt_id)                                       AS total_donations,
    (CURRENT_DATE - MAX(d.dnt_donation_date)::date)       AS days_since_last_donation
FROM donor_mgmt.donor dn
LEFT JOIN donation.donation d
       ON d.dnt_don_id = dn.don_id
      AND d.dnt_deleted_at IS NULL
WHERE dn.don_deleted_at IS NULL
GROUP BY dn.don_id, dn.don_name;
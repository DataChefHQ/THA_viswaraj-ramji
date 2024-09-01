DROP VIEW banner_campaign_summary_view;

CREATE VIEW banner_campaign_summary_view AS
SELECT
    i.banner_id,
    i.campaign_id,
    COUNT(c.click_id) AS click_count,
    COUNT(conv.conversion_id) AS conversion_count,
    COALESCE(SUM(conv.revenue), 0) AS total_revenue
FROM
    impressions i
LEFT JOIN
    clicks c ON i.banner_id = c.banner_id AND i.campaign_id = c.campaign_id
LEFT JOIN
    conversions conv ON c.click_id = conv.click_id
GROUP BY
    i.banner_id,
    i.campaign_id;

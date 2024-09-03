--DROP VIEW banner_campaign_summary_view;
--DROP TABLE clicks;
--DROP TABLE conversions;
--DROP TABLE impressions;
DROP VIEW banner_campaign_summary_view;
CREATE OR REPLACE VIEW banner_campaign_summary_view AS
SELECT
    i.banner_id,
    i.campaign_id,
    COUNT(DISTINCT c.click_id) AS click_count,
    COUNT(conv.conversion_id) AS conversion_count,
    COALESCE(SUM(conv.revenue), 0) AS total_revenue,
    i.timestamp
FROM
    impressions i
LEFT JOIN
    clicks c
    ON i.banner_id = c.banner_id
    AND i.campaign_id = c.campaign_id
    AND i.timestamp = c.timestamp
LEFT JOIN
    conversions conv
    ON c.click_id = conv.click_id
    AND c.timestamp = conv.timestamp
GROUP BY
    i.banner_id,
    i.campaign_id,
    i.timestamp;

--DROP VIEW banner_campaign_summary_view;
--DROP TABLE clicks;
--DROP TABLE conversions;
--DROP TABLE impressions;
-- Create your tables
--drop table impressions;
--drop table clicks;
--drop table conversions;
--drop table banner_campaign_summary_view;
DROP VIEW banner_campaign_summary_view;
DROP TABLE  impressions;
DROP TABLE clicks;
DROP TABLE  conversions;


CREATE TABLE impressions (
    banner_id BIGINT NOT NULL,
    campaign_id BIGINT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    PRIMARY KEY (banner_id, campaign_id, timestamp)
);

CREATE TABLE clicks (
    click_id BIGINT NOT NULL,
    banner_id BIGINT NOT NULL,
    campaign_id BIGINT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    PRIMARY KEY (click_id, banner_id, campaign_id, timestamp)
);

CREATE TABLE conversions (
    conversion_id BIGINT NOT NULL,
    click_id BIGINT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    revenue DOUBLE NOT NULL,
    PRIMARY KEY (conversion_id, click_id, timestamp)
);

CREATE INDEX idx_clicks_click_id ON clicks(click_id);
CREATE INDEX idx_clicks_banner_campaign_timestamp ON clicks(banner_id, campaign_id, timestamp);
CREATE INDEX idx_conversions_click_id ON conversions(click_id);

CREATE VIEW banner_campaign_summary_view AS
SELECT
    i.banner_id,
    i.campaign_id,
    COALESCE(click_details.click_count, 0) AS click_count,
    COALESCE(conversion_details.conversion_count, 0) AS conversion_count,
    COALESCE(conversion_details.total_revenue, 0) AS total_revenue,
    i.timestamp
FROM
    impressions i
LEFT JOIN
    (
        SELECT
            banner_id,
            campaign_id,
            timestamp,
            COUNT(DISTINCT click_id) AS click_count
        FROM
            clicks
        GROUP BY
            banner_id,
            campaign_id,
            timestamp
    ) AS click_details
    ON i.banner_id = click_details.banner_id
    AND i.campaign_id = click_details.campaign_id
    AND i.timestamp = click_details.timestamp
LEFT JOIN
    (
        SELECT
            c.banner_id,
            c.campaign_id,
            c.timestamp,
            COUNT(conv.conversion_id) AS conversion_count,
            SUM(conv.revenue) AS total_revenue
        FROM
            clicks c
        LEFT JOIN
            conversions conv ON c.click_id = conv.click_id
        GROUP BY
            c.banner_id,
            c.campaign_id,
            c.timestamp
    ) AS conversion_details
    ON i.banner_id = conversion_details.banner_id
    AND i.campaign_id = conversion_details.campaign_id
    AND i.timestamp = conversion_details.timestamp
GROUP BY
    i.banner_id,
    i.campaign_id,
    i.timestamp;


--2024-09-04 04:12:33.577731
--2024-09-04 04:15:33.577731
--2024-09-04 04:18:33.577731
--2024-09-04 04:21:33.57773

--
--
--select count(*) from IMPRESSIONS where timestamp='2024-09-04 04:49:01.596991'
--20040
--
--select count(*) from IMPRESSIONS where timestamp='2024-09-04 04:52:01.596991'
--20042
--
--select count(*) from IMPRESSIONS where timestamp='2024-09-04 04:55:01.596991'
--20034
--
--select count(*) from IMPRESSIONS where timestamp='2024-09-04 04:58:01.596991'
--20035


--2024-09-04 06:21:44.056393
--2024-09-04 06:24:44.056393
--2024-09-04 06:27:44.056393
select * from IMPRESSIONS where timestamp='2024-09-04 06:18:44.056393' and campaign_id=1;

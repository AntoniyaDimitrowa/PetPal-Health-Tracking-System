-- V3__AddWeightRangeToBreedHealthInfo.sql

-- 1. Add weight_range_min and weight_range_max columns to BreedHealthInfo table
ALTER TABLE breed_health_info
    ADD COLUMN weight_range_min DOUBLE NOT NULL,
    ADD COLUMN weight_range_max DOUBLE NOT NULL;
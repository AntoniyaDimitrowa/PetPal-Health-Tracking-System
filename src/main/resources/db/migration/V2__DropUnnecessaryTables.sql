-- V2__UpdateSchema.sql

-- 1. Add user_id to breed_health_info and pet tables
ALTER TABLE breed_health_info
    ADD COLUMN user_id BIGINT,
    ADD CONSTRAINT fk_breed_health_info_user FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE pet
    ADD COLUMN user_id BIGINT,
    ADD CONSTRAINT fk_pet_user FOREIGN KEY (user_id) REFERENCES user (id);

-- 2. Drop user_pets and user_breed_health_infos tables, as they are no longer needed
DROP TABLE IF EXISTS user_pets;
DROP TABLE IF EXISTS user_breed_health_infos;

-- 3. Modify notes in health_record to use LONGTEXT instead of VARCHAR
ALTER TABLE health_record
    MODIFY notes LONGTEXT;

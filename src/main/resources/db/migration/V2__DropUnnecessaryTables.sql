ALTER TABLE breed_health_info
    ADD COLUMN user_id BIGINT,
    ADD CONSTRAINT fk_breed_health_info_user FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE pet
    ADD COLUMN user_id BIGINT,
    ADD CONSTRAINT fk_pet_user FOREIGN KEY (user_id) REFERENCES user (id);

DROP TABLE IF EXISTS user_pets;
DROP TABLE IF EXISTS user_breed_health_infos;

ALTER TABLE health_record
    MODIFY notes LONGTEXT;

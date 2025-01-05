-- V4__CreateNotificationTable.sql

CREATE TABLE notification
(
    result_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    pet_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    message LONGTEXT NOT NULL,
    is_read BOOLEAN NOT NULL,

    FOREIGN KEY (pet_id) REFERENCES pet(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

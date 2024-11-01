
-- Mood Table
CREATE TABLE mood
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(255) NOT NULL UNIQUE,
    emoji LONGTEXT NOT NULL
);

-- Breed Table
CREATE TABLE breed
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(255)   NOT NULL UNIQUE,
    description    LONGTEXT NOT NULL,
    normal_mood_id BIGINT         NOT NULL,
    minimum_exercise_per_day DOUBLE NOT NULL,
    FOREIGN KEY (normal_mood_id) REFERENCES mood (id)
);

CREATE TABLE breed_health_problems
(
    breed_id       BIGINT NOT NULL,
    health_problem VARCHAR(255),
    FOREIGN KEY (breed_id) REFERENCES breed (id)
);

-- Breed Health Info Table
CREATE TABLE breed_health_info
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    breed_id        BIGINT NOT NULL,
    age_range_start INT    NOT NULL CHECK (age_range_start >= 0),
    age_range_end   INT    NOT NULL CHECK (age_range_end >= 1),
    normal_food_intake DOUBLE NOT NULL,
    normal_water_intake DOUBLE NOT NULL,
    FOREIGN KEY (breed_id) REFERENCES breed (id)
);

-- Pet Table
CREATE TABLE pet
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    breed_id  BIGINT       NOT NULL,
    gender    VARCHAR(255) NOT NULL,
    birthdate DATE         NOT NULL,
    weight DOUBLE NOT NULL CHECK (weight >= 0),
    image     LONGTEXT NOT NULL,
    FOREIGN KEY (breed_id) REFERENCES breed (id)
);



-- Health Record Table
CREATE TABLE health_record
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    pet_id             BIGINT       NOT NULL,
    date               DATE         NOT NULL,
    food_intake DOUBLE NOT NULL,
    water_intake DOUBLE NOT NULL,
    mood_id            BIGINT       NOT NULL,
    activity_level     INT          NOT NULL CHECK (activity_level BETWEEN 1 AND 10),
    social_interaction VARCHAR(255) NOT NULL,
    notes              VARCHAR(1000),
    FOREIGN KEY (pet_id) REFERENCES pet (id),
    FOREIGN KEY (mood_id) REFERENCES mood (id)
);

-- Vaccination Table
CREATE TABLE vaccination
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    type  VARCHAR(255) NOT NULL,
    period_vac BIGINT  NOT NULL CHECK (period_vac >= 1)
);

-- Vaccination Record Table
CREATE TABLE vaccination_record
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    vaccination_id BIGINT NOT NULL,
    date           DATE   NOT NULL,
    pet_id         BIGINT NOT NULL,
    FOREIGN KEY (vaccination_id) REFERENCES vaccination (id),
    FOREIGN KEY (pet_id) REFERENCES pet (id)
);

-- User Table
CREATE TABLE user
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    member_since DATE         NOT NULL,
    role         VARCHAR(255) NOT NULL,
    address      VARCHAR(255) NOT NULL,
    image        LONGTEXT
);

CREATE TABLE user_pets
(
    user_id BIGINT NOT NULL,
    pet_id  BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (pet_id) REFERENCES pet (id)
);


CREATE TABLE user_breed_health_infos
(
    user_id              BIGINT NOT NULL,
    breed_health_info_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (breed_health_info_id) REFERENCES breed_health_info (id)
);




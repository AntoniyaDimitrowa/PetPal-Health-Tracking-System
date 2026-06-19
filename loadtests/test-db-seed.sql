SET FOREIGN_KEY_CHECKS=0;

TRUNCATE TABLE notification;
TRUNCATE TABLE vaccination_record;
TRUNCATE TABLE health_record;
TRUNCATE TABLE pet;
TRUNCATE TABLE breed_health_info;
TRUNCATE TABLE breed_health_problems;
TRUNCATE TABLE vaccination;
TRUNCATE TABLE breed;
TRUNCATE TABLE mood;
TRUNCATE TABLE user;

SET FOREIGN_KEY_CHECKS=1;

INSERT INTO mood (name, emoji) VALUES
  ('Happy', ':)'),
  ('Playful', ':D');

INSERT INTO breed (name, description, normal_mood_id, minimum_exercise_per_day) VALUES
  ('German Shepherd', 'Intelligent, loyal working dog.', 1, 2.0),
  ('Golden Retriever', 'Friendly, family-oriented dog.', 2, 1.5),
  ('Beagle', 'Curious, friendly, great scent hound.', 1, 1.0);

INSERT INTO breed_health_info (
  breed_id,
  user_id,
  age_range_start,
  age_range_end,
  normal_food_intake,
  normal_water_intake,
  weight_range_min,
  weight_range_max
) VALUES
  (2, NULL, 1, 3, 300, 1.2, 20, 28);

INSERT INTO vaccination (name, type, period_vac) VALUES
  ('Rabies', 'FOR_ADULT', 16),
  ('Bordetella', 'FOR_PUPPY', 12);

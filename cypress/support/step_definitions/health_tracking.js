import { Before, Then, When } from "@badeball/cypress-cucumber-preprocessor";
import {
  createHealthRecord,
  fixedDates,
  getBreedByName,
  getHealthRecords,
  getPetStatistics,
} from "../bdd-helpers";
import { resetScenarioState, scenarioState as state } from "../bdd-state";

Before(resetScenarioState);

When("the user adds a health record for the pet", () => {
  return getBreedByName(state.petBreedName, state.accessToken).then((breed) => {
    state.expectedMoodName = breed.normalMood.name;
    state.healthRecordPayload = {
      date: fixedDates.healthRecordDate,
      foodIntake: 250.0,
      waterIntake: 1.8,
      moodId: breed.normalMood.id,
      activityLevel: 7,
      socialInteraction: "Friendly",
      notes: "BDD health record",
    };

    return createHealthRecord(state.petId, state.accessToken, state.healthRecordPayload).then(
      (response) => {
        state.healthRecordResponse = response;
        state.healthRecordId = response.body.id;
      }
    );
  });
});

Then("the health record creation should return status 201", () => {
  expect(state.healthRecordResponse.status).to.eq(201);
  expect(state.healthRecordId).to.be.a("number");
});

Then("the health record should appear in the pet health history", () => {
  return getHealthRecords(state.petId, state.accessToken).then((response) => {
    state.healthHistoryResponse = response;
    expect(response.status).to.eq(200);
    expect(response.body).to.have.length(1);
    expect(response.body[0].id).to.eq(state.healthRecordId);
    expect(response.body[0].foodIntake).to.eq(250);
    expect(response.body[0].waterIntake).to.eq(1.8);
    expect(response.body[0].activityLevel).to.eq(7);
    expect(response.body[0].socialInteraction).to.eq("Friendly");
    expect(response.body[0].mood.name).to.eq(state.expectedMoodName);
  });
});

Then("the pet statistics should include the new record", () => {
  return getPetStatistics(
    state.petId,
    state.accessToken,
    fixedDates.statisticsMonth,
    fixedDates.statisticsYear
  ).then((response) => {
    state.statisticsResponse = response;
    expect(response.status).to.eq(200);
    expect(response.body.foodIntake).to.have.length(1);
    expect(response.body.waterIntake).to.have.length(1);
    expect(response.body.activityLevel).to.have.length(1);
    expect(response.body.moodDistribution).to.have.length(1);
    expect(response.body.foodIntake[0].norm).to.eq(300);
    expect(response.body.waterIntake[0].norm).to.eq(1.2);
    expect(response.body.activityLevel[0].norm).to.eq(1.5);
    expect(response.body.moodDistribution[0].mood).to.eq(state.expectedMoodName);
    expect(response.body.moodDistribution[0].value).to.eq(1);
  });
});

When("the second user adds a health record to that pet", () => {
  return getBreedByName(state.petBreedName, state.guestToken).then((breed) => {
    state.expectedMoodName = breed.normalMood.name;
    state.healthRecordPayload = {
      date: fixedDates.healthRecordDate,
      foodIntake: 180.0,
      waterIntake: 1.2,
      moodId: breed.normalMood.id,
      activityLevel: 6,
      socialInteraction: "Trusted",
      notes: "Guest user health record",
    };

    return createHealthRecord(state.petId, state.guestToken, state.healthRecordPayload).then(
      (response) => {
        state.healthRecordResponse = response;
        state.healthRecordId = response.body.id;
      }
    );
  });
});

When("the first user requests the second user's health records", () => {
  return getHealthRecords(state.petId, state.ownerToken).then((response) => {
    state.response = response;
  });
});

Then("health record access should be rejected with status 403", () => {
  expect(state.response.status).to.eq(403);
});

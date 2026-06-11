import { Before, Then, When } from "@badeball/cypress-cucumber-preprocessor";
import {
  createPet,
  fixedDates,
  getBreedByName,
  getPet,
} from "../bdd-helpers";
import { resetScenarioState, scenarioState as state } from "../bdd-state";

Before(resetScenarioState);

When("the user creates a pet named {string} with breed {string}", (name, breedName) => {
  state.petName = name;
  state.petBreedName = breedName;

  return getBreedByName(breedName, state.accessToken).then((breed) => {
    state.petBreedId = breed.id;

    return createPet(state.accessToken, {
      name,
      breedId: breed.id,
      userId: state.claims.userId,
      gender: "MALE",
      birthdate: fixedDates.petBirthdate,
      weight: 12.5,
      image: null,
      vaccinationRecordsIds: [],
    }).then((response) => {
      state.petCreateResponse = response;
      state.petId = response.body.id;
    });
  });
});

Then("the pet creation should return status 201", () => {
  expect(state.petCreateResponse.status).to.eq(201);
  expect(state.petId).to.be.a("number");
});

Then("the pet should be retrievable with the expected details", () => {
  return getPet(state.petId, state.accessToken).then((response) => {
    state.petResponse = response;
    expect(response.status).to.eq(200);
    expect(response.body.id).to.eq(state.petId);
    expect(response.body.name).to.eq(state.petName);
    expect(response.body.breed.name).to.eq(state.petBreedName);
    expect(response.body.gender).to.eq("MALE");
    expect(response.body.healthRecords).to.be.an("array").that.is.empty;
    expect(response.body.vaccinationRecords).to.be.an("array").that.is.empty;
  });
});

When("the second user creates a pet named {string} with breed {string}", (name, breedName) => {
  state.petName = name;
  state.petBreedName = breedName;

  return getBreedByName(breedName, state.guestToken).then((breed) => {
    state.petBreedId = breed.id;

    return createPet(state.guestToken, {
      name,
      breedId: breed.id,
      userId: state.guestClaims.userId,
      gender: "FEMALE",
      birthdate: fixedDates.petBirthdate,
      weight: 8.4,
      image: null,
      vaccinationRecordsIds: [],
    }).then((response) => {
      state.petCreateResponse = response;
      state.petId = response.body.id;
    });
  });
});

When("the first user requests the second user's pet", () => {
  return getPet(state.petId, state.ownerToken).then((response) => {
    state.response = response;
  });
});

Then("pet access should be rejected with status 403", () => {
  expect(state.response.status).to.eq(403);
});

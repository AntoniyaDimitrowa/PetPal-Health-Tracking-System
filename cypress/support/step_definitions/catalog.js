import { Before, Then, When } from "@badeball/cypress-cucumber-preprocessor";
import {
  getBreed,
  getBreeds,
  getVaccinations,
} from "../bdd-helpers";
import { resetScenarioState, scenarioState as state } from "../bdd-state";

Before(resetScenarioState);

When("the user requests the breeds catalog", () => {
  return getBreeds(state.accessToken).then((response) => {
    state.breedsResponse = response;
    state.goldenRetrieverBreed = response.body.find(
      (breed) => breed.name === "Golden Retriever"
    );
    state.germanShepherdBreed = response.body.find((breed) => breed.name === "German Shepherd");
  });
});

Then("the breed catalog should include the seeded breeds", () => {
  expect(state.breedsResponse.status).to.eq(200);

  const breedNames = state.breedsResponse.body.map((breed) => breed.name);
  expect(breedNames).to.include("German Shepherd");
  expect(breedNames).to.include("Golden Retriever");
});

When("the user requests the Golden Retriever breed details", () => {
  return getBreed(state.goldenRetrieverBreed.id, state.accessToken).then((response) => {
    state.breedResponse = response;
  });
});

Then("the Golden Retriever breed details should show the expected normal mood", () => {
  expect(state.goldenRetrieverBreed).to.exist;
  expect(state.breedResponse.status).to.eq(200);
  expect(state.breedResponse.body.name).to.eq("Golden Retriever");
  expect(state.breedResponse.body.normalMood.name).to.eq("Playful");
});

When("the user requests the vaccinations catalog", () => {
  return getVaccinations(state.accessToken).then((response) => {
    state.vaccinationsResponse = response;
  });
});

Then("the vaccination catalog should include the seeded vaccinations", () => {
  expect(state.vaccinationsResponse.status).to.eq(200);

  const vaccinationNames = state.vaccinationsResponse.body.map((vaccination) => vaccination.name);
  expect(vaccinationNames).to.include("Rabies");
  expect(vaccinationNames).to.include("Bordetella");
});

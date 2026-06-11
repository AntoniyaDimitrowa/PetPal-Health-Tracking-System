import { Before, Then, When } from "@badeball/cypress-cucumber-preprocessor";
import {
  createVaccinationRecord,
  fixedDates,
  getPet,
  getVaccinationByName,
} from "../bdd-helpers";
import { resetScenarioState, scenarioState as state } from "../bdd-state";

Before(resetScenarioState);

When("the user adds a vaccination record for the pet", () => {
  return getVaccinationByName("Rabies", state.accessToken).then((vaccination) => {
    state.vaccinationRecordPayload = {
      petId: state.petId,
      vaccinationId: vaccination.id,
      date: fixedDates.vaccinationDate,
    };

    return createVaccinationRecord(state.accessToken, state.vaccinationRecordPayload).then(
      (response) => {
        state.vaccinationRecordResponse = response;
        state.vaccinationRecordId = response.body.id;
      }
    );
  });
});

Then("the vaccination record creation should return status 201", () => {
  expect(state.vaccinationRecordResponse.status).to.eq(201);
  expect(state.vaccinationRecordId).to.be.a("number");
});

Then("the pet details should include the new vaccination record", () => {
  return getPet(state.petId, state.accessToken).then((response) => {
    state.petDetailsResponse = response;
    expect(response.status).to.eq(200);
    expect(response.body.vaccinationRecords).to.have.length(1);
    expect(response.body.vaccinationRecords[0].vaccination.name).to.eq("Rabies");
  });
});

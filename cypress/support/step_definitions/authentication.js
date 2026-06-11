import { Before, Given, Then, When } from "@badeball/cypress-cucumber-preprocessor";
import {
  buildUniqueUser,
  decodeJwtPayload,
  getProtectedResource,
  loginUser,
  signupUser,
} from "../bdd-helpers";
import { resetScenarioState, scenarioState as state } from "../bdd-state";

Before(resetScenarioState);

Given("a registered user exists", () => {
  state.user = buildUniqueUser("owner");

  return signupUser(state.user).then((response) => {
    state.signupResponse = response;
  });
});

When("the user logs in with valid credentials", () => {
  return loginUser({
    email: state.user.email,
    password: state.user.password,
  }).then((response) => {
    state.loginResponse = response;
    state.accessToken = response.body.accessToken;
    state.claims = decodeJwtPayload(state.accessToken);
  });
});

Then("the system should return status 201", () => {
  expect(state.loginResponse.status).to.eq(201);
});

Then("an access token should be issued", () => {
  expect(state.accessToken).to.be.a("string").and.not.be.empty;
  expect(state.claims.userId).to.be.a("number");
  expect(state.claims.roles).to.include("Owner");
});

When("the profile is requested for the logged in user", () => {
  return getProtectedResource(`/users/${state.claims.userId}`, state.accessToken).then(
    (response) => {
      state.profileResponse = response;
    }
  );
});

Then("the profile should match the registered user", () => {
  expect(state.profileResponse.status).to.eq(200);
  expect(state.profileResponse.body.email).to.eq(state.user.email);
  expect(state.profileResponse.body.name).to.eq(state.user.name);
  expect(state.profileResponse.body.address).to.eq(state.user.address);
  expect(state.profileResponse.body.role).to.eq("Owner");
});

Given("a registered user with invalid login credentials", () => {
  state.user = buildUniqueUser("invalid-login");
  state.invalidPassword = "WrongPass1!";

  return signupUser(state.user).then((response) => {
    state.signupResponse = response;
  });
});

When("the user attempts to log in with invalid credentials", () => {
  return loginUser(
    {
      email: state.user.email,
      password: state.invalidPassword,
    },
    { failOnStatusCode: false }
  ).then((response) => {
    state.loginResponse = response;
  });
});

Then("invalid credentials should be rejected with status 404", () => {
  expect(state.loginResponse.status).to.eq(404);
});

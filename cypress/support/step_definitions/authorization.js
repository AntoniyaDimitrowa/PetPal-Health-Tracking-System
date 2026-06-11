import { Before, Given, Then, When } from "@badeball/cypress-cucumber-preprocessor";
import {
  buildUniqueUser,
  decodeJwtPayload,
  getAnonymousResource,
  getProtectedResource,
  updateUserBasicProfile,
  signupUser,
} from "../bdd-helpers";
import { resetScenarioState, scenarioState as state } from "../bdd-state";

Before(resetScenarioState);

When("an anonymous user requests the breeds list", () => {
  return getAnonymousResource("/breeds").then((response) => {
    state.response = response;
  });
});

Then("anonymous access should be rejected with status 401", () => {
  expect(state.response.status).to.eq(401);
});

Given("two registered users exist", () => {
  state.owner = buildUniqueUser("owner");
  state.guest = buildUniqueUser("guest");

  return signupUser(state.owner)
    .then((ownerResponse) => {
      state.ownerToken = ownerResponse.body.accessToken;
      state.ownerClaims = decodeJwtPayload(state.ownerToken);

      return signupUser(state.guest);
    })
    .then((guestResponse) => {
      state.guestToken = guestResponse.body.accessToken;
      state.guestClaims = decodeJwtPayload(state.guestToken);
    });
});

When("the first user requests the second user's profile", () => {
  return getProtectedResource(`/users/${state.guestClaims.userId}`, state.ownerToken).then(
    (response) => {
      state.response = response;
    }
  );
});

Then("profile access should be rejected with status 403", () => {
  expect(state.response.status).to.eq(403);
});

When("the first user tries to update the second user's profile", () => {
  return updateUserBasicProfile(
    state.guestClaims.userId,
    state.ownerToken,
    {
      name: state.guest.name,
      email: state.guest.email,
      address: state.guest.address,
      image: null,
    },
    { failOnStatusCode: false }
  ).then((response) => {
    state.response = response;
  });
});

Then("profile update should be rejected with status 403", () => {
  expect(state.response.status).to.eq(403);
});

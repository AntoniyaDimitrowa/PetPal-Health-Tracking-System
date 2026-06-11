import { Before, Then, When } from "@badeball/cypress-cucumber-preprocessor";
import {
  decodeJwtPayload,
  getProtectedResource,
  loginUser,
  updateUserBasicProfile,
  updateUserWithPassword,
} from "../bdd-helpers";
import { resetScenarioState, scenarioState as state } from "../bdd-state";

Before(resetScenarioState);

When("the user updates their name to {string} and address to {string}", (name, address) => {
  state.updatedName = name;
  state.updatedAddress = address;

  return updateUserBasicProfile(
    state.claims.userId,
    state.accessToken,
    {
      name,
      email: state.user.email,
      address,
      image: state.user.image ?? null,
    }
  ).then((response) => {
    state.updateResponse = response;
  });
});

Then("the basic profile update should return status 204", () => {
  expect(state.updateResponse.status).to.eq(204);
});

Then("the profile should contain the updated name and address", () => {
  return getProtectedResource(`/users/${state.claims.userId}`, state.accessToken).then(
    (response) => {
      state.profileResponse = response;
      expect(response.status).to.eq(200);
      expect(response.body.name).to.eq(state.updatedName);
      expect(response.body.address).to.eq(state.updatedAddress);
      expect(response.body.email).to.eq(state.user.email);
      expect(response.body.role).to.eq("Owner");
    }
  );
});

When("the user changes their password to {string}", (newPassword) => {
  state.newPassword = newPassword;

  return updateUserWithPassword(
    state.claims.userId,
    state.accessToken,
    {
      name: state.user.name,
      email: state.user.email,
      oldPassword: state.user.password,
      newPassword,
      address: state.user.address,
      image: state.user.image ?? null,
    }
  ).then((response) => {
    state.updateResponse = response;
  });
});

Then("the secure profile update should return status 204", () => {
  expect(state.updateResponse.status).to.eq(204);
});

Then("the user can log in with the new password", () => {
  return loginUser({
    email: state.user.email,
    password: state.newPassword,
  }).then((response) => {
    state.reloginResponse = response;
    expect(response.status).to.eq(201);

    const claims = decodeJwtPayload(response.body.accessToken);
    expect(claims.userId).to.eq(state.claims.userId);
  });
});

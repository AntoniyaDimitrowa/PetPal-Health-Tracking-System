const DEFAULT_API_BASE_URL = "http://localhost:8081/backend";
const DEFAULT_PASSWORD = Cypress.env("TEST_PASSWORD") || "change-me-local-test-password";

const normalizeLabel = (label) =>
  label
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, "-")
    .replace(/^-+|-+$/g, "");

export const apiBaseUrl = () => Cypress.env("apiBaseUrl") || DEFAULT_API_BASE_URL;

const buildIsoDateYearsAgo = (years) => {
  const date = new Date();
  date.setFullYear(date.getFullYear() - years);
  return date.toISOString();
};

const now = new Date();

export const fixedDates = {
  petBirthdate: buildIsoDateYearsAgo(2),
  healthRecordDate: now.toISOString(),
  vaccinationDate: now.toISOString(),
  statisticsMonth: now.getMonth() + 1,
  statisticsYear: now.getFullYear(),
};

export const seededDates = fixedDates;

export const buildUniqueUser = (label = "bdd") => {
  const safeLabel = normalizeLabel(label) || "bdd";
  const suffix = `${safeLabel}-${Date.now()}-${Cypress._.random(1000, 9999)}`;

  return {
    name: `BDD User ${suffix}`,
    email: `${suffix}@example.com`,
    address: "Eindhoven, Netherlands",
    password: DEFAULT_PASSWORD,
  };
};

export const decodeJwtPayload = (token) => {
  const payload = token.split(".")[1].replace(/-/g, "+").replace(/_/g, "/");
  const paddedPayload = payload.padEnd(payload.length + ((4 - (payload.length % 4)) % 4), "=");
  const decodedPayload = Cypress.Buffer.from(paddedPayload, "base64").toString("utf8");

  return JSON.parse(decodedPayload);
};

export const authHeaders = (token) => ({
  Authorization: `Bearer ${token}`,
});

export const signupUser = (user, options = {}) =>
  cy.request({
    method: "POST",
    url: `${apiBaseUrl()}/authentication/signup`,
    body: user,
    failOnStatusCode: options.failOnStatusCode ?? true,
    headers: {
      "Content-Type": "application/json",
      ...(options.headers ?? {}),
    },
  });

const requestWithBody = (method, path, token, body, options = {}) =>
  cy.request({
    method,
    url: `${apiBaseUrl()}${path}`,
    body,
    failOnStatusCode: options.failOnStatusCode ?? true,
    headers: {
      "Content-Type": "application/json",
      ...(token ? authHeaders(token) : {}),
      ...(options.headers ?? {}),
    },
  });

export const loginUser = (credentials, options = {}) =>
  cy.request({
    method: "POST",
    url: `${apiBaseUrl()}/authentication/login`,
    body: credentials,
    failOnStatusCode: options.failOnStatusCode ?? true,
    headers: {
      "Content-Type": "application/json",
      ...(options.headers ?? {}),
    },
  });

export const getProtectedResource = (path, token, options = {}) =>
  cy.request({
    method: "GET",
    url: `${apiBaseUrl()}${path}`,
    failOnStatusCode: options.failOnStatusCode ?? false,
    headers: {
      ...authHeaders(token),
      ...(options.headers ?? {}),
    },
  });

export const getCollection = (path, token, options = {}) =>
  getProtectedResource(path, token, options);

export const getBreedByName = (breedName, token, options = {}) =>
  getBreeds(token, options).then((response) => {
    const breed = response.body.find((item) => item.name === breedName);
    expect(breed, `Breed ${breedName} should exist`).to.exist;
    return breed;
  });

export const getVaccinationByName = (vaccinationName, token, options = {}) =>
  getVaccinations(token, options).then((response) => {
    const vaccination = response.body.find((item) => item.name === vaccinationName);
    expect(vaccination, `Vaccination ${vaccinationName} should exist`).to.exist;
    return vaccination;
  });

export const updateUserBasicProfile = (userId, token, payload, options = {}) =>
  requestWithBody("PUT", `/users/${userId}/basic`, token, payload, options);

export const updateUserWithPassword = (userId, token, payload, options = {}) =>
  requestWithBody("PUT", `/users/${userId}/secure`, token, payload, options);

export const getAnonymousResource = (path, options = {}) =>
  cy.request({
    method: "GET",
    url: `${apiBaseUrl()}${path}`,
    failOnStatusCode: options.failOnStatusCode ?? false,
    headers: {
      ...(options.headers ?? {}),
    },
  });

export const createPet = (token, payload, options = {}) =>
  requestWithBody(
    "POST",
    "/pets",
    token,
    {
      ...payload,
      image: payload.image ?? "",
    },
    options
  );

export const getPet = (petId, token, options = {}) =>
  getProtectedResource(`/pets/${petId}`, token, options);

export const createHealthRecord = (petId, token, payload, options = {}) =>
  requestWithBody("POST", `/health/pets/${petId}/records`, token, payload, options);

export const getHealthRecords = (petId, token, options = {}) =>
  getProtectedResource(`/health/pets/${petId}/records`, token, options);

export const getPetStatistics = (petId, token, month, year, options = {}) =>
  getProtectedResource(
    `/health/pets/${petId}/statistics?month=${month}&year=${year}`,
    token,
    options
  );

export const createVaccinationRecord = (token, payload, options = {}) =>
  requestWithBody("POST", "/vaccinations", token, payload, options);

export const getBreeds = (token, options = {}) => getCollection("/breeds", token, options);

export const getBreed = (breedId, token, options = {}) =>
  getProtectedResource(`/breeds/${breedId}`, token, options);

export const getVaccinations = (token, options = {}) =>
  getCollection("/vaccinations", token, options);

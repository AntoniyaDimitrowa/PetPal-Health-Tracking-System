import http from "k6/http";
import { check, fail, sleep } from "k6";

const baseUrl = __ENV.API_BASE_URL || "http://petpal-backend-test-service:8080/backend";
    const password = __ENV.TEST_PASSWORD || __ENV.DB_PASSWORD_TEST || "change-me-local-test-password";
const jsonHeaders = {
  "Content-Type": "application/json",
};

export const options = {
  scenarios: {
    catalog_read: {
      executor: "ramping-vus",
      stages: [
        { duration: "30s", target: 5 },
        { duration: "60s", target: 15 },
        { duration: "30s", target: 0 },
      ],
      gracefulRampDown: "10s",
    },
  },
  thresholds: {
    checks: ["rate==1"],
    http_req_failed: ["rate<0.01"],
    http_req_duration: ["p(95)<1000"],
  },
};

export function setup() {
  const suffix = `${Date.now()}-${Math.floor(Math.random() * 100000)}`;
  const user = {
    name: `Load Test ${suffix}`,
    email: `loadtest-${suffix}@example.com`,
    address: "Eindhoven, Netherlands",
    password,
  };

  const signupResponse = http.post(
    `${baseUrl}/authentication/signup`,
    JSON.stringify(user),
    { headers: jsonHeaders }
  );

  check(signupResponse, {
    "signup succeeded": (response) => response.status === 200,
    "signup returned a token": (response) =>
      response.status === 200 && Boolean(response.json("accessToken")),
  });

  if (signupResponse.status !== 200) {
    fail(`Setup signup failed with status ${signupResponse.status}`);
  }

  return {
    email: user.email,
    password: user.password,
  };
}

export default function (data) {
  const loginResponse = http.post(
    `${baseUrl}/authentication/login`,
    JSON.stringify({
      email: data.email,
      password: data.password,
    }),
    { headers: jsonHeaders }
  );

  check(loginResponse, {
    "login succeeded": (response) => response.status === 201,
    "login returned a token": (response) =>
      response.status === 201 && Boolean(response.json("accessToken")),
  });

  const accessToken = loginResponse.json("accessToken");
  const authHeaders = {
    Authorization: `Bearer ${accessToken}`,
  };

  const breedsResponse = http.get(`${baseUrl}/breeds`, {
    headers: authHeaders,
  });
  const vaccinationsResponse = http.get(`${baseUrl}/vaccinations`, {
    headers: authHeaders,
  });

  check(breedsResponse, {
    "breeds request succeeded": (response) => response.status === 200,
    "breeds response has expected data": (response) => {
      const breeds = response.json();
      return (
        Array.isArray(breeds) &&
        breeds.some((breed) => breed.name === "Golden Retriever") &&
        breeds.some((breed) => breed.name === "German Shepherd")
      );
    },
  });

  check(vaccinationsResponse, {
    "vaccinations request succeeded": (response) => response.status === 200,
    "vaccinations response has expected data": (response) => {
      const vaccinations = response.json();
      return (
        Array.isArray(vaccinations) &&
        vaccinations.some((vaccination) => vaccination.name === "Rabies") &&
        vaccinations.some((vaccination) => vaccination.name === "Bordetella")
      );
    },
  });

  sleep(1);
}

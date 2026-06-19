import encoding from "k6/encoding";
import http from "k6/http";
import { check, fail, sleep } from "k6";

const baseUrl = __ENV.API_BASE_URL || "http://petpal-backend-test-service:8080/backend";
const password = __ENV.TEST_PASSWORD || "PetPal@1234";
const jsonHeaders = {
  "Content-Type": "application/json",
};

const toIsoDateYearsAgo = (years) => {
  const date = new Date();
  date.setFullYear(date.getFullYear() - years);
  return date.toISOString();
};

const now = new Date();
const month = now.getMonth() + 1;
const year = now.getFullYear();
const healthRecordDate = now.toISOString();
const petBirthdate = toIsoDateYearsAgo(2);

const decodeJwtPayload = (token) => {
  const payload = token.split(".")[1];
  const decoded = encoding.b64decode(payload, "rawurl");
  const decodedJson = String.fromCharCode.apply(null, new Uint8Array(decoded));
  return JSON.parse(decodedJson);
};

export const options = {
  scenarios: {
    pet_health_write: {
      executor: "ramping-vus",
      stages: [
        { duration: "20s", target: 2 },
        { duration: "40s", target: 5 },
        { duration: "20s", target: 0 },
      ],
      gracefulRampDown: "10s",
    },
  },
  thresholds: {
    checks: ["rate==1"],
    http_req_failed: ["rate<0.01"],
    http_req_duration: ["p(95)<1200"],
  },
};

export function setup() {
  const suffix = `${Date.now()}-${Math.floor(Math.random() * 100000)}`;
  const user = {
    name: `Load Test ${suffix}`,
    email: `petloadtest-${suffix}@example.com`,
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

  const accessToken = signupResponse.json("accessToken");
  const claims = decodeJwtPayload(accessToken);

  const breedsResponse = http.get(`${baseUrl}/breeds`, {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });

  check(breedsResponse, {
    "breeds lookup succeeded": (response) => response.status === 200,
  });

  const breeds = breedsResponse.json();
  const goldenRetriever = breeds.find((breed) => breed.name === "Golden Retriever");

  if (!goldenRetriever) {
    fail("Golden Retriever breed was not found in the catalog");
  }

      const petResponse = http.post(
        `${baseUrl}/pets`,
        JSON.stringify({
          name: `Load Pet ${suffix}`,
          breedId: goldenRetriever.id,
          userId: claims.userId,
          gender: "MALE",
          birthdate: petBirthdate,
          weight: 12.5,
          image: "",
          vaccinationRecordsIds: [],
        }),
        {
          headers: Object.assign({}, jsonHeaders, {
            Authorization: `Bearer ${accessToken}`,
          }),
        }
      );

  check(petResponse, {
    "pet creation succeeded": (response) => response.status === 201,
    "pet id returned": (response) => response.status === 201 && Boolean(response.json("id")),
  });

  if (petResponse.status !== 201) {
    fail(`Setup pet creation failed with status ${petResponse.status}`);
  }

  return {
    email: user.email,
    password: user.password,
    petId: petResponse.json("id"),
    breedName: goldenRetriever.name,
    moodName: goldenRetriever.normalMood.name,
    moodId: goldenRetriever.normalMood.id,
    month,
    year,
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

      const healthRecordResponse = http.post(
        `${baseUrl}/health/pets/${data.petId}/records`,
        JSON.stringify({
          date: healthRecordDate,
      foodIntake: 250.0,
      waterIntake: 1.8,
      moodId: data.moodId,
      activityLevel: 7,
      socialInteraction: "Trusted",
      notes: `Load test iteration ${__ITER}`,
        }),
        {
          headers: Object.assign({}, jsonHeaders, authHeaders),
        }
      );

  check(healthRecordResponse, {
    "health record creation succeeded": (response) => response.status === 201,
  });

  const recordsResponse = http.get(`${baseUrl}/health/pets/${data.petId}/records`, {
    headers: authHeaders,
  });
  const statsResponse = http.get(
    `${baseUrl}/health/pets/${data.petId}/statistics?month=${data.month}&year=${data.year}`,
    {
      headers: authHeaders,
    }
  );

  check(recordsResponse, {
    "health records request succeeded": (response) => response.status === 200,
  });

  check(statsResponse, {
    "statistics request succeeded": (response) => response.status === 200,
    "statistics contains data": (response) => {
      const payload = response.json();
      return (
        Array.isArray(payload.foodIntake) &&
        payload.foodIntake.length > 0 &&
        payload.foodIntake[0].norm === 300 &&
        payload.waterIntake[0].norm === 1.2 &&
        payload.activityLevel[0].norm === 1.5 &&
        payload.moodDistribution[0].mood === data.moodName
      );
    },
  });

  sleep(1);
}

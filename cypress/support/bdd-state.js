export const scenarioState = {};

export const resetScenarioState = () => {
  Object.keys(scenarioState).forEach((key) => {
    delete scenarioState[key];
  });
};

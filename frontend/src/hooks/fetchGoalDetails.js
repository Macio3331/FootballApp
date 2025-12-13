import api from "../api/api";

export const fetchGoalDetails = async (goalData, logError) => {
  if (!goalData) return;
  var scorer;
  var assistant;
  var match;
  try {
    const _scorer = await api.get(goalData.scorer);
    if (_scorer) scorer = _scorer.data;
    const _assistant = await api.get(goalData.assistant);
    if (_assistant) assistant = _assistant.data;
    const _match = await api.get(goalData.match);
    if (_match) match = _match.data;
  } catch (err) {
    logError(err);
  }
  return { scorer, assistant, match };
};

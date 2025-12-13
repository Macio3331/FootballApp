import api from "../api/api";

export const fetchPlayerDetails = async (playerData, logError) => {
  if (!playerData) return;
  var club;
  var matches;
  var goals;
  var assists;
  try {
    const _club = await api.get(playerData.club);
    if (_club) club = _club.data;
    const _matches = await api.get(playerData.matches);
    if (_matches) matches = _matches.data;
    const _goals = await api.get(playerData.goals);
    if (_goals) goals = _goals.data;
    const _assists = await api.get(playerData.assists);
    if (_assists) assists = _assists.data;
  } catch (err) {
    logError(err);
  }
  return { club, matches, goals, assists };
};

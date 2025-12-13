import api from "../api/api";

export const fetchClubDetails = async (clubData, logError) => {
  if (!clubData) return;
  var players = [];
  var homeMatches = [];
  var awayMatches = [];
  try {
    const _players = await api.get(clubData.players);
    if (_players) players = _players.data;
    const _homeMatches = await api.get(clubData.homeMatches);
    if (_homeMatches) homeMatches = _homeMatches.data;
    const _awayMatches = await api.get(clubData.awayMatches);
    if (_awayMatches) awayMatches = _awayMatches.data;
  } catch (err) {
    logError(err);
  }
  return { players, homeMatches, awayMatches };
};

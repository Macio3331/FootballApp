import api from "../api/api";

export const fetchGameDetails = async (gameData, logError) => {
  if (!gameData) return;
  var players = [];
  var goals = [];
  var homeClub;
  var awayClub;
  try {
    const _players = await api.get(gameData.players);

    if (_players) players = _players.data;
    const _goals = await api.get(gameData.goals);
    if (_goals) goals = _goals.data;
    const _homeClub = await api.get(gameData.homeClub);
    if (_homeClub) homeClub = _homeClub.data;
    const _awayClub = await api.get(gameData.awayClub);
    if (_awayClub) awayClub = _awayClub.data;
  } catch (err) {
    logError(err);
  }
  return { players, homeClub, awayClub, goals };
};

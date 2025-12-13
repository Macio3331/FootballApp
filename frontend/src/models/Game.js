export class Game {
  constructor(
    id,
    date,
    awayResult = null,
    homeResult = null,
    location,
    played,
    goals = new Set(),
    homeClub,
    awayClub,
    players = new Set()
  ) {
    this.id = id;
    this.date = date;
    this.awayResult = awayResult;
    this.homeResult = homeResult;
    this.location = location;
    this.played = played;
    this.goals = goals;
    this.homeClub = homeClub;
    this.awayClub = awayClub;
    this.players = players;
  }
}

export class Club {
  constructor(
    id,
    name,
    location,
    dateOfCreation,
    players = new Set(),
    homeMatches = new Set(),
    awayMatches = new Set()
  ) {
    this.id = id;
    this.name = name;
    this.location = location;
    this.dateOfCreation = dateOfCreation;
    this.players = players;
    this.homeMatches = homeMatches;
    this.awayMatches = awayMatches;
  }
}

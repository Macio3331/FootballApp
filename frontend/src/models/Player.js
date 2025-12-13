export class Player {
  constructor(
    id,
    name,
    surname,
    number,
    position,
    goals = new Set(),
    assists = new Set(),
    club,
    matches = new Set()
  ) {
    this.id = id;
    this.name = name;
    this.surname = surname;
    this.number = number;
    this.position = position;
    this.goals = goals;
    this.assists = assists;
    this.club = club;
    this.matches = matches;
  }
}

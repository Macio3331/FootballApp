export class Goal {
  constructor(id, minute, ownGoal, scorer, assistant, match) {
    this.id = id;
    this.minute = minute;
    this.ownGoal = ownGoal;
    this.scorer = scorer;
    this.assistant = assistant;
    this.match = match;
  }
}

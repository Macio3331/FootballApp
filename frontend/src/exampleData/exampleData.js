import { Player } from "../models/Player";
import { Game } from "../models/Game";
import { Club } from "../models/Club";
const examplePlayers = new Set([
  new Player(1, "John", "Doe", 10, "Forward"),
  new Player(2, "Jane", "Smith", 8, "Midfielder"),
]);

const exampleHomeMatches = new Set([
  new Game(1, new Date("2023-07-04"), 3, 1, "Home Stadium", true),
  new Game(2, new Date("2023-07-11"), 2, 2, "Home Stadium", true),
]);

const exampleAwayMatches = new Set([
  new Game(3, new Date("2023-07-18"), 1, 3, "Away Stadium", true),
  new Game(4, new Date("2023-07-25"), 0, 2, "Away Stadium", true),
]);

export const exampleClubs = [
  new Club(
    1,
    "Example Club",
    "Example City",
    new Date("1900-01-01"),
    examplePlayers,
    exampleHomeMatches,
    exampleAwayMatches
  ),
  new Club(
    2,
    "Example Club 2",
    "Example City 2",
    new Date("1900-02-02"),
    examplePlayers,
    exampleHomeMatches,
    exampleAwayMatches
  ),
];

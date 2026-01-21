import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import Navbar from "./components/navbar/Navbar";
import ScrollToTop from "./components/general/ScrollToTop";
import Clubs from "./pages/clubs/Clubs";
import ClubDetails from "./pages/clubs/ClubDetails";
import Players from "./pages/players/Players";
import Games from "./pages/games/Games";
import Goals from "./pages/goals/Goals";
import CreateClub from "./pages/clubs/CreateClub";
import GameDetails from "./pages/games/GameDetails";
import CreateGame from "./pages/games/CreateGame";
import PlayerDetails from "./pages/players/PlayerDetails";
import CreatePlayer from "./pages/players/CreatePlayer";
import GoalDetails from "./pages/goals/GoalDetails";
import CreateGoal from "./pages/goals/CreateGoal";
import League from "./pages/league/League";
import Schedule from "./pages/league/Schedule";
function AppRouter() {
  return (
    <>
      <Navbar />
      <ScrollToTop />
      <Routes>
        <Route path="/clubs" element={<Clubs />} />
        <Route path="/club/:id" element={<ClubDetails />} />
        <Route path="/club/new" element={<CreateClub edit={false} />} />
        <Route path="/club/edit/:id" element={<CreateClub edit={true} />} />

        <Route path="/players" element={<Players />} />
        <Route path="/player/:id" element={<PlayerDetails />} />
        <Route path="/player/new" element={<CreatePlayer edit={false} />} />
        <Route path="/player/edit/:id" element={<CreatePlayer edit={true} />} />

        <Route path="/games" element={<Games />} />
        <Route path="/game/:id" element={<GameDetails />} />
        <Route path="/game/new" element={<CreateGame edit={false} />} />
        <Route path="/game/edit/:id" element={<CreateGame edit={true} />} />

        <Route path="/goals" element={<Goals />} />
        <Route path="/goal/:id" element={<GoalDetails />} />
        <Route path="/goal/new" element={<CreateGoal edit={false} />} />
        <Route path="/goal/edit/:id" element={<CreateGoal edit={true} />} />

        <Route path="/league" element={<League />} />
        <Route path="/schedule" element={<Schedule />} />

        <Route path="*" element={<Navigate to="/clubs" />} />
      </Routes>
    </>
  );
}

export default AppRouter;

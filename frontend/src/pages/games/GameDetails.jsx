import React, { useDebugValue, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import DetailsPageWrapper from "../DetailsPageWrapper";
import GameCard from "../../components/containers/cards/GameCard";
import { fetchGameDetails } from "../../hooks/fetchGameDetails";
import { usePopupContext } from "../../context/PopupContext";
import { useFetchData } from "../../hooks/useFetchData";
import ScrollableNavContainer from "../../components/containers/ScrollableNavContainer";
import api from "../../api/api";
import ClubCard from "../../components/containers/cards/ClubCard";
import NavCard from "../../components/containers/NavCard";
import GoalCard from "../../components/containers/cards/GoalCard";
import PlayerCard from "../../components/containers/cards/PlayerCard";

function GameDetails() {
  const { id } = useParams();
  const { data, isPending } = useFetchData("/matches", id);
  const { logError, addMessage } = usePopupContext();

  const [players, setPlayers] = useState([]);
  const [goals, setGoals] = useState([]);
  const [homeClub, setHomeClub] = useState();
  const [awayClub, setAwayClub] = useState();

  const navigate = useNavigate();

  useEffect(() => {
    const loadDetails = async () => {
      if (data) {
        const { players, homeClub, awayClub, goals } = await fetchGameDetails(
          data,
          logError
        );
        setPlayers(players);
        setHomeClub(homeClub);
        setAwayClub(awayClub);
        setGoals(goals);
      }
    };
    loadDetails();
  }, [data]);

  const onDelete = async () => {
    try {
      const res = await api.delete(`/matches/${data?.id}`);
      if (res) {
        addMessage("Game deleted successfully");
        navigate("/games");
      }
    } catch (err) {
      logError(err);
    }
  };

  return (
    <DetailsPageWrapper
      header={`Game details`}
      isPending={isPending}
      data={data}
      editPageLink={"/game/edit"}
      deleteMethod={onDelete}
    >
      <div className="flex flex-row items-center justify-center gap-8 bg-background-50 p-4 rounded-md shadow-md">
        <GameCard data={data} />
      </div>

      <h1 className="text-2xl">Home Club</h1>
      <NavCard
        navigationPrefix={"club"}
        data={homeClub}
        CardContent={ClubCard}
      />
      <h1 className="text-2xl">Away Club</h1>
      <NavCard
        navigationPrefix={"club"}
        data={awayClub}
        CardContent={ClubCard}
      />
      <h1 className="text-2xl">Players</h1>
      <ScrollableNavContainer
        data={players}
        isPending={false}
        navigationPrefix={"player"}
        CardContent={PlayerCard}
      />
      <h1 className="text-2xl">Goals</h1>
      <ScrollableNavContainer
        data={goals}
        isPending={false}
        navigationPrefix={"goal"}
        CardContent={GoalCard}
      />
    </DetailsPageWrapper>
  );
}

export default GameDetails;

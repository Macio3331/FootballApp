import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import DetailsPageWrapper from "../DetailsPageWrapper";
import PlayerCard from "../../components/containers/cards/PlayerCard";
import { fetchPlayerDetails } from "../../hooks/fetchPlayerDetails";
import { usePopupContext } from "../../context/PopupContext";
import { useFetchData } from "../../hooks/useFetchData";
import ScrollableNavContainer from "../../components/containers/ScrollableNavContainer";
import api from "../../api/api";
import GameCard from "../../components/containers/cards/GameCard";
import ClubCard from "../../components/containers/cards/ClubCard";
import NavCard from "../../components/containers/NavCard";
import GoalCard from "../../components/containers/cards/GoalCard";

function PlayerDetails() {
  const { id } = useParams();
  const { data, isPending } = useFetchData("/players", id);
  const { logError, addMessage } = usePopupContext();

  const [club, setClub] = useState([]);
  const [matches, setMatches] = useState([]);
  const [goals, setGoals] = useState();
  const [assists, setAssists] = useState();

  const navigate = useNavigate();

  useEffect(() => {
    const loadDetails = async () => {
      if (data) {
        const { club, matches, goals, assists } = await fetchPlayerDetails(
          data,
          logError
        );
        setClub(club);
        setMatches(matches);
        setGoals(goals);
        setAssists(assists);
      }
    };
    loadDetails();
  }, [data, logError]);

  const onDelete = async () => {
    try {
      const res = await api.delete(`/players/${data?.id}`);
      if (res) {
        addMessage("Player deleted successfully");
        navigate("/players");
      }
    } catch (err) {
      logError(err);
    }
  };

  return (
    <DetailsPageWrapper
      header={`Player details`}
      isPending={isPending}
      data={data}
      editPageLink={"/player/edit"}
      deleteMethod={onDelete}
    >
      <div className="flex flex-row items-center justify-center gap-8 bg-background-50 p-4 rounded-md shadow-md">
        <PlayerCard data={data} />
      </div>

      <h1 className="text-2xl">Club</h1>
      <NavCard navigationPrefix={"club"} data={club} CardContent={ClubCard} />

      <h1 className="text-2xl">Matches</h1>
      <ScrollableNavContainer
        data={matches}
        isPending={false}
        navigationPrefix={"game"}
        CardContent={GameCard}
      />
      <h1 className="text-2xl">Goals</h1>
      <ScrollableNavContainer
        data={goals}
        isPending={false}
        navigationPrefix={"goal"}
        CardContent={GoalCard}
      />
      <h1 className="text-2xl">Assists</h1>
      <ScrollableNavContainer
        data={assists}
        isPending={false}
        navigationPrefix={"goal"}
        CardContent={GoalCard}
      />
    </DetailsPageWrapper>
  );
}

export default PlayerDetails;

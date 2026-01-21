import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import DetailsPageWrapper from "../DetailsPageWrapper";
import ClubCard from "../../components/containers/cards/ClubCard";
import { fetchClubDetails } from "../../hooks/fetchClubDetails";
import { usePopupContext } from "../../context/PopupContext";
import { useFetchData } from "../../hooks/useFetchData";
import ScrollableNavContainer from "../../components/containers/ScrollableNavContainer";
import api from "../../api/api";
import GameCard from "../../components/containers/cards/GameCard";
import PlayerCard from "../../components/containers/cards/PlayerCard";

function ClubDetails() {
  const { id } = useParams();
  const { data, isPending } = useFetchData("/clubs", id);
  const { logError, addMessage } = usePopupContext();

  const [players, setPlayers] = useState([]);
  const [homeMatches, setHomeMatches] = useState([]);
  const [awayMatches, setAwayMatches] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {
    const loadDetails = async () => {
      if (data) {
        const { players, homeMatches, awayMatches } = await fetchClubDetails(
          data,
          logError
        );
        setPlayers(players);
        setHomeMatches(homeMatches);
        setAwayMatches(awayMatches);
      }
    };
    loadDetails();
  }, [data, logError]);

  const onDelete = async () => {
    try {
      const res = await api.delete(`/clubs/${data?.id}`);
      if (res) {
        addMessage("Club deleted successfully");
        navigate("/");
      }
    } catch (err) {
      logError(err);
    }
  };

  return (
    <DetailsPageWrapper
      header={`Club details`}
      isPending={isPending}
      data={data}
      editPageLink={"/club/edit"}
      deleteMethod={onDelete}
    >
      <div className="flex flex-row items-center justify-center gap-8 bg-background-50 p-4 rounded-md shadow-md">
        <ClubCard data={data} />
      </div>
      <h1 className="text-2xl">Players</h1>
      <ScrollableNavContainer
        data={players}
        isPending={false}
        navigationPrefix={"player"}
        CardContent={PlayerCard}
      />
      <h1 className="text-2xl">Home Matches</h1>
      <ScrollableNavContainer
        data={homeMatches}
        isPending={false}
        navigationPrefix={"game"}
        CardContent={GameCard}
      />
      <h1 className="text-2xl">Away Matches</h1>
      <ScrollableNavContainer
        data={awayMatches}
        isPending={false}
        navigationPrefix={"game"}
        CardContent={GameCard}
      />
    </DetailsPageWrapper>
  );
}

export default ClubDetails;

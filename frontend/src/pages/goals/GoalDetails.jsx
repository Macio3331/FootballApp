import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import DetailsPageWrapper from "../DetailsPageWrapper";
import GoalCard from "../../components/containers/cards/GoalCard";
import { fetchGoalDetails } from "../../hooks/fetchGoalDetails";
import { usePopupContext } from "../../context/PopupContext";
import { useFetchData } from "../../hooks/useFetchData";
import api from "../../api/api";
import GameCard from "../../components/containers/cards/GameCard";
import NavCard from "../../components/containers/NavCard";
import PlayerCard from "../../components/containers/cards/PlayerCard";

function GoalDetails() {
  const { id } = useParams();
  const { data, isPending } = useFetchData("/goals", id);
  const { logError, addMessage } = usePopupContext();

  const [match, setMatch] = useState();
  const [scorer, setScorer] = useState();
  const [assistant, setAssistant] = useState();

  const navigate = useNavigate();

  useEffect(() => {
    const loadDetails = async () => {
      if (data) {
        const { match, scorer, assistant } = await fetchGoalDetails(
          data,
          logError
        );
        setMatch(match);
        setScorer(scorer);
        setAssistant(assistant);
      }
    };
    loadDetails();
  }, [data, logError]);

  const onDelete = async () => {
    try {
      const res = await api.delete(`/goals/${data?.id}`);
      if (res) {
        addMessage("Goal deleted successfully");
        navigate("/goals");
      }
    } catch (err) {
      logError(err);
    }
  };

  return (
    <DetailsPageWrapper
      header={`Goal details`}
      isPending={isPending}
      data={data}
      editPageLink={"/goal/edit"}
      deleteMethod={onDelete}
    >
      <div className="flex flex-row items-center justify-center gap-8 bg-background-50 p-4 rounded-md shadow-md">
        <GoalCard data={data} />
      </div>
      <h1 className="text-2xl">Match</h1>
      <NavCard navigationPrefix={"game"} data={match} CardContent={GameCard} />
      <h1 className="text-2xl">Scorer</h1>
      <NavCard
        navigationPrefix={"player"}
        data={scorer}
        CardContent={PlayerCard}
      />
      <h1 className="text-2xl">Assistant</h1>
      <NavCard
        navigationPrefix={"player"}
        data={assistant}
        CardContent={PlayerCard}
      />
    </DetailsPageWrapper>
  );
}

export default GoalDetails;

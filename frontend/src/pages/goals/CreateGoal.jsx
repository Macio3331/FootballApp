import React, { useEffect, useState } from "react";
import CreatePageWrapper from "../CreatePageWrapper";
import CustomButton from "../../components/general/CustomButton";
import { usePopupContext } from "../../context/PopupContext";
import api from "../../api/api";
import { useNavigate, useParams } from "react-router-dom";
import SelectContainer from "../../components/containers/SelectContainer";
import PlayerCard from "../../components/containers/cards/PlayerCard";
import GameCard from "../../components/containers/cards/GameCard";

function CreateGoal({ edit }) {
  const { id } = useParams();

  const [minute, setMinute] = useState(1);
  const [ownGoal, setOwnGoal] = useState(false);
  const [scorerId, setScorerId] = useState();
  const [assistantId, setAssistantId] = useState();
  const [matchId, setMatchId] = useState();

  const { logError, addMessage } = usePopupContext();

  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await api.get(`/goals/${id}`);
        const goal = res.data;
        setMinute(goal.minute);
        setOwnGoal(goal.ownGoal);
        setScorerId(goal.scorerId);
        setAssistantId(goal.assistantId);
        setMatchId(goal.matchId);
      } catch (err) {
        logError(err);
      }
    };
    if (edit && id) {
      fetchData();
    }
  }, [edit, id, logError]);

  const onSubmit = async (e) => {
    e.preventDefault();
    const body = { minute, ownGoal, scorerId, assistantId, matchId };
    try {
      const res =
        edit && id
          ? await api.put(`/goals/${id}`, body)
          : await api.post("/goals", body);
      if (res) {
        addMessage(`Goal ${edit ? "edited" : "created"} successfully`);
        navigate("/goals");
      }
    } catch (err) {
      logError(err);
    }
  };
  return (
    <CreatePageWrapper header={edit ? "Edit goal" : "New goal"}>
      <form
        onSubmit={onSubmit}
        className="flex flex-col gap-2 items-center justify-center"
      >
        <label>Minute:</label>
        <input
          type="number"
          min={0}
          required
          value={minute}
          className="p-2 w-screen max-w-3xl"
          onChange={(e) => {
            setMinute(e.target.value);
          }}
        />
        <label>Own goal:</label>
        <input
          type="checkbox"
          value={ownGoal}
          className="p-2 w-screen max-w-3xl"
          onChange={(e) => {
            setOwnGoal(e.target.checked);
          }}
        />
        <label className="text-2xl">Match:</label>
        <div className=" max-h-[30rem] w-screen max-w-4xl overflow-y-scroll">
          <SelectContainer
            apiUrl={`/matches`}
            setMethod={setMatchId}
            CardContent={GameCard}
          />
        </div>
        {matchId && (
          <>
            <label className="text-2xl">Scorer:</label>
            <div className="h-[30rem] w-screen max-w-4xl overflow-y-scroll">
              <SelectContainer
                apiUrl={`/matches/${matchId}/players`}
                setMethod={setScorerId}
                CardContent={PlayerCard}
              />
            </div>
            <label className="text-2xl">Assistant:</label>
            <div className="h-[30rem] w-screen max-w-4xl overflow-y-scroll">
              <SelectContainer
                apiUrl={`/matches/${matchId}/players`}
                setMethod={setAssistantId}
                CardContent={PlayerCard}
              />
            </div>
          </>
        )}

        <CustomButton>{edit ? "Edit" : "Create"}</CustomButton>
      </form>
    </CreatePageWrapper>
  );
}

export default CreateGoal;

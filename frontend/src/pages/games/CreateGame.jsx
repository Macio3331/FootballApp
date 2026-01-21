import React, { useEffect, useState } from "react";
import CreatePageWrapper from "../CreatePageWrapper";
import CustomButton from "../../components/general/CustomButton";
import { usePopupContext } from "../../context/PopupContext";
import api from "../../api/api";
import { useNavigate, useParams } from "react-router-dom";
import MultiselectContainer from "../../components/containers/MultiselectContainer";
import PlayerCard from "../../components/containers/cards/PlayerCard";
import SelectContainer from "../../components/containers/SelectContainer";
import ClubCard from "../../components/containers/cards/ClubCard";

function CreateGame({ edit }) {
  const { id } = useParams();

  const [location, setLocation] = useState("");
  const [date, setDate] = useState(new Date());
  const [homeClubId, setHomeClubId] = useState();
  const [awayClubId, setAwayClubId] = useState();
  const [homePlayers, setHomePlayers] = useState([]);
  const [awayPlayers, setAwayPlayers] = useState([]);
  const [played, setPlayed] = useState(false);

  const { logError, addMessage } = usePopupContext();
  const navigate = useNavigate();

  useEffect(() => {
    setHomePlayers([]);
  }, [homeClubId]);
  useEffect(() => {
    setAwayPlayers([]);
  }, [awayClubId]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await api.get(`/matches/${id}`);

        const game = res.data;
        setLocation(game.location);
        setDate(game.date);
        const homeClub = await api.get(game.homeClub);
        const awayClub = await api.get(game.awayClub);
        setHomeClubId(homeClub.data.id);
        setAwayClubId(awayClub.data.id);
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
    const body = {
      location: location,
      date: date,
      players:
        homePlayers.length === 0 && awayPlayers.length === 0
          ? undefined
          : [...homePlayers, ...awayPlayers],
      homeClubId: edit ? undefined : homeClubId,
      awayClubId: edit ? undefined : awayClubId,
      played: edit ? played : undefined,
    };
    console.log(body);
    try {
      const res =
        edit && id
          ? await api.put(`/matches/${id}`, body)
          : await api.post("/matches", body);
      if (res) {
        addMessage(`Game ${edit ? "edited" : "created"} successfully`);
        navigate("/games");
      }
    } catch (err) {
      logError(err);
    }
  };
  return (
    <CreatePageWrapper header={edit ? "Edit Game" : "New Game"}>
      <form
        onSubmit={onSubmit}
        className="flex flex-col gap-2 items-center justify-center"
      >
        <label>Location:</label>
        <input
          type="text"
          required
          value={location}
          className="p-2 w-screen max-w-3xl"
          onChange={(e) => {
            setLocation(e.target.value);
          }}
        />
        <label>Date of creation:</label>
        <input
          type="date"
          required
          value={date}
          className="p-2 w-screen max-w-3xl"
          onChange={(e) => {
            setDate(e.target.value);
          }}
        />
        {edit && (
          <>
            <label>Played:</label>
            <input
              type="checkbox"
              value={played}
              className="p-2 w-screen max-w-3xl"
              onChange={(e) => {
                setPlayed(e.target.checked);
              }}
            />
          </>
        )}
        {!edit && (
          <>
            <label className="text-2xl">Home club:</label>
            <div className="h-[30rem] w-screen max-w-4xl overflow-y-scroll">
              <SelectContainer
                apiUrl={`/clubs`}
                setMethod={setHomeClubId}
                CardContent={ClubCard}
                prohibitedId={awayClubId}
              />
            </div>
          </>
        )}
        {!edit && (
          <>
            <label className="text-2xl">Away club:</label>
            <div className="h-[30rem] w-screen max-w-4xl overflow-y-scroll">
              <SelectContainer
                apiUrl={`/clubs`}
                setMethod={setAwayClubId}
                CardContent={ClubCard}
                prohibitedId={homeClubId}
              />
            </div>
          </>
        )}

        {homeClubId && awayClubId && (
          <>
            <label className="text-2xl">Home players:</label>
            <div className="h-[30rem] w-screen max-w-4xl overflow-y-scroll">
              <MultiselectContainer
                selectedEntries={homePlayers}
                apiUrl={`/clubs/${homeClubId}/players`}
                setMethod={setHomePlayers}
                maxEntries={16}
                CardContent={PlayerCard}
              />
            </div>
            <label className="text-2xl">Away players:</label>
            <div className="h-[30rem] w-screen max-w-4xl overflow-y-scroll">
              <MultiselectContainer
                selectedEntries={awayPlayers}
                apiUrl={`/clubs/${awayClubId}/players`}
                setMethod={setAwayPlayers}
                maxEntries={16}
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

export default CreateGame;

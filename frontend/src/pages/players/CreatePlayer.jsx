import React, { useEffect, useState } from "react";
import CreatePageWrapper from "../CreatePageWrapper";
import CustomButton from "../../components/general/CustomButton";
import { usePopupContext } from "../../context/PopupContext";
import api from "../../api/api";
import { useNavigate, useParams } from "react-router-dom";
import SelectContainer from "../../components/containers/SelectContainer";
import ClubCard from "../../components/containers/cards/ClubCard";

function CreatePlayer({ edit }) {
  const { id } = useParams();

  const [name, setName] = useState("");
  const [surname, setSurname] = useState("");
  const [number, setNumber] = useState(1);
  const [position, setPosition] = useState("");
  const [clubId, setClubId] = useState();

  const { logError, addMessage } = usePopupContext();

  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await api.get(`/players/${id}`);
        const player = res.data;
        setName(player.name);
        setSurname(player.surname);
        setNumber(player.number);
        setPosition(player.position);
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
    const body = { name, surname, number, position, clubId };
    try {
      const res =
        edit && id
          ? await api.put(`/players/${id}`, body)
          : await api.post("/players", body);
      if (res) {
        addMessage(`Player ${edit ? "edited" : "created"} successfully`);
        navigate("/players");
      }
    } catch (err) {
      logError(err);
    }
  };
  return (
    <CreatePageWrapper header={edit ? "Edit player" : "New player"}>
      <form
        onSubmit={onSubmit}
        className="flex flex-col gap-2 items-center justify-center"
      >
        <label>Name:</label>
        <input
          type="text"
          required
          value={name}
          className="p-2 w-screen max-w-3xl"
          onChange={(e) => {
            setName(e.target.value);
          }}
        />
        <label>Surname:</label>
        <input
          type="text"
          required
          value={surname}
          className="p-2 w-screen max-w-3xl"
          onChange={(e) => {
            setSurname(e.target.value);
          }}
        />
        <label>Number:</label>
        <input
          type="number"
          min={1}
          max={99}
          required
          value={number}
          className="p-2 w-screen max-w-3xl"
          onChange={(e) => {
            setNumber(e.target.value);
          }}
        />
        <label>Position:</label>
        <input
          type="text"
          required
          value={position}
          className="p-2 w-screen max-w-3xl"
          onChange={(e) => {
            setPosition(e.target.value);
          }}
        />
        <label className="text-2xl">Club:</label>
        <div className=" max-h-[30rem] w-screen max-w-4xl">
          <SelectContainer
            apiUrl={`/clubs`}
            setMethod={setClubId}
            CardContent={ClubCard}
          />
        </div>

        <CustomButton>{edit ? "Edit" : "Create"}</CustomButton>
      </form>
    </CreatePageWrapper>
  );
}

export default CreatePlayer;

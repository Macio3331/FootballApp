import React, { useEffect, useState } from "react";
import CreatePageWrapper from "../CreatePageWrapper";
import CustomButton from "../../components/general/CustomButton";
import { usePopupContext } from "../../context/PopupContext";
import api from "../../api/api";
import { useNavigate, useParams } from "react-router-dom";

function CreateClub({ edit }) {
  const { id } = useParams();

  const [name, setName] = useState("");
  const [location, setLocation] = useState("");
  const [dateOfCreation, setDateOfCreation] = useState(new Date());
  const { logError, addMessage } = usePopupContext();

  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await api.get(`/clubs/${id}`);
        const club = res.data;
        setName(club.name);
        setLocation(club.location);
        setDateOfCreation(club.dateOfCreation);
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
    try {
      const res =
        edit && id
          ? await api.put(`/clubs/${id}`, { name, location, dateOfCreation })
          : await api.post("/clubs", { name, location, dateOfCreation });
      if (res) {
        addMessage(`Club ${edit ? "edited" : "created"} successfully`);
        navigate("/clubs");
      }
    } catch (err) {
      logError(err);
    }
  };
  return (
    <CreatePageWrapper header={edit ? "Edit club" : "New club"}>
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
          value={dateOfCreation}
          className="p-2 w-screen max-w-3xl"
          onChange={(e) => {
            setDateOfCreation(e.target.value);
          }}
        />
        <CustomButton>{edit ? "Edit" : "Create"}</CustomButton>
      </form>
    </CreatePageWrapper>
  );
}

export default CreateClub;

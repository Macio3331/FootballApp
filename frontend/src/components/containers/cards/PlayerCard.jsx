import React from "react";
function PlayerCard({ data }) {
  if (!data) return <></>;
  return (
    <>
      <p>
        <p>ID:</p> {data.id}
      </p>
      <p>
        <p>Name:</p> {data.name}
      </p>
      <p>
        <p>Surname:</p> {data.surname}
      </p>
      <p>
        <p>Number:</p> {data.number}
      </p>
      <p>
        <p>Position:</p> {data.position}
      </p>
    </>
  );
}

export default PlayerCard;

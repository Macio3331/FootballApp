import React from "react";
function GameCard({ data }) {
  if (!data) return <></>;
  return (
    <>
      <p>
        <p>ID:</p> {data.id}
      </p>
      <p>
        <p>Location:</p> {data.location}
      </p>
      <p>
        <p>Date:</p>{" "}
        {typeof data.date === "string" ? data.date : data.date.toDateString()}
      </p>
      <p>
        <p>Home Result:</p> {data.homeResult}
      </p>
      <p>
        <p>Away Result:</p> {data.awayResult}
      </p>
      <p>
        <p>Played:</p> {data.played ? "Yes" : "No"}
      </p>
    </>
  );
}

export default GameCard;

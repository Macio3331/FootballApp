import React from "react";
function ClubCard({ data }) {
  if (!data) return <></>;
  return (
    <>
      <p className="mr-auto">
        <p>Place:</p> {data.place}
      </p>
      <p>
        <p>Club:</p> {data.club.name}
      </p>
      <p>
        <p>Matches:</p> {data.numberOfMatches}
      </p>
      <p>
        <p>Wins:</p> {data.numberOfWins}
      </p>
      <p>
        <p>Ties:</p> {data.numberOfTies}
      </p>
      <p>
        <p>Losses:</p> {data.numberOfLosses}
      </p>
      <div>
        <p>
          <p>Goals Scored:</p> {data.goalsScored}
        </p>
        <p>
          <p>Goals Conceded:</p> {data.goalsConceded}
        </p>
        <p>
          <p>Goal Difference:</p> {data.goalDifference}
        </p>
      </div>
      <p className="ml-auto">Points: {data.points}</p>
    </>
  );
}

export default ClubCard;

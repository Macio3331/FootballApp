import React from "react";
function GoalCard({ data }) {
  if (!data) return <></>;
  return (
    <>
      <p>
        <p>ID:</p> {data.id}
      </p>
      <p>
        <p>Minute:</p> {data.minute}
      </p>
      <p>
        <p>Own goal:</p> {data.ownGoal ? "Yes" : "No"}
      </p>
    </>
  );
}

export default GoalCard;

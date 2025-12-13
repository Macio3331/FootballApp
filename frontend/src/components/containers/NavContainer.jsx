import React from "react";
import LoadingBar from "../general/LoadingBar";
import NavCard from "./NavCard";
function NavContainer({
  data,
  isLoading,
  navigationPrefix,
  CardContent,
  idProperty,
}) {
  return (
    <>
      {isLoading ? (
        <LoadingBar />
      ) : data === undefined || data.length <= 0 ? (
        <p>There is no data yet.</p>
      ) : (
        data.map((entry) => (
          <NavCard
            navigationPrefix={navigationPrefix}
            id={entry?.id}
            CardContent={CardContent}
            data={entry}
            idProperty={idProperty}
          />
        ))
      )}
    </>
  );
}

export default NavContainer;

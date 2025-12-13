import React from "react";
import { NavLink } from "react-router-dom";

function NavCard({ navigationPrefix, data, CardContent, idProperty }) {
  return (
    <NavLink
      to={`/${navigationPrefix}/${idProperty ? data[idProperty] : data?.id}`}
      className={
        "flex flex-row items-center justify-start gap-4 p-3 bg-background-50 transition-colors hover:bg-primary-100 rounded-md shadow-md w-full max-w-4xl flex-wrap"
      }
    >
      <CardContent data={data} />
    </NavLink>
  );
}

export default NavCard;

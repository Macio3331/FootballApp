import React from "react";
import CustomButton from "../components/general/CustomButton";
import { NavLink } from "react-router-dom";

function PageWrapper({ header, creationNavingate, children, disableCreation }) {
  return (
    <div className="mt-24 flex flex-col justify-center items-center gap-4 p-2">
      <h1 className="text-2xl">{header}</h1>
      {!disableCreation && (
        <NavLink to={creationNavingate}>
          <CustomButton>Create new</CustomButton>
        </NavLink>
      )}
      {children}
    </div>
  );
}

export default PageWrapper;

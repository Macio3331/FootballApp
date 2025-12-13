import React from "react";

function CreatePageWrapper({ children, header }) {
  return (
    <div className="mt-24 flex flex-col justify-center items-center gap-4 p-2">
      <h1 className="text-2xl">{header}</h1>
      {children}
    </div>
  );
}

export default CreatePageWrapper;

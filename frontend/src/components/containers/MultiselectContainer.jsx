import React, { useEffect, useState } from "react";
import { useFetchArrayData } from "../../hooks/useFetchArrayData";
import LoadingBar from "../general/LoadingBar";
import CustomButton from "../general/CustomButton";

function MultiselectContainer({
  selectedEntries,
  apiUrl,
  setMethod,
  CardContent,
  maxEntries = Infinity,
  header,
}) {
  const { data, isPending } = useFetchArrayData(apiUrl);

  const onCardClick = (cardData) => {
    setMethod((prev) =>
      prev.find((e) => e === cardData.id)
        ? prev.filter((id) => id !== cardData.id)
        : prev.length < maxEntries
        ? [...prev, cardData.id]
        : prev
    );
  };

  return (
    <div className="w-full h-full flex flex-col items-center justify-start gap-2 p-2 px-4 bg-primary-900/30 rounded-md overflow-y-scroll">
      {isPending ? (
        <LoadingBar />
      ) : data === undefined || data.length <= 0 ? (
        <p>No data yet</p>
      ) : (
        <>
          <h1 className="text-xl">{header}</h1>
          {data.map((entry) => (
            <CustomButton
              type="button"
              onClick={() => onCardClick(entry)}
              variant={"light"}
              className={` w-full gap-6 ${
                selectedEntries?.find((e) => e === entry.id)
                  ? "bg-primary-200 hover:bg-primary-300"
                  : ""
              }`}
            >
              <CardContent data={entry} />
            </CustomButton>
          ))}
        </>
      )}
    </div>
  );
}

export default MultiselectContainer;

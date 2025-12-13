import React, { useState } from "react";
import { useFetchArrayData } from "../../hooks/useFetchArrayData";
import LoadingBar from "../general/LoadingBar";
import CustomButton from "../general/CustomButton";

function SelectContainer({
  apiUrl,
  setMethod,
  CardContent,
  header,
  prohibitedId,
}) {
  const { data, isPending } = useFetchArrayData(apiUrl);
  const [selected, setSelected] = useState();
  const onCardClick = (cardData) => {
    setMethod((prev) => {
      if (prev === cardData.id) {
        setSelected(undefined);
        return undefined;
      } else if (cardData.id === prohibitedId) {
        return prev;
      } else {
        setSelected(cardData.id);
        return cardData.id;
      }
    });
  };
  return (
    <div className="w-full h-full flex flex-col items-center justify-start gap-2 p-2 px-4 bg-primary-900/30 rounded-md overflow-y-scroll">
      {isPending ? (
        <LoadingBar />
      ) : data === undefined ? (
        "No data yet"
      ) : (
        <>
          <h1 className="text-xl">{header}</h1>
          {data.map((entry) => (
            <CustomButton
              type="button"
              variant={"light"}
              onClick={() => onCardClick(entry)}
              className={`w-full gap-6 ${
                selected === entry.id
                  ? " bg-primary-200 hover:bg-primary-300"
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

export default SelectContainer;

import React from "react";
import PageWrapper from "../PageWrapper";
import NavContainer from "../../components/containers/NavContainer";
import ClubCard from "../../components/containers/cards/ClubCard";
import { useFetchArrayData } from "../../hooks/useFetchArrayData";

function Clubs() {
  const { data, isPending } = useFetchArrayData("/clubs");
  return (
    <PageWrapper header={"List of clubs"} creationNavingate={"/club/new"}>
      <NavContainer
        data={data}
        isLoading={isPending}
        navigationPrefix={"club"}
        CardContent={ClubCard}
      />
    </PageWrapper>
  );
}

export default Clubs;

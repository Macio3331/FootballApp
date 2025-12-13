import React from "react";
import PageWrapper from "../PageWrapper";
import NavContainer from "../../components/containers/NavContainer";
import PlayerCard from "../../components/containers/cards/PlayerCard";
import { useFetchArrayData } from "../../hooks/useFetchArrayData";

function Players() {
  const { data, isPending } = useFetchArrayData("/players");
  return (
    <PageWrapper header={"List of players"} creationNavingate={"/player/new"}>
      <NavContainer
        data={data}
        isLoading={isPending}
        navigationPrefix={"player"}
        CardContent={PlayerCard}
      />
    </PageWrapper>
  );
}

export default Players;

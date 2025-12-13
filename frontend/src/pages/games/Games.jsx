import React from "react";
import PageWrapper from "../PageWrapper";
import NavContainer from "../../components/containers/NavContainer";
import GameCard from "../../components/containers/cards/GameCard";
import { useFetchArrayData } from "../../hooks/useFetchArrayData";

function Games() {
  const { data, isPending } = useFetchArrayData("/matches");
  return (
    <PageWrapper header={"List of games"} creationNavingate={"/game/new"}>
      <NavContainer
        data={data}
        isLoading={isPending}
        navigationPrefix={"game"}
        CardContent={GameCard}
      />
    </PageWrapper>
  );
}

export default Games;

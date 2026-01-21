import React from "react";
import PageWrapper from "../PageWrapper";
import NavContainer from "../../components/containers/NavContainer";
import GameCard from "../../components/containers/cards/GameCard";
import { useFetchArrayData } from "../../hooks/useFetchArrayData";

function Schedule() {
  const { data, isPending } = useFetchArrayData("/league/schedule");
  return (
    <PageWrapper header={"League schedule"} disableCreation={true}>
      <NavContainer
        data={data}
        isLoading={isPending}
        navigationPrefix={"game"}
        CardContent={GameCard}
      />
    </PageWrapper>
  );
}

export default Schedule;

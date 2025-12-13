import React from "react";
import PageWrapper from "../PageWrapper";
import NavContainer from "../../components/containers/NavContainer";
import GoalCard from "../../components/containers/cards/GoalCard";
import { useFetchArrayData } from "../../hooks/useFetchArrayData";

function Goals() {
  const { data, isPending } = useFetchArrayData("/goals");
  return (
    <PageWrapper header={"List of goals"} creationNavingate={"/goal/new"}>
      <NavContainer
        data={data}
        isLoading={isPending}
        navigationPrefix={"goal"}
        CardContent={GoalCard}
      />
    </PageWrapper>
  );
}

export default Goals;

import React from "react";
import PageWrapper from "../PageWrapper";
import NavContainer from "../../components/containers/NavContainer";
import TableCard from "../../components/containers/cards/TableCard";
import { useLeagueTable } from "../../hooks/useLeagueTable";

function League() {
  const { data, isPending } = useLeagueTable("/league/table");
  return (
    <PageWrapper header={"League table"} disableCreation={true}>
      <NavContainer
        data={data}
        isLoading={isPending}
        navigationPrefix={"club"}
        idProperty={"clubId"}
        CardContent={TableCard}
      />
    </PageWrapper>
  );
}

export default League;

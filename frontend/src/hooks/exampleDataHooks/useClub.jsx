import { useState, useEffect } from "react";
import { exampleClubs } from "../../exampleData/exampleData";
export const useClub = (apiUrl, id) => {
  const [data, setData] = useState(exampleClubs[0]);
  const [isPending, setIsPending] = useState(false);
  return { data, isPending };
};

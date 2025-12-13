import { useState, useEffect } from "react";
import { exampleClubs } from "../../exampleData/exampleData";
export const useClubs = (apiUrl) => {
  const [data, setData] = useState(exampleClubs);
  const [isPending, setIsPending] = useState(false);
  return { data, isPending };
};

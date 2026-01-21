import { useState, useEffect } from "react";
import { useGlobalReloadContext } from "../context/GlobalReloadContext";
import api from "../api/api";
export const useFetchData = (apiUrl, id, retrigger, setRetrigger) => {
  const [data, setData] = useState();
  const [isPending, setIsPending] = useState(false);
  const { globalReload, setGlobalReload } = useGlobalReloadContext();
  useEffect(() => {
    const loadData = async () => {
      if (id === undefined) return;

      setIsPending(true);
      try {
        const parsedId = parseInt(id);
        const res = await api.get(`${apiUrl}/${parsedId}`);
        if (res) {
          setData(res.data);
        }
      } catch (err) {
        console.log(err);
      }
      setIsPending(false);
      if (setRetrigger) setRetrigger(false);
      if (globalReload) setGlobalReload(false);
    };
    loadData();
  }, [id, retrigger, globalReload, apiUrl, setGlobalReload, setRetrigger]);

  return { data, isPending };
};

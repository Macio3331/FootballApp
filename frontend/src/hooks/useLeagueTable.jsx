import { useState, useEffect } from "react";
import { useGlobalReloadContext } from "../context/GlobalReloadContext";
import api from "../api/api";
import { usePopupContext } from "../context/PopupContext";

export const useLeagueTable = (apiUrl) => {
  const [data, setData] = useState([]);
  const [isPending, setIsPending] = useState(false);
  const { globalReload, setGlobalReload } = useGlobalReloadContext();
  const { logError } = usePopupContext();

  useEffect(() => {
    const loadData = async () => {
      setIsPending(true);
      try {
        const res = await api.get(apiUrl);
        const clubs = await Promise.all(
          res.data.map(async (e) => {
            const clubRes = await api.get(`/clubs/${e.clubId}`);
            return { ...e, club: clubRes.data };
          })
        );
        setData(clubs);
        if (globalReload) setGlobalReload(false);
      } catch (err) {
        logError(err);
        setData([]);
      }
      setIsPending(false);
    };
    loadData();
  }, [globalReload, apiUrl]);

  return { data, isPending };
};

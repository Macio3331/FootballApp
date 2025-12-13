import { useState, useEffect } from "react";
import { useGlobalReloadContext } from "../context/GlobalReloadContext";
import api from "../api/api";
import { usePopupContext } from "../context/PopupContext";
export const useFetchArrayData = (apiUrl) => {
  const [data, setData] = useState();
  const [isPending, setIsPending] = useState(false);
  const { globalReload, setGlobalReload } = useGlobalReloadContext();
  const { logError } = usePopupContext();

  useEffect(() => {
    const loadData = async () => {
      setIsPending(true);
      try {
        const res = await api.get(apiUrl);
        setData(res.data);
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

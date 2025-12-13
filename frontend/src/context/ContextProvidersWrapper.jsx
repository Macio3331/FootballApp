import React from "react";
import { PopupContextProvider } from "./PopupContext";
import { GlobalReloadContextProvider } from "./GlobalReloadContext";
import { SettingsContextProvider } from "./SettingsContext";
function ContextProvidersWrapper({ children }) {
  return (
    <SettingsContextProvider>
      <GlobalReloadContextProvider>
        <PopupContextProvider>{children}</PopupContextProvider>
      </GlobalReloadContextProvider>
    </SettingsContextProvider>
  );
}

export default ContextProvidersWrapper;

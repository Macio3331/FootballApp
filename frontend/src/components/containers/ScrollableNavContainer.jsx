import React from "react";
import NavContainer from "./NavContainer";
import { cn } from "../../helpers/helpers";

function ScrollableNavContainer({
  data,
  isLoading,
  navigationPrefix,
  CardContent,
  className,
}) {
  return (
    <div
      className={cn(
        "h-fit max-h-[30rem] overflow-y-scroll p-2 w-screen max-w-4xl bg-primary-900/20 rounded-md flex flex-col items-center justify-start gap-2",
        className
      )}
    >
      <NavContainer
        data={data}
        isLoading={isLoading}
        navigationPrefix={navigationPrefix}
        CardContent={CardContent}
      />
    </div>
  );
}

export default ScrollableNavContainer;

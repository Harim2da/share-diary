import { atom } from "recoil";

export const selectDateState = atom<string>({
  key: "selectDateState",
  default: "",
});

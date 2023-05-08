import { atom } from "recoil";

export const selectDateState = atom<string>({
  key: "selectDateState",
  default: "",
});

export const isMenuOpenState = atom<boolean>({
  key: "isMenuOpenState",
  default: false,
});

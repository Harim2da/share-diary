import { atom } from "recoil";

export const isMenuOpenState = atom<boolean>({
  key: "isMenuOpenState",
  default: false,
});

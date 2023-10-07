import { atom } from "recoil";

export const selectDateState = atom<string>({
  key: "selectDateState",
  default: "",
});

export const isLoginState = atom<boolean>({
  key: "isLoginState",
  default: false,
});

export const diaryUpdateState = atom<boolean>({
  key: "diaryUpdateState",
  default: false,
});

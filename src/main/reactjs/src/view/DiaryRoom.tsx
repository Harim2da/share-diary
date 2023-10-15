import { useRecoilValue } from "recoil";
import { selectDateState } from "../atom/recoil";
import SelectedDiary from "../component/Calender/SelectedDiary";
import ShareCalender from "../component/Calender/ShareCalender";
import ComponentsWrapper from "../styles/ComponentsWrapper";

export default function DiaryRoom() {
  const selectDate = useRecoilValue(selectDateState);

  return (
    <ComponentsWrapper>
      <ShareCalender />
      {selectDate === "" ? null : <SelectedDiary />}
    </ComponentsWrapper>
  );
}

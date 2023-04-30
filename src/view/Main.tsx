import { useRecoilValue } from "recoil";
import { selectDateState } from "../atom/recoil";
import SelectedDiary from "../component/Calender/SelectedDiary";
import ShareCalender from "../component/Calender/ShareCalender";
import ComponentsWrapper from "../styles/ComponentsWrapper";

function Main() {
  const selectDate = useRecoilValue(selectDateState);

  return (
    <ComponentsWrapper>
      <ShareCalender />
      {selectDate === "" ? null : <SelectedDiary />}
    </ComponentsWrapper>
  );
}

export default Main;

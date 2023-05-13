import { useRecoilValue } from "recoil";
import { selectDateState } from "../atom/recoil";
import SelectedDiary from "../component/Calender/SelectedDiary";
import ShareCalender from "../component/Calender/ShareCalender";
import ComponentsWrapper from "../styles/ComponentsWrapper";
import styled from "styled-components";

function Main() {
  const selectDate = useRecoilValue(selectDateState);

  return (
    <ComponentsWrapper>
      <Div>
        <ShareCalender />
        {selectDate === "" ? null : <SelectedDiary />}
      </Div>
    </ComponentsWrapper>
  );
}

export default Main;

const Div = styled.div`
  height: 100%;
  overflow-y: scroll;
`;

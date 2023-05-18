import styled from "styled-components";
import RankingList from "../component/Ranking/RankingList";
import SelectRanking from "../component/Ranking/SelectRanking";
import ComponentsWrapper from "../styles/ComponentsWrapper";

function Ranking() {
  return (
    <ComponentsWrapper>
      <RankingWrap>
        <SelectRanking />
        <RankingList />
      </RankingWrap>
    </ComponentsWrapper>
  );
}

export default Ranking;

const RankingWrap = styled.div`
  padding: 2rem;
`;

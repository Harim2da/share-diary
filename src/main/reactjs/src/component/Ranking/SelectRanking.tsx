import styled from "styled-components";

function SelectRanking() {
  return (
    <Select>
      <option value="">전체</option>
      <option value="dog">클라이밍 일기</option>
      <option value="cat">농구 일기</option>
    </Select>
  );
}

export default SelectRanking;

const Select = styled.select`
  margin-bottom: 2rem;
  width: 200px;
  height: 35px;
  padding: 0 10px;
  border: 1px solid #d3d3d3;
  border-radius: 8px;
`;

import styled from "styled-components";
import ComponentsWrapper from "../styles/ComponentsWrapper";
import { useState } from "react";

const diary = [
  "클라이밍 일기방",
  "여행 일기방",
  "멍멍이 자랑 일기방",
  "집가고 싶은 사람들의 일기방",
  "클라이밍 일기방",
  "여행 일기방",
  "멍멍이 자랑 일기방",
  "집가고 싶은 사람들의 일기방",
];

function Ranking() {
  const [rankingName, setRankingName] = useState(diary[0]);

  const handleChangeSelect = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setRankingName(e.target.value);
  };

  return (
    <ComponentsWrapper>
      <RankingWrap>
        <select onChange={handleChangeSelect} value={rankingName}>
          {diary.map((i, idx) => (
            <option value={i} key={idx}>
              {i}
            </option>
          ))}
        </select>
        <div>
          <div className="rankingName">{rankingName}</div>
          <table>
            <colgroup>
              <col width="15%" />
              <col width="30%" />
              <col />
              <col width="20%" />
            </colgroup>

            <thead>
              <tr>
                <th>순위</th>
                <th>이미지</th>
                <th>닉네임</th>
                <th>점수</th>
              </tr>
            </thead>

            <tbody>
              <tr>
                <td>1위</td>
                <td>
                  <img
                    src="img/face-happy.png"
                    className="user-icon"
                    alt="user-icon"
                  />
                </td>
                <td>개미핡기</td>
                <td>100</td>
              </tr>

              <tr>
                <td>2위</td>
                <td>
                  <img
                    src="img/face-cool.png"
                    alt="user-icon"
                    className="user-icon"
                  />
                </td>
                <td>주말이 젤루좋아</td>
                <td>100</td>
              </tr>

              <tr>
                <td>3위</td>
                <td>
                  <img
                    src="img/face-muted.png"
                    alt="user-icon"
                    className="user-icon"
                  />
                </td>
                <td>만년 3위</td>
                <td>100</td>
              </tr>
            </tbody>
          </table>
        </div>
      </RankingWrap>
    </ComponentsWrapper>
  );
}

export default Ranking;

const RankingWrap = styled.div`
  padding: 2rem;

  select {
    margin-bottom: 2rem;
    width: 200px;
    height: 35px;
    padding: 0 10px;
    border: 1px solid #d3d3d3;
    border-radius: 8px;
  }

  .rankingName {
    margin-bottom: 1rem;
    font-size: 20px;
    font-weight: bold;
  }

  table {
    width: 100%;
    text-align: center;

    thead {
      th {
        border-bottom: 1px solid #d9d9d9;
        border-top: 1px solid #d9d9d9;
        padding: 1rem 0;
        font-weight: bold;
      }
    }

    tbody {
      td {
        padding: 1rem 0;
        vertical-align: middle;
      }

      .user-icon {
        width: 50px;
      }
    }
  }
`;

import { useRecoilValue } from "recoil";
import { selectDateState } from "../../atom/recoil";
import styled from "styled-components";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faLock, faLockOpen } from "@fortawesome/free-solid-svg-icons";
import { useState } from "react";

function SelectedDiary() {
  const selectDate = useRecoilValue(selectDateState);
  const [isPrivate, setIsPrivate] = useState(false);
  const [emojiCount, setEmojiCount] = useState({
    good: 0,
    sad: 0,
    like: 0,
  });

  return (
    <Wrap>
      <div className="left">
        <div className="top">
          <span className="date">{selectDate} 오늘의 일기</span>
          <span>
            <span className="emoji">
              <span>
                ❤️<span className="num">0</span>
              </span>
              <span>
                👍<span className="num">0</span>
              </span>
              <span>
                🎉<span className="num">0</span>
              </span>
              <span>
                🎂<span className="num">0</span>
              </span>
              <span>
                😈<span className="num">0</span>
              </span>
            </span>
            <span
              className="isPrivate"
              onClick={() => setIsPrivate((prev) => !prev)}
            >
              <FontAwesomeIcon icon={isPrivate ? faLock : faLockOpen} />
            </span>
          </span>
        </div>
        <div className="diary">일기 내용</div>
      </div>
      <div className="right">
        <div>지은영</div>
        <div>지은영</div>
        <div>지은영</div>
        <div>지은영</div>
      </div>
    </Wrap>
  );
}

export default SelectedDiary;

const Wrap = styled.div`
  width: 70%;
  margin: 0 auto 5rem;
  display: flex;
  justify-content: space-evenly;

  .left {
    // margin-left: 4rem;
    margin-top: 2rem;
    width: 70%;
  }

  .top {
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
  }

  .date {
    font-size: 23px;
    font-weight: bold;
  }

  .emoji,
  .isPrivate {
    padding-left: 18px;
  }

  .emoji {
    font-size: 15px;
    span {
      cursor: pointer;
      background: #dfdfdf;
      padding: 0.5rem;
      font-size: 20px;
      margin-left: 1rem;
      border-radius: 5px;

      &:first-child {
        margin-left: 0;
      }

      .num {
        border: none;
        font-size: 12px;
        padding: 0;
        margin-left: 0;
      }
    }
  }

  .isPrivate {
    width: 20px;
    display: inline-block;

    path {
      cursor: pointer;
      color: #8685ef;
    }
  }

  .diary {
    margin-top: 2rem;
    margin-bottom: 2rem;
    border: 2px solid #d6d4e6;
    border-radius: 10px;
    padding: 1rem;
    min-height: 300px;
  }

  .right {
    width: 20%;
    margin-top: 2rem;
    border: 2px solid #d6d4e6;
    border-radius: 10px;
    min-height: 100px;
    padding: 1rem;

    div {
      margin-bottom: 10px;
      padding: 5px;
      cursor: pointer;

      :first-child {
        background: #d6d4e6;
      }
    }
  }

  @media (max-width: 1520px) {
    .emoji {
      span {
        font-size: 16px;
      }
    }

    .num {
      display: none;
    }
  }

  @media (max-width: 1330px) {
    .top {
      flex-direction: column;
      align-items: flex-start;
    }

    .date {
      margin-bottom: 1.5rem;
    }

    .emoji {
      padding-left: 0;
    }
  }
`;

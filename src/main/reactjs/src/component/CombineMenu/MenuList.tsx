import React, { useEffect, useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faMagnifyingGlass,
  faMedal,
  faRightLong,
} from "@fortawesome/free-solid-svg-icons";
import styled from "styled-components";
import { Link, useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import { loginState } from "../../atom/loginState";
import { useRecoilValue } from "recoil";

const dummy = [
  {
    id: 1,
    name: "클라이밍 일기",
    createBy: "만든사람",
    modifyBy: "수정한 사람",
  },
  {
    id: 2,
    name: "농구 일기",
    createBy: "만든사람2",
    modifyBy: "수정한 사람2",
  },
];

interface IDiaryList {
  id: number;
  name: string;
  createBy: string;
  modifyBy: string;
}

function MenuList(props: { isMenuOpen: boolean }) {
  const navigate = useNavigate();
  const isLoggedIn = useRecoilValue(loginState);
  const [diaryList, setDiaryList] = useState<IDiaryList[]>([]);
  const { diaryRoom } = useParams();

  useEffect(() => {
    const data = () => {
      axios
        .get("/api/v0/diary-rooms", {
          headers: { Authorization: localStorage.getItem("login-token") },
        })
        .then((res) => console.log(res));
    };

    if (isLoggedIn) {
      data();
    }
  }, [isLoggedIn]);

  return (
    <ListWrap display={props.isMenuOpen ? "block" : "none"}>
      <div className="search">
        <FontAwesomeIcon icon={faMagnifyingGlass} />
        <input type="text" placeholder="검색" />
      </div>
      <div className="ranking-tab" onClick={() => navigate("/ranking")}>
        일기방 랭킹
        <FontAwesomeIcon icon={faMedal} />
      </div>
      <ul>
        {dummy.map((i) => (
          <li className={String(i.id) === diaryRoom ? "focus" : ""} key={i.id}>
            <Link to={String(i.id)}>
              {/* {String(i.id) === diaryRoom && (
                <FontAwesomeIcon icon={faRightLong} />
              )} */}
              {i.name}
            </Link>
          </li>
        ))}
      </ul>
    </ListWrap>
  );
}

export default MenuList;

const ListWrap = styled.div<{ display: string }>`
  font-size: 1rem;
  padding: 1.5rem 1rem;
  display: ${(props) => props.display};

  .search {
    padding: 0px 12px;
    border-radius: 20px;
    border: 1px solid #d6d6d6;
    display: flex;
    align-items: center;

    svg {
      margin-bottom: -2px;
      padding-right: 6px;
      color: #999;
    }

    input {
      height: 36px;
    }
  }

  .ranking-tab {
    padding: 20px 0;
    color: #8685ef;
    font-weight: bold;

    svg {
      padding-left: 6px;
    }
  }

  li {
    margin-bottom: 14px;
    padding: 9px 0;
    cursor: pointer;

    a {
      color: black;
      text-decoration: none;
    }

    svg {
      padding: 0 6px;
    }

    &.focus {
      font-weight: bold;
      background: #eeeeee;

      a {
        color: #8685ef;
      }
    }
  }
`;

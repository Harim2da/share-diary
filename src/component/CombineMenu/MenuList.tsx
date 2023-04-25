import React, { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faMagnifyingGlass,
  faMedal,
  faRightLong,
} from "@fortawesome/free-solid-svg-icons";
import styled from "styled-components";

function MenuList(props: { isMenuOpen: boolean }) {
  return (
    <ListWrap display={props.isMenuOpen ? "block" : "none"}>
      <div className="search">
        <FontAwesomeIcon icon={faMagnifyingGlass} />
        <input type="text" placeholder="검색" />
      </div>
      <div className="ranking-tab">
        일기방 랭킹
        <FontAwesomeIcon icon={faMedal} />
      </div>
      <ul>
        <li className="focus">
          <FontAwesomeIcon icon={faRightLong} />
          클라이밍 일기
        </li>
        <li>농구 일기</li>
        <li>공부 일기</li>
        <li>반려견 일기</li>
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

    svg {
      padding: 0 6px;
    }

    &.focus {
      font-weight: bold;
      background: #eeeeee;
    }
  }
`;

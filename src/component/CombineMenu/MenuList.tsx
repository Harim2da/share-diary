import React, { useState } from "react";
import { useCycle, motion } from "framer-motion";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faMagnifyingGlass,
  faMedal,
  faPlus,
  faRightLong,
} from "@fortawesome/free-solid-svg-icons";
import styled from "styled-components";

const listVariants = {
  open: {
    opacity: 1,
    display: "block",
    transition: {
      delay: 0.5,
      type: "linear",
    },
  },
  closed: {
    opacity: 0,
    display: "none",
    transition: {
      type: "linear",
    },
  },
};

interface IListProps {
  isOpen: boolean;
}

function MenuList(props: IListProps) {
  const { isOpen } = props;

  return (
    <ListWrap animate={isOpen ? "open" : "closed"} variants={listVariants}>
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
      <FontAwesomeIcon icon={faPlus} className="plusBtn" />
    </ListWrap>
  );
}

export default MenuList;

const ListWrap = styled(motion.div)`
  font-size: 18px;
  padding: 1.5rem 1.75rem;

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
    color: #f4c005;
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

  .plusBtn {
    display: block;
    margin: 60px auto 0;
    padding: 4px;
    width: 35px !important;
    height: 35px;
    background: #e0e0e0;
    color: #7a7979;
    border-radius: 10px;
  }
`;

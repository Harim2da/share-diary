import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBell } from "@fortawesome/free-regular-svg-icons";
import styled from "styled-components";

function Header() {
  return (
    <HeaderWrap>
      <div>
        <h1>클라이밍 일기</h1>
      </div>
      <div>
        <span className="bell">
          <FontAwesomeIcon icon={faBell} />
          <span className="red" />
        </span>
        <span className="login-btn">로그인</span>
      </div>
    </HeaderWrap>
  );
}

export default Header;

const HeaderWrap = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  border-bottom: 1px solid #d9d9d9;
  padding: 0.75rem 1rem;
  position: relative;

  h1 {
    font-weight: bold;
    display: inline-block;
    padding-left: 1.875rem;
  }

  span {
    margin: 0 0.375rem;
    cursor: pointer;

    svg {
      margin-bottom: -3px;
    }

    :last-child {
      margin-right: 0;
      margin-bottom: 0;
    }
  }

  .bell {
    position: relative;
  }

  .red {
    background: red;
    position: absolute;
    margin: 0;
    border-radius: 50%;
    top: 8px;
    right: -1px;
    width: 8px;
    height: 8px;
  }

  .login-btn {
    text-align: center;
    font-size: 12px;
    display: inline-block;
    border: 1px solid #d9d9d9;
    border-radius: 20px;
    padding: 0.5rem 1rem;
  }
`;

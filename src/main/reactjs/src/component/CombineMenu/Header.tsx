import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faBell } from "@fortawesome/free-regular-svg-icons";
import styled from "styled-components";
import { faBars, faUser } from "@fortawesome/free-solid-svg-icons";
import { useSetRecoilState } from "recoil";
import { useNavigate } from "react-router-dom";
import { isMenuOpenState } from "../../atom/uiAtom";
import { useRecoilState } from "recoil";
import { loginState } from "../../atom/loginState";
import axios from "axios";

function Header() {
  const setIsMenuOpen = useSetRecoilState(isMenuOpenState);
  const [isLoggedIn, setIsLoggedIn] = useRecoilState(loginState);
  let navigate = useNavigate();

  //로그아웃
  const handleLogout = () => {
    axios({
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: localStorage.getItem("login-token"),
      },
      url: "/api/auth/logout",
    }).then((response) => {
      setIsLoggedIn(false);
      localStorage.removeItem("login-token");
      console.log(response);
    });
  };

  return (
    <HeaderWrap>
      <div>
        <Icon
          icon={faBars}
          onClick={() => setIsMenuOpen((prev) => !prev)}
          style={{ cursor: "pointer" }}
        />
        <h1 onClick={() => { navigate("/"); }} style={{ cursor: "pointer" }}>잇츠 다이어리</h1>
      </div>
      <div>
        {isLoggedIn ?
          <><span className="bell">
            <Icon icon={faBell} />
            <span className="red" />
          </span>
            <span className="myInfo"
              onClick={() => {
                navigate("/mypage");
              }}>
              <Icon icon={faUser} />
            </span>
            <span
              className="login-btn"
              onClick={handleLogout}>로그아웃</span>
          </> :
          <>
            <span
              className="login-btn"
              onClick={() => {
                navigate("/userLogin");
              }}>로그인</span>
          </>}
      </div>
    </HeaderWrap>
  );
}

export default Header;

const Icon = styled(FontAwesomeIcon)`
  font-size:25px;
`

const HeaderWrap = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  border-bottom: 1px solid #d9d9d9;
  padding: 0.75rem 1rem;
  position: relative;
  height: 60px;

  h1 {
    font-weight: bold;
    display: inline-block;
    padding-left: 1rem;
    margin-top: 1px;
    cursor: pointer;
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

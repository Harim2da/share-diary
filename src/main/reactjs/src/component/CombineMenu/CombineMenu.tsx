import React, { useEffect, useState } from "react";
import styled from "styled-components";
import MenuList from "./MenuList";
import SideMenuBtn from "./SideMenuBtn";
import { useRecoilState } from "recoil";
import { useMediaQuery } from "react-responsive";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faXmark } from "@fortawesome/free-solid-svg-icons";
import { isMenuOpenState } from "../../atom/uiAtom";

function CombineMenu() {
  const [isMenuOpen, setIsMenuOpen] = useRecoilState(isMenuOpenState);
  const [delay, setDelay] = useState(false);

  const width860 = useMediaQuery({
    query: "(max-width:860px)",
  });

  useEffect(() => {
    if (isMenuOpen) {
      setDelay(true);
    } else {
      setDelay(false);
    }
  }, [isMenuOpen]);

  return (
    <CombineNav className="menu" width={isMenuOpen ? "240px" : "0px"}>
      <div className="logo"></div>
      {width860 ? (
        <FontAwesomeIcon
          icon={faXmark}
          className="close"
          onClick={() => setIsMenuOpen(false)}
        />
      ) : null}
      <MenuList isMenuOpen={delay} />
      <SideMenuBtn isMenuOpen={delay} />
    </CombineNav>
  );
}

export default CombineMenu;

const CombineNav = styled.nav<{ width: string }>`
  border-right: 1px solid #d9d9d9;
  box-shadow: 1px 0px 5px #d9d9d9;
  background: #fff;

  .logo {
    width: ${(props) => props.width};
    transition: width 0.3s linear;
    height: 40px;
    background: #d6d4e6;
    margin: 1rem auto 0;
    border-radius: 5px;
    cursor: pointer;
  }

  .close {
    position: absolute;
    right: 2rem;
    top: 1.5rem;
    font-size: 20px;
  }

  @media (max-width: 860px) {
    .logo {
      transition: none;
    }
  }
`;

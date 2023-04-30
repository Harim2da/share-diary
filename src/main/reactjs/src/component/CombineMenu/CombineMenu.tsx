import React, { useEffect, useState } from "react";
import styled from "styled-components";
import MenuList from "./MenuList";
import SideMenuBtn from "./SideMenuBtn";

function CombineMenu() {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [delay, setDelay] = useState(false);

  const onClickLogo = () => {
    setIsMenuOpen((prev) => !prev);
  };

  useEffect(() => {
    if (isMenuOpen) {
      setTimeout(() => {
        setDelay(true);
      }, 300);
    } else {
      setDelay(false);
    }
  }, [isMenuOpen]);

  console.log(isMenuOpen);

  return (
    <CombineNav className="menu" width={isMenuOpen ? "240px" : "40px"}>
      <div className="logo" onClick={onClickLogo}></div>
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
  padding: 0 10px;

  .logo {
    width: ${(props) => props.width};
    transition: width 0.3s linear;
    height: 40px;
    background: #d6d4e6;
    margin: 1rem auto 0;
    border-radius: 5px;
    cursor: pointer;
  }
`;

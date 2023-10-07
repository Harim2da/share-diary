import React, { useState, useEffect } from "react";
import styled from "styled-components";
import CombineMenu from "../component/CombineMenu/CombineMenu";
import Header from "../component/CombineMenu/Header";
import { useRecoilValue } from "recoil";
import { isMenuOpenState } from "../atom/uiAtom";

function ComponentsWrapper(props: { children: React.ReactNode }) {
  const isMenuOpen = useRecoilValue(isMenuOpenState);

  return (
    <Wrap isMenuOpen={isMenuOpen}>
      <CombineMenu />
      <div className="wrap-right">
        <Header />
        <div className="cont">{props.children}</div>
      </div>
    </Wrap>
  );
}
export default ComponentsWrapper;

const Wrap = styled.div<{ isMenuOpen: boolean }>`
  height: 100vh;
  display: flex;

  .wrap-right {
    width: 100%;
    overflow: hidden;
  }

  .menu {
    float: left;
    height: 100vh;
    position: relative;
  }

  .cont {
    height: 100%;
    overflow-y: scroll;
  }

  @media (max-width: 860px) {
    .menu {
      position: absolute;
      z-index: 1;
      width: ${(props) => (props.isMenuOpen ? "100%" : "0")};
      transition: width 0.3s linear;
    }
  }
`;

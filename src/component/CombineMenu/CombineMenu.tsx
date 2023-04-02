import React from "react";
import { motion } from "framer-motion";
import MenuToggle from "./MenuToggle";
import styled from "styled-components";
import MenuList from "./MenuList";

const sidebarVariants = {
  open: {
    width: 300,
    transition: {
      type: "linear",
    },
  },
  closed: {
    width: 0,
    transition: {
      delay: 0.3,
      type: "linear",
    },
  },
};

const titleVariants = {
  open: {
    opacity: 1,
    display: "inline-block",
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

interface ICombineProps {
  isOpen: boolean;
  toggleOpen: () => void;
}

function CombineMenu(props: ICombineProps) {
  const { isOpen, toggleOpen } = props;

  return (
    <CombineNav animate={isOpen ? "open" : "closed"}>
      <motion.div variants={sidebarVariants} />
      <MenuToggle toggle={() => toggleOpen()} />
      <DiaryTitle variants={titleVariants}>클라이밍 일기</DiaryTitle>
      <MenuList isOpen={isOpen} />
    </CombineNav>
  );
}

export default CombineMenu;

const CombineNav = styled(motion.nav)`
  position: absolute;
  top: 0;
  /* background-color: green; */
  height: 100%;
  border-right: 1px solid #d9d9d9;
  box-shadow: 1px 0px 5px #d9d9d9;
  background: #fff;
`;

const DiaryTitle = styled(motion.h1)`
  font-size: 18px;
  font-weight: bold;
  display: inline-block;
  width: 100%;
  text-align: center;
  padding: 20px 0px 16px 0px;
`;

import React, { useState } from 'react';
import { motion } from 'framer-motion';
import styled from 'styled-components';
import Modal from '../Modal/SideMenuModal'
import { useNavigate } from 'react-router-dom';

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

function SideMenuBtn(props: { isOpen: boolean }) {
  let navigate = useNavigate();
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isCreate, setIsCreate] = useState(false);

  const showModal = (create: boolean) => {
    setIsCreate(create);
    setIsModalVisible(true);
  };

  const closeModal = () => {
    setIsModalVisible(false);
  };

  return (
    <>
    <Modal visible={isModalVisible} closeModal={closeModal} isCreate={isCreate} />
    <BtnsWrap
      animate={props.isOpen ? 'open' : 'closed'}
      variants={listVariants}
    >
      <button onClick={() => { navigate('/write') }}>오늘의 일기쓰기</button>
      <button onClick={() => showModal(false)}>일기방 초대하기</button>
      <button onClick={() => showModal(true)}>일기방 만들기</button>
    </BtnsWrap>
  </>
  );
}

export default SideMenuBtn;

const BtnsWrap = styled(motion.div)`
  position: absolute;
  bottom: 0px;
  width: 100%;

  button {
    display: block;
    background: #d2d2d2;
    width: 90%;
    margin: 0 auto 10px;
    padding: 0.625rem 0;
    border-radius: 10px;
    cursor: pointer;

    &:hover {
      background: #d6d4e6;
    }
  }
`;

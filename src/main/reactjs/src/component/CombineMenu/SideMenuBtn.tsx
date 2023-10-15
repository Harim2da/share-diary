import React, { useState } from "react";
import styled from "styled-components";
import Modal from "../Modal/SideMenuModal";
import { useNavigate, useParams } from "react-router-dom";

function SideMenuBtn(props: { isMenuOpen: boolean }) {
  let navigate = useNavigate();
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isCreate, setIsCreate] = useState(false);
  const { diaryRoom } = useParams();

  const showModal = (create: boolean) => {
    setIsCreate(create);
    setIsModalVisible(true);
  };

  const closeModal = () => {
    setIsModalVisible(false);
  };

  return (
    <>
      <Modal
        visible={isModalVisible}
        closeModal={closeModal}
        isCreate={isCreate}
      />
      <BtnsWrap display={props.isMenuOpen ? "block" : "none"}>
        <button
          onClick={() => {
            navigate("/write");
          }}
        >
          오늘의 일기쓰기
        </button>
        {diaryRoom !== undefined && (
          <button onClick={() => showModal(false)}>일기방 초대하기</button>
        )}
        <button onClick={() => showModal(true)}>일기방 만들기</button>
      </BtnsWrap>
    </>
  );
}

export default SideMenuBtn;

const BtnsWrap = styled.div<{ display: string }>`
  width: 100%;
  display: ${(props) => props.display};
  position: absolute;
  right: 0;
  bottom: 0;

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

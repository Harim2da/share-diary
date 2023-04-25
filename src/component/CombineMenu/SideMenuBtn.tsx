import styled from "styled-components";

function SideMenuBtn(props: { isMenuOpen: boolean }) {
  return (
    <BtnsWrap display={props.isMenuOpen ? "block" : "none"}>
      <button>오늘의 일기쓰기</button>
      <button>일기방 초대하기</button>
      <button>일기방 만들기</button>
    </BtnsWrap>
  );
}

export default SideMenuBtn;

const BtnsWrap = styled.div<{ display: string }>`
  width: 100%;
  display: ${(props) => props.display};

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

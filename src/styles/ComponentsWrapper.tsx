import styled from "styled-components";
import CombineMenu from "../component/CombineMenu/CombineMenu";
import Header from "../component/CombineMenu/Header";

function ComponentsWrapper(props: { children: React.ReactNode }) {
  return (
    <Wrap>
      <CombineMenu />
      <Header />
      {props.children}
    </Wrap>
  );
}
export default ComponentsWrapper;

const Wrap = styled.div`
  .menu {
    float: left;
    height: 100vh;
  }
`;

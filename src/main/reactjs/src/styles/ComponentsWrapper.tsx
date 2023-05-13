import styled from "styled-components";
import CombineMenu from "../component/CombineMenu/CombineMenu";
import Header from "../component/CombineMenu/Header";

function ComponentsWrapper(props: { children: React.ReactNode }) {
  return (
    <Wrap>
      <CombineMenu />
      <div className="wrap-right">
        <Header />
        {props.children}
      </div>
    </Wrap>
  );
}
export default ComponentsWrapper;

const Wrap = styled.div`
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

  @media (max-width: 860px) {
    .menu {
    }
  }
`;

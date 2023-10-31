import Carousel from "../component/Info/Carousel";
import InfoText from "../component/Info/InfoText";
import UsageText from "../component/Info/UsageText";
import ComponentsWrapper from "../styles/ComponentsWrapper";


function Main() {
  return <ComponentsWrapper>
    <>
      <Carousel />
      <InfoText />
      <UsageText />
    </>
  </ComponentsWrapper>;
}

export default Main;

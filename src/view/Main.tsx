import { useCycle } from "framer-motion";
import CombineMenu from "../component/CombineMenu/CombineMenu";
import Header from "../component/Header";

function Main() {
  const [isOpen, toggleOpen] = useCycle(false, true);

  return (
    <div>
      <Header />
      <CombineMenu isOpen={isOpen} toggleOpen={toggleOpen} />
    </div>
  );
}

export default Main;

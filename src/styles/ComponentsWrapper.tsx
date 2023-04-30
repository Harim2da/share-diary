import { useCycle } from "framer-motion";
import CombineMenu from "../component/CombineMenu/CombineMenu";
import Header from "../component/CombineMenu/Header";

function ComponentsWrapper(props: { children: React.ReactNode }) {
  const [isOpen, toggleOpen] = useCycle(false, true);

  return (
    <div>
      <Header />
      <CombineMenu isOpen={isOpen} toggleOpen={toggleOpen} />
      {props.children}
    </div>
  );
}
export default ComponentsWrapper;

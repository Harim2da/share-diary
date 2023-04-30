import { motion } from "framer-motion";
import styled from "styled-components";

const Path = (props: any) => (
  <motion.path
    fill="transparent"
    strokeWidth="3"
    stroke="hsl(0, 0%, 18%)"
    strokeLinecap="round"
    {...props}
  />
);

function MenuToggle({ toggle }: any) {
  return (
    <BtnWrap onClick={toggle}>
      <svg width="24" height="24" viewBox="0 0 24 16">
        <Path
          variants={{
            closed: { d: "M 2 2.5 L 20 2.5" },
            open: { d: "M 3 16.5 L 17 2.5" },
          }}
        />
        <Path
          d="M 2 9.423 L 20 9.423"
          variants={{
            closed: { opacity: 1 },
            open: { opacity: 0 },
          }}
          transition={{ duration: 0.1 }}
        />
        <Path
          variants={{
            closed: { d: "M 2 16.346 L 20 16.346" },
            open: { d: "M 3 2.5 L 17 16.346" },
          }}
        />
      </svg>
    </BtnWrap>
  );
}

export default MenuToggle;

const BtnWrap = styled.button`
  /* background: rebeccapurple; */
  padding: 14px 12px;
  position: absolute;
`;

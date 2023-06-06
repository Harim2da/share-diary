import { useEffect } from "react";
import { useLocation } from "react-router-dom";
import { useSetRecoilState } from "recoil";
import { isMenuOpenState } from "../../atom/uiAtom";

function ResetRecoil() {
  const { pathname } = useLocation();
  const setIsMenuOpen = useSetRecoilState(isMenuOpenState);

  useEffect(() => {
    setIsMenuOpen(false);
  }, [pathname]);

  return null;
}

export default ResetRecoil;

import { Route, Routes } from "react-router";
import { BrowserRouter as Router } from "react-router-dom";
import Main from "./view/Main";
import WriteDiary from "./view/WriteDiary";
import Ranking from "./view/Ranking";
import Login from "./view/Login";
import SignUp from "./view/SignUp";
import FindIdPw from "./view/FindIdPw";
import ResetRecoil from "./component/Common/ResetRecoil";

function App() {
  return (
    <Router>
      <ResetRecoil />
      <Routes>
        <Route path="/" element={<Main />} />
        <Route path="/write" element={<WriteDiary />} />
        <Route path="/ranking" element={<Ranking />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/find" element={<FindIdPw />} />
      </Routes>
    </Router>
  );
}

export default App;

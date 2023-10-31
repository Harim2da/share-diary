import { Route, Routes } from "react-router";
import { BrowserRouter } from "react-router-dom";
import Main from "./view/Main";
import WriteDiary from "./view/WriteDiary";
import Ranking from "./view/Ranking";
import Login from "./view/Login";
import SignUp from "./view/SignUp";
import FindIdPw from "./view/FindIdPw";
import ResetRecoil from "./component/Common/ResetRecoil";
import MyPage from "./view/MyPage";
import DiaryRoom from "./view/DiaryRoom";
import Callback from "./view/Callback";

function App() {
  return (
    <BrowserRouter basename={process.env.PUBLIC_URL}>
      <ResetRecoil />
      <Routes>
        <Route path="/" element={<Main />} />
        <Route path="/room/:diaryRoom" element={<DiaryRoom />} />
        <Route path="/write" element={<WriteDiary />} />
        <Route path="/ranking" element={<Ranking />} />
        <Route path="/userLogin" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/find" element={<FindIdPw />} />
        <Route path="/mypage" element={<MyPage />} />
        <Route path="/login/oauth2/callback" element={<Callback />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

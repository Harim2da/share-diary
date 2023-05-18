import { Route, Routes } from "react-router";
import { BrowserRouter as Router } from "react-router-dom";
import Main from "./view/Main";
import WriteDiary from "./view/WriteDiary";
import ResetRecoil from "./component/common/ResetRecoil";
import Ranking from "./view/Ranking";

function App() {
  return (
    <Router>
      <ResetRecoil />
      <Routes>
        <Route path="/" element={<Main />} />
        <Route path="/write" element={<WriteDiary />} />
        <Route path="/ranking" element={<Ranking />} />
      </Routes>
    </Router>
  );
}

export default App;

import React from "react";
import { Route, Routes } from "react-router";
import { BrowserRouter as Router } from "react-router-dom";
import Main from "./view/Main";
import WriteDiary from "./view/WriteDiary";
import Login from "./view/Login";
import SignUp from "./view/SignUp";
import FindIdPw from "./view/FindIdPw";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Main />} />
        <Route path="/write" element={<WriteDiary />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/find" element={<FindIdPw />} />
      </Routes>
    </Router>
  );
}

export default App;

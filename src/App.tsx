import React from "react";
import { Route, Routes } from "react-router";
import { BrowserRouter as Router } from "react-router-dom";
import Main from "./view/Main";
import WriteDiary from "./view/WriteDiary";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Main />} />
        <Route path="/write" element={<WriteDiary />} />
      </Routes>
    </Router>
  );
}

export default App;

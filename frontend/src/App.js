import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './HomePage';
import AboutPage from './AboutPage';
import ResultPage from "./ResultPage";
import AdminPage from "./AdminPage";


const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/about" element={<AboutPage />} />
                <Route path="/search/:keyword" element={<ResultPage />} />
                <Route path="/secret" element={<AdminPage />} />
            </Routes>
        </Router>
    );
};

export default App;

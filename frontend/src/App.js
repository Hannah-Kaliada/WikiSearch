import React from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import HomePage from './HomePage';
import AboutPage from './AboutPage';
import ResultPage from "./ResultPage";
import AdminPage from "./AdminPage";
import LoginPage from "./LoginPage";
import TermsOfUsePage from "./TermsOfUsePage";
import SignupPage from "./SignupPage";


const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage/>}/>
                <Route path="/about" element={<AboutPage/>}/>
                <Route path="/search/:keyword/:userId" element={<ResultPage/>}/>
                <Route path="/secret" element={<AdminPage/>}/>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/terms-of-use" element={<TermsOfUsePage/>}/>
                <Route path="/sign-up" element={<SignupPage/>}/>
            </Routes>
        </Router>
    );
};

export default App;

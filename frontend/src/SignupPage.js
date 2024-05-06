import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './SignupPage.css';

const SignupPage = () => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleSignup = async () => {
        try {
            const response = await axios.post('http://localhost:8080/api/v1/users/addUser', { username, email, password });
            setMessage('User signed up successfully. UserID: ' + response.data.id);
            navigate('/');
        } catch (error) {
            setMessage('Failed to sign up. Please try again.');
        }
    };

    return (
        <div className="page-container">
            <div className="signup-container">
                <h2>Sign Up</h2>
                <div>
                    <label>Username:</label>
                    <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} />
                </div>
                <div>
                    <label>Email:</label>
                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} />
                </div>
                <div>
                    <label>Password:</label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
                </div>
                <button onClick={handleSignup}>Sign Up</button>
                {message && <p className="message">{message}</p>}
            </div>
        </div>
    );
};

export default SignupPage;

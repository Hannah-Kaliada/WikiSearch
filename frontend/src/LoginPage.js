import React, {useState} from 'react';
import axios from 'axios';
import {useNavigate} from 'react-router-dom';
import './LoginPage.css'; // Импорт стилей CSS

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleLogin = async () => {
        try {
            const response = await axios.post('http://localhost:8080/api/v1/users/login', {email, password});
            if (response.data !== 0) {
                setMessage('User authenticated successfully. UserID: ' + response.data);

                navigate('/', {state: {userId: response.data}});
            } else {
                setMessage('Invalid email or password');
            }
        } catch (error) {
            setMessage('Something went wrong. Please try again later.');
        }
    };

    return (
        <div className="page-container">
            <div className="login-container">
                <h2>Login</h2>
                <div>
                    <label>Email:</label>
                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)}/>
                </div>
                <div>
                    <label>Password:</label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)}/>
                </div>
                <button onClick={handleLogin}>Login</button>
                {message && <p className="message">{message}</p>}
            </div>
        </div>
    );
};

export default LoginPage;

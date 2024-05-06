import React, {useEffect, useState} from 'react';
import {useNavigate, Link} from 'react-router-dom';
import {useLocation} from 'react-router-dom';
import './HomePage.css';
import ProfileModal from "./ProfileModal";

const HomePage = () => {
    const {state} = useLocation();
    const userId = state ? state.userId : 0;
    const [articles, setArticles] = useState([]);
    const [menuOpen, setMenuOpen] = useState(false);
    const [user, setUser] = useState(null);
    const navigate = useNavigate();
    const [isProfileModalOpen, setIsProfileModalOpen] = useState(false);


    useEffect(() => {
        fetch('http://localhost:8080/api/v1/articles/top5ByUserCount')
            .then(response => response.json())
            .then(articles => {
                setArticles(articles.slice(0, 5));
            })
            .catch(error => console.error('Ошибка получения данных: ', error));
        loadUserInfo(userId);
    }, []);

    const loadUserInfo = async (userId) => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/users/${userId}`);
            if (response.ok) {
                const userData = await response.json();
                setUser(userData);
            }
        } catch (error) {
            console.error('Ошибка загрузки информации о пользователе:', error);
        }
    };

    const searchApi = () => {
        let searchTerm = encodeURIComponent(document.getElementById("word").value);
        navigate(`/search/${searchTerm}/${userId}`);
    };

    const toggleMenu = () => {
        setMenuOpen(!menuOpen);
    };

    return (
        <>
            <div id="header">
                <div id="logo-and-menu">
                    <button onClick={toggleMenu} id="menu-button"></button>
                </div>
                <h1>WikiSearch</h1>
                <div id="login-signup">
                    {user ? (
                        <div>
                            <span>Hello, {user.username}!</span>
                            <button className="profile-button" onClick={() => setIsProfileModalOpen(true)}>
                                {user && user.username && user.username.charAt(0).toUpperCase()}
                            </button>

                        </div>
                    ) : (
                        <div>
                            <a href="/sign-up">Sign up</a>
                            <a href="/login">Log in</a>
                        </div>
                    )}
                </div>
            </div>

            {menuOpen && (
                <div id="menu">
                    <ul>
                        <a href="http://localhost:3000/secret">&#129323;</a>
                    </ul>
                </div>

            )}
            <hr/>
            <div id="searchBox">
                <form id="searchForm" onSubmit={(e) => {
                    e.preventDefault();
                    searchApi();
                }}>
                    <label htmlFor="word"></label><input type="text" id="word" name="word" required/>
                    <input type="submit" value="Search"/>
                </form>
            </div>
            <hr/>
            <div className="articles">
                <h1>Top 5 Articles</h1>
                {articles.map((article, index) => (
                    <div key={index} className="article">
                        {article.imagePath ? (
                            <img className="article-image" src={article.imagePath} alt="Article"/>
                        ) : (
                            <div className="article-placeholder"></div>
                        )}
                        <div className="article-content">
                            <div className="article-title">
                                <a href={article.url} target="_blank" rel="noopener noreferrer">{article.title}</a>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
            <div id="footer">
                <a href="/terms-of-use">Terms of Use</a>
            </div>
            <ProfileModal
                isOpen={isProfileModalOpen}
                onRequestClose={() => setIsProfileModalOpen(false)}
                user={user}
            />
        </>
    );
}
export default HomePage;

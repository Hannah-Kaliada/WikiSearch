import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './HomePage.css';

const HomePage = () => {
    const [articles, setArticles] = useState([]);
    const [menuOpen, setMenuOpen] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        fetch('/api/v1/articles/top5ByUserCount')
            .then(response => response.json())
            .then(articles => {
                setArticles(articles.slice(0, 5));
            })
            .catch(error => console.error('Ошибка получения данных: ', error));
    }, []);

    const searchApi = () => {
        let searchTerm = encodeURIComponent(document.getElementById("word").value);
        navigate(`/search/${searchTerm}`); // Используем navigate для навигации
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
        </>
    );
};

export default HomePage;

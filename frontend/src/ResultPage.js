import React, {useEffect, useState} from 'react';
import {useParams} from 'react-router-dom';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faStar} from '@fortawesome/free-solid-svg-icons';
import './ResultPage.css';

const ResultPage = () => {
    const [articles, setArticles] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 5;
    const {keyword, userId} = useParams();
    const [user, setUser] = useState(null);

    useEffect(() => {
        fetchUserById(userId);
        loadArticles(currentPage, keyword);
    }, [currentPage, keyword, userId]);
    const fetchUserById = (userId) => {
        fetch(`http://localhost:8080/api/v1/users/${userId}`)
            .then(response => response.json())
            .then(user => setUser(user))
            .catch(error => console.error('Error fetching user:', error));
    };
    const [starredArticles, setStarredArticles] = useState([]);

    const handleStarClick = async (articleId) => {
        if (starredArticles.includes(articleId)) {

            try {
                await fetch(`http://localhost:8080/api/v1/favorite-articles/${userId}/remove/${articleId}`, {
                    method: 'DELETE'
                });
                setStarredArticles(starredArticles.filter(id => id !== articleId));
            } catch (error) {
                console.error('Error removing article from favorites:', error);
            }
        } else {

            try {
                await fetch(`http://localhost:8080/api/v1/favorite-articles/${userId}/add/${articleId}`, {
                    method: 'POST'
                });
                setStarredArticles([...starredArticles, articleId]);
            } catch (error) {
                console.error('Error adding article to favorites:', error);
            }
        }
    };

    const loadArticles = (page, keyword) => {
        const startIndex = (page - 1) * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;

        fetch(`http://localhost:8080/api/v1/search?searchTerm=${keyword}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(() => {

                return fetch(`http://localhost:8080/api/v1/articles/search?keyword=${keyword}`);
            })
            .then(response => response.json())
            .then(data => {
                const slicedData = data.slice(startIndex, endIndex);
                setArticles(slicedData);
            })
            .catch(error => console.error('Ошибка при получении данных:', error));
    };


    const goToPrevPage = () => {
        setCurrentPage(prevPage => Math.max(prevPage - 1, 1));
    };

    const goToNextPage = () => {
        setCurrentPage(prevPage => prevPage + 1);
    };

    return (
        <>
            <h1>Search Results for "{keyword}"</h1>
            <div className="articles">
                {articles.map(article => (
                    <div key={article.id} className="article">
                        <img className="article-image" src={article.imagePath} alt={article.title}/>
                        <div className="article-content">
                            <div className="article-title">
                                <a href={article.url} target="_blank" rel="noopener noreferrer">{article.title}</a>
                            </div>
                            {user && (
                                <button
                                    className="star-button"
                                    style={{color: starredArticles.includes(article.id) ? 'yellow' : 'darkgrey'}}
                                    onClick={() => handleStarClick(article.id)}
                                >
                                    <FontAwesomeIcon icon={faStar}/>
                                </button>
                            )}
                        </div>
                    </div>
                ))}
            </div>
            <div className="pagination">
                <button disabled={currentPage === 1} onClick={goToPrevPage}>&lt; Previous</button>
                <button onClick={goToNextPage}>Next &gt;</button>
            </div>
        </>
    );
};

export default ResultPage;

import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const ResultPage = () => {
    const [articles, setArticles] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 5;
    const { keyword } = useParams();

    useEffect(() => {
        loadArticles(currentPage, keyword);
    }, [currentPage, keyword]);

    const loadArticles = (page, keyword) => {
        const startIndex = (page - 1) * itemsPerPage;
        const endIndex = startIndex + itemsPerPage;

        // First, make a POST request to add a new entry to the database
        fetch(`http://localhost:8080/api/v1/search?searchTerm=${keyword}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(() => {
                // Now, fetch the articles based on the provided keyword
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
                        <img className="article-image" src={article.imagePath} alt={article.title} />
                        <div className="article-content">
                            <div className="article-title">
                                <a href={article.url} target="_blank" rel="noopener noreferrer">{article.title}</a>
                            </div>
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

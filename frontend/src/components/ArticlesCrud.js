import React, {useState, useEffect} from 'react';
import axios from 'axios';

const ArticleCrud = () => {
    const [articles, setArticles] = useState([]);
    const [showAllArticles, setShowAllArticles] = useState(false);
    const [searchKeyword, setSearchKeyword] = useState('');
    const [sortBy, setSortBy] = useState(null);

    const sortArticles = () => {
        if (sortBy === 'title') {
            setArticles(articles.slice().sort((a, b) => a.title.localeCompare(b.title)));
        } else if (sortBy === 'id') {
            setArticles(articles.slice().sort((a, b) => a.id - b.id));
        }
    };

    const fetchArticles = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/v1/articles');
            setArticles(response.data);
        } catch (error) {
            console.error('Error fetching articles:', error);
        }
    };

    useEffect(() => {
        if (showAllArticles) {
            fetchArticles();
        }
    }, [showAllArticles]);

    useEffect(() => {
        sortArticles();
    }, [sortBy, articles]);

    const handleDelete = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/api/v1/articles/deleteArticle/${id}`);
            setArticles(articles.filter(article => article.id !== id));
        } catch (error) {
            console.error('Error deleting article:', error);
        }
    };

    const filteredArticles = articles.filter(article =>
        article.title.toLowerCase().includes(searchKeyword.toLowerCase())
    );

    return (
        <div className="crud-form">
            <hr/>
            <div style={{display: 'flex', justifyContent: 'center', marginBottom: '10px'}}>
                <button onClick={() => setShowAllArticles(!showAllArticles)}>
                    {showAllArticles ? 'Hide Articles' : 'Show Articles'}
                </button>
            </div>
            {showAllArticles && (
                <div>
                    <h3>All Articles</h3>
                    <div>
                        <input
                            type="text"
                            value={searchKeyword}
                            onChange={(e) => setSearchKeyword(e.target.value)}
                            placeholder="Search by keyword"
                        />
                        <button onClick={() => setSortBy('title')}>Sort by Title</button>
                        <button onClick={() => setSortBy('id')}>Sort by ID</button>
                    </div>
                    <table style={{width: '100%', textAlign: 'center'}}>
                        <tbody>
                        {filteredArticles.map((article) => (
                            <tr key={article.id}>
                                <td>{article.id}</td>
                                <td>{article.title}</td>
                                <td>
                                    <button style={{ backgroundColor: 'black' }} onClick={() => handleDelete(article.id)}>Delete</button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
            <hr/>
        </div>
    );
};

export default ArticleCrud;

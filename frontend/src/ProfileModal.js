import React, {useState, useEffect} from 'react';
import Modal from 'react-modal';
import axios from 'axios';
import './ProfileModule.css';

const ProfileModal = ({isOpen, onRequestClose, user}) => {
    const [showPassword, setShowPassword] = useState(false);
    const [updatedUser, setUpdatedUser] = useState(user);

    useEffect(() => {
        setUpdatedUser(user);
    }, [user]);

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    };

    const removeArticle = async (articleId) => {
        try {
            const response = await axios.delete(`http://localhost:8080/api/v1/favorite-articles/${user.id}/remove/${articleId}`);
            if (response.status === 200) {
                const updatedUserCopy = {...updatedUser};
                updatedUserCopy.favoriteArticles = updatedUserCopy.favoriteArticles.filter(article => article.id !== articleId);
                setUpdatedUser(updatedUserCopy);
            }
        } catch (error) {
            console.error('Ошибка при удалении статьи:', error);
        }
    };

    if (!updatedUser) {
        return null;
    }

    return (
        <Modal
            isOpen={isOpen}
            onRequestClose={onRequestClose}
            contentLabel="Profile Modal"
            ariaHideApp={false}
        >
            <button onClick={onRequestClose}
                    style={{position: 'absolute', top: '10px', right: '10px', cursor: 'pointer'}}>X
            </button>
            <h2>{updatedUser.username}'s profile</h2>
            <div>
                <p>Username: {updatedUser.username}</p>
                <p>Email: {updatedUser.email}</p>
                <p>Password: {showPassword ? updatedUser.password : '**********'}</p>
                <button onClick={togglePasswordVisibility}>
                    {showPassword ? 'Hide Password' : 'Show Password'}
                </button>
                <h3>Favorite Articles:</h3>
                <table>
                    <tbody>
                    {updatedUser.favoriteArticles.map((article, index) => (
                        <tr key={index}>
                            <td><img src={article.imagePath} alt="Thumbnail" width="50" height="50"/></td>
                            <td>
                                <div>
                                    <a href={article.url} target="_blank" rel="noopener noreferrer">{article.title}</a>
                                </div>
                            </td>
                            <td>
                                <button onClick={() => removeArticle(article.id)}>Remove</button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </Modal>
    );
};

export default ProfileModal;

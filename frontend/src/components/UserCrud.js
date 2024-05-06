import React, {useEffect, useState} from 'react';
import axios from 'axios';
import Modal from 'react-modal';
import './UserCrud.css';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faTrash} from '@fortawesome/free-solid-svg-icons';


const UserCard = ({user, onDelete, fetchUsers}) => {
    const [country, setCountry] = useState('Loading...');
    const [isEditing, setIsEditing] = useState(false);
    const [editedUsername, setEditedUsername] = useState(user.username);
    const [editedEmail, setEditedEmail] = useState(user.email);
    const [countryId, setCountryId] = useState(user.countryId);
    const [countryOptions, setCountryOptions] = useState([]);
    const [selectedCountryName, setSelectedCountryName] = useState('');

    useEffect(() => {
        const fetchCountry = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/v1/users/${user.id}/country`);
                setCountry(response.data.name);
            } catch (error) {
                console.error('Error fetching country:', error);
                setCountry('N/A');
            }
        };
        fetchCountry();
    }, [user.id]);

    useEffect(() => {
        const fetchCountries = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/v1/countries');
                setCountryOptions(response.data);
            } catch (error) {
                console.error('Error fetching countries:', error);
            }
        };
        fetchCountries();
    }, []);

    const handleEdit = () => {
        setIsEditing(true);
        setSelectedCountryName(country);
    };

    const handleCancelEdit = () => {
        setIsEditing(false);
        setEditedUsername(user.username);
        setEditedEmail(user.email);
        setCountry(selectedCountryName);
    };

    const handleSaveEdit = async () => {
        try {
            const countryResponse = await axios.get(`http://localhost:8080/api/v1/countries/name/${country}`);
            const countryId = countryResponse.data.id;
            const updatedUserData = {
                id: user.id,
                username: editedUsername,
                email: editedEmail,
                password: user.password
            };
            await axios.put(`http://localhost:8080/api/v1/users/updateUser/${user.id}`, updatedUserData);

            await axios.put(`http://localhost:8080/api/v1/users/updateUserCountry/${user.id}/${countryId}`);
            fetchUsers();
            setIsEditing(false);
        } catch (error) {
            console.error('Error updating user:', error);
        }
    };


    const handleInputChange = (e) => {
        const {name, value} = e.target;
        if (name === 'username') {
            setEditedUsername(value);
        } else if (name === 'email') {
            setEditedEmail(value);
        }
    };

    const handleCountryChange = (e) => {

        setCountry(e.target.value);
    };
    const handleRemoveArticle = async (userId, articleId) => {
        try {
            await axios.delete(`http://localhost:8080/api/v1/favorite-articles/${userId}/remove/${articleId}`);
            fetchUsers(); // Optionally, fetch updated user data after removal
        } catch (error) {
            console.error('Error removing article:', error);
        }
    };


    return (
        <>
            <tr className="user-card">
                <td>{user.id}</td>
                <td>{isEditing ? <input type="text" name="username" value={editedUsername}
                                        onChange={handleInputChange}/> : user.username}</td>
                <td>{isEditing ?
                    <input type="email" name="email" value={editedEmail}
                           onChange={handleInputChange}/> : user.email}</td>
                <td>
                    {isEditing ? (
                        <select value={country} onChange={handleCountryChange}>
                            <option value="">Select country</option>
                            {countryOptions.map(country => (
                                <option key={country.id} value={country.name}>{country.name}</option>
                            ))}
                        </select>
                    ) : (
                        country
                    )}
                </td>
                <td>
                    {isEditing ? (
                        <>
                            <button onClick={handleSaveEdit}>Save</button>
                            <button onClick={handleCancelEdit}>Cancel</button>
                        </>
                    ) : (
                        <button onClick={handleEdit}>Edit</button>
                    )}
                    <button onClick={() => onDelete(user.id)}>Delete</button>
                </td>
            </tr>
            <tr>
                <td colSpan="6">
                    <strong>Favorite Articles:</strong>
                    <ul className="favorite-articles-list favorite-articles-grid">
                        {user.favoriteArticles && user.favoriteArticles.map(article => (
                            <li key={article.id} className="favorite-article-item">
                                <a href={article.url} target="_blank" rel="noopener noreferrer">{article.title}</a>
                                <button onClick={() => handleRemoveArticle(user.id, article.id)}>
                                    <FontAwesomeIcon icon={faTrash} style={{color: 'grey'}}/>
                                </button>
                            </li>
                        ))}
                    </ul>
                </td>
            </tr>


        </>
    );
};
const UserCrud = () => {
    const [users, setUsers] = useState([]);
    const [name, setName] = useState('');
    const [searchTerm, setSearchTerm] = useState('');
    const [isLoaded, setIsLoaded] = useState(false);
    const [isOpen, setIsOpen] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [modalUsername, setModalUsername] = useState('');
    const [modalEmail, setModalEmail] = useState('');
    const [modalPassword, setModalPassword] = useState('');
    const [modalCountry, setModalCountry] = useState('');
    const [sortType, setSortType] = useState('');

    const [error, setError] = useState('');
    const fetchUsers = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/v1/users');
            console.log("Fetched users:", response.data);
            setUsers(response.data);
            setIsLoaded(true);
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };

    const handleSortChange = (e) => {
        setSortType(e.target.value);
    };


    const handleCreateUser = async () => {
        try {
            const response = await axios.post(`http://localhost:8080/api/v1/users/addUserAndCountry/${modalCountry}`, {
                username: modalUsername,
                email: modalEmail,
                password: modalPassword,
                favoriteArticles: []
            });
            console.log("Added user:", response.data);
            setUsers([...users, response.data]);
            setModalUsername('');
            setModalEmail('');
            setModalPassword('');
            setModalCountry('');
            setIsModalOpen(false);
        } catch (error) {
            console.error('Error creating user:', error);
            setError('Failed to create user. Please try again.');
        }
    };

    const handleSearchUser = (searchTerm) => {
        const foundUsers = document.querySelectorAll('.user-card');
        let userFound = false;

        foundUsers.forEach(userRow => {
            const userEmail = userRow.querySelector('td:nth-child(3)').textContent;
            if (userEmail === searchTerm) {
                userRow.scrollIntoView({behavior: 'smooth', block: 'center'});
                userRow.style.border = '4px solid #0044ff';
                userFound = true;
            } else {
                userRow.style.border = 'none';
            }
        });

        if (!userFound) {
            setError('User not found.');
        } else {
            setError('');
        }
    };

    const resetSearch = () => {
        setSearchTerm('');
        setError('');
        users.forEach(user => {
            const userRow = document.getElementById(`user-${user.id}`);
            if (userRow) {
                userRow.style.border = 'none';
            }
        });
    };

    const handleDeleteUser = async (id) => {
        try {
            console.log("Deleting user with ID:", id);
            await axios.delete(`http://localhost:8080/api/v1/users/deleteUser/${id}`);
            setUsers(users.filter(user => user.id !== id));
        } catch (error) {
            console.error('Error deleting user:', error);
        }
    };

    const handleInputChange = (e) => {
        setSearchTerm(e.target.value);
    };

    const handleModalInputChange = (e) => {
        const {name, value} = e.target;
        if (name === 'username') {
            setModalUsername(value);
        } else if (name === 'email') {
            setModalEmail(value);
        } else if (name === 'password') {
            setModalPassword(value);
        } else if (name === 'country') {
            setModalCountry(value);
        }
    };

    const sortUsers = (users) => {
        if (sortType === 'id') {
            return users.slice().sort((a, b) => a.id - b.id);
        } else if (sortType === 'username') {
            return users.slice().sort((a, b) => a.username.localeCompare(b.username));
        } else if (sortType === 'email') {
            return users.slice().sort((a, b) => a.email.localeCompare(b.email));
        } else {
            return users;
        }
    };

    const handleToggleUsers = () => {
        setIsOpen(!isOpen);
        fetchUsers();
    };


    const openModal = () => {
        setIsModalOpen(true);
        setError('');
    };

    const closeModal = () => {
        setIsModalOpen(false);
    };

    return (
        <div>
            <hr></hr>
            <div style={{display: 'flex', justifyContent: 'center', marginBottom: '10px'}}>
                <button onClick={handleToggleUsers}>{isOpen ? 'Hide Users' : 'Show Users'}</button>
            </div>
            <hr/>
            <div style={{display: 'flex', justifyContent: 'center', marginBottom: '10px'}}>
                <input type="text" value={searchTerm} onChange={handleInputChange} placeholder="Search by email"/>
                <button onClick={() => handleSearchUser(searchTerm)} disabled={!searchTerm}>Search</button>
                <button onClick={resetSearch}>Reset</button>
            </div>
            <div style={{display: 'flex', justifyContent: 'center', marginBottom: '10px'}}>
                <button onClick={openModal}>Add User</button>
            </div>
            <Modal
                isOpen={isModalOpen}
                onRequestClose={closeModal}
                style={{
                    content: {
                        width: 'fit-content',
                        height: 'fit-content',
                        margin: 'auto',
                        border: '1px solid #ccc',
                        borderRadius: '5px',
                        padding: '20px',
                        boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
                    },
                    overlay: {
                        backgroundColor: 'rgba(0, 0, 0, 0.5)',
                    },
                }}
            >
                <h2>Create User</h2>
                {error && <div style={{color: 'red', marginBottom: '10px'}}>{error}</div>}
                <div style={{marginBottom: '10px'}}>
                    <label>Username:</label>
                    <br/>
                    <input type="text" name="username" value={modalUsername} onChange={handleModalInputChange}
                           style={{width: '100%', boxSizing: 'border-box', marginBottom: '10px'}}/>
                </div>
                <div style={{marginBottom: '10px'}}>
                    <label>Email:</label>
                    <br/>
                    <input type="email" name="email" value={modalEmail} onChange={handleModalInputChange}
                           style={{width: '100%', boxSizing: 'border-box', marginBottom: '10px'}}/>
                </div>
                <div style={{marginBottom: '10px'}}>
                    <label>Password:</label>
                    <br/>
                    <input type="password" name="password" value={modalPassword} onChange={handleModalInputChange}
                           style={{width: '100%', boxSizing: 'border-box', marginBottom: '10px'}}/>
                </div>
                <div style={{marginBottom: '10px'}}>
                    <label>Country:</label>
                    <br/>
                    <input type="text" name="country" value={modalCountry} onChange={handleModalInputChange}
                           style={{width: '100%', boxSizing: 'border-box', marginBottom: '10px'}}/>
                </div>
                <button onClick={handleCreateUser} style={{marginRight: '10px'}}>Add User</button>
                <button onClick={closeModal}>Cancel</button>
            </Modal>


            {isOpen && (
                <>
                    <h3>All Users</h3>
                    <div>
                        <label htmlFor="sort">Sort by:&ensp;</label>
                        <select id="sort" value={sortType} onChange={handleSortChange}>
                            <option value="">None</option>
                            <option value="id">ID</option>
                            <option value="username">Username</option>
                            <option value="email">Email</option>
                        </select>
                    </div>
                    <table className="user-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Country</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {sortUsers(users).map(user => (
                            <UserCard key={user.id} user={user} onDelete={handleDeleteUser} fetchUsers={fetchUsers}/>
                        ))}
                        </tbody>
                    </table>
                </>
            )}

        </div>
    );
};

export default UserCrud;
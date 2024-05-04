import React, { useState } from 'react';
import axios from 'axios';
import Modal from 'react-modal';
import './UserCrud.css';

const UserCard = ({ user, onDelete }) => {
    return (
        <>
            <tr className="user-card">
                <td>{user.id}</td>
                <td>{user.username}</td>
                <td>{user.email}</td>
                <td>{user.country}</td>
                <td>
                    <button onClick={() => onDelete(user.id)}>Delete</button>
                </td>
            </tr>
            <tr>
                <td colSpan="6">
                    <strong>Favorite Articles:</strong>
                    <ul>
                        {user.favoriteArticles.map(article => (
                            <li key={article.id}>
                                <a href={article.url} target="_blank" rel="noopener noreferrer">{article.title}</a>
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
    const [isLoaded, setIsLoaded] = useState(false); // New state to track if users are loaded
    const [isOpen, setIsOpen] = useState(false); // New state to track if user list is open or closed
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [modalUsername, setModalUsername] = useState('');
    const [modalEmail, setModalEmail] = useState('');
    const [modalPassword, setModalPassword] = useState('');
    const [modalCountry, setModalCountry] = useState('');
    const [error, setError] = useState('');
    const fetchUsers = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/v1/users');
            console.log("Fetched users:", response.data);
            setUsers(response.data);
        } catch (error) {
            console.error('Error fetching users:', error);
        }
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
        setName(e.target.value);
    };

    const handleModalInputChange = (e) => {
        const { name, value } = e.target;
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

    const handleToggleUsers = () => {
        setIsOpen(!isOpen); // Toggle isOpen state
        if (!isLoaded) {
            fetchUsers(); // Load users only if not already loaded
            setIsLoaded(true); // Set isLoaded to true once users are loaded
        }
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
            {/* Button to toggle users */}
            <hr></hr>
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
                {error && <div style={{ color: 'red', marginBottom: '10px' }}>{error}</div>}
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


            {isOpen && ( // Render user list only if isOpen is true
                <>
                    <h3>All Users</h3>
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
                        {users.map(user => (
                            <UserCard key={user.id} user={user} onDelete={handleDeleteUser}/>
                        ))}
                        </tbody>
                    </table>
                </>
            )}
        </div>
    );
};

export default UserCrud;

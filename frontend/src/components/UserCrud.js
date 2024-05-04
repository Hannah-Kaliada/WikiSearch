import React, {useState} from 'react';
import axios from 'axios';
import './UserCrud.css';

const UserCard = ({user, onDelete}) => {
    return (
        <>
            <tr className="user-card">
                <td>{user.id}</td>
                <td>{user.username}</td>
                <td>{user.email}</td>
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
            const response = await axios.post('http://localhost:8080/api/v1/users/addUser', {
                username: name, email: "etegf",
                password: "etdjsddskiegf", favoriteArticles: []
            });
            console.log("Added user:", response.data);
            setUsers([...users, response.data]);
            setName('');
        } catch (error) {
            console.error('Error creating user:', error);
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

    const handleToggleUsers = () => {
        setIsOpen(!isOpen); // Toggle isOpen state
        if (!isLoaded) {
            fetchUsers(); // Load users only if not already loaded
            setIsLoaded(true); // Set isLoaded to true once users are loaded
        }
    };

    return (
        <div>
            <hr></hr>
            <div style={{display: 'flex', justifyContent: 'center', marginBottom: '10px'}}>
                <button onClick={handleToggleUsers}>{isOpen ? 'Hide Users' : 'Show Users'}</button>
            </div>
            {/* Button to toggle users */}
            <hr></hr>
            <label>Create User: </label>
            <input type="text" value={name} onChange={handleInputChange}/>
            <button onClick={handleCreateUser}>Add User</button>
            {isOpen && ( // Render user list only if isOpen is true
                <>
                    <h3>All Users</h3>
                    <table className="user-table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Email</th>
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

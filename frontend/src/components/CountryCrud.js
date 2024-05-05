import React, {useState, useEffect} from 'react';
import axios from 'axios';
import '../AdminPage.css';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faTrash} from '@fortawesome/free-solid-svg-icons';
import {faSync} from "@fortawesome/free-solid-svg-icons/faSync";

const CountryCrud = ({country, onCreate, onUpdate, onDelete}) => {
    const [name, setName] = useState(country ? country.name : '');
    const [countries, setCountries] = useState([]);
    const [showAllCountries, setShowAllCountries] = useState(false);
    const [updatedName, setUpdatedName] = useState('');
    const [showUpdateInput, setShowUpdateInput] = useState(false);
    const [selectedCountryId, setSelectedCountryId] = useState(null);
    const [sortBy, setSortBy] = useState(null);

    const sortCountries = () => {
        if (sortBy === 'name') {
            setCountries(countries.slice().sort((a, b) => a.name.localeCompare(b.name)));
        } else if (sortBy === 'id') {
            setCountries(countries.slice().sort((a, b) => a.id - b.id));
        }
    };

    const selectCountryForUpdate = (id) => {
        setSelectedCountryId(id);
    };

    const cancelUpdate = () => {
        setSelectedCountryId(null);
    };

    const fetchCountries = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/v1/countries');
            setCountries(response.data);
        } catch (error) {
            console.error('Error fetching countries:', error);
        }
    };


    const pollCountries = () => {
        setInterval(() => {
            if (showAllCountries) {
                fetchCountries();
            }
        }, 1000);
    };

    useEffect(() => {
        if (showAllCountries) {
            fetchCountries();
            pollCountries();
        }
    }, [showAllCountries]);
    useEffect(() => {
        sortCountries();
    }, [sortBy, countries]);

    const handleCreate = async () => {
        try {
            const response = await axios.post('http://localhost:8080/api/v1/countries/addCountry', {name});
            onCreate(response.data);
            setName('');
        } catch (error) {
            console.error('Error creating country:', error);
        }
    };

    const handleUpdate = async (id) => {
        try {
            const response = await axios.put(`http://localhost:8080/api/v1/countries/updateCountry/${id}`, {name: updatedName});
            onUpdate(response.data);
            fetchCountries();
            setShowUpdateInput({...showUpdateInput, [id]: false});
        } catch (error) {
            console.error('Error updating country:', error);
        }
    };
    const handleDelete = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/api/v1/countries/deleteCountry/${id}`);
            onDelete(id);
        } catch (error) {
            console.error('Error deleting country:', error);
        }
    };

    return (
        <div className="crud-form">
            <hr></hr>
            <div style={{display: 'flex', justifyContent: 'center', marginBottom: '10px'}}>
                <button onClick={() => setShowAllCountries(!showAllCountries)}>
                    {showAllCountries ? 'Hide Countries' : 'Show Countries'}
                </button>
            </div>
            {showAllCountries && (
                <div>
                    <h3>All Countries</h3>
                    <div>
                        <button onClick={() => setSortBy('name')}>Sort by Name</button>
                        <button onClick={() => setSortBy('id')}>Sort by ID</button>
                    </div>
                    <table style={{width: '100%', textAlign: 'center'}}>
                        <tbody>
                        {countries.map((country) => (
                            <tr key={country.id}>
                                <td>{country.id}</td>
                                <td>{country.name}</td>
                                <td>
                                    <button onClick={() => handleDelete(country.id)}>
                                        <FontAwesomeIcon icon={faTrash} style={{color: 'grey'}}/>
                                    </button>
                                </td>
                                <td>
                                    {selectedCountryId !== country.id ? (
                                        <button onClick={() => selectCountryForUpdate(country.id)}>
                                            <FontAwesomeIcon icon={faSync} style={{color: 'black'}}/>
                                        </button>
                                    ) : (
                                        <>
                                            <input
                                                type="text"
                                                value={updatedName}
                                                onChange={(e) => setUpdatedName(e.target.value)}
                                                placeholder="New name"
                                                style={{marginLeft: '10px'}}
                                            />
                                            <button onClick={() => handleUpdate(country.id)}>Update</button>
                                            <button onClick={cancelUpdate}>Cancel</button>
                                        </>
                                    )}
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
            <hr></hr>
            <label>
                Name:
                <input type="text" value={name} onChange={(e) => setName(e.target.value)}/>
            </label>
            {country ? (
                <div>
                    <button onClick={handleUpdate}>Update</button>
                    <button onClick={() => handleDelete(country.id)}>Delete</button>
                </div>
            ) : (
                <button onClick={handleCreate}>Create</button>
            )}
        </div>
    );
};

export default CountryCrud;
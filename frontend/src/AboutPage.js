import React from 'react';
import {Link} from 'react-router-dom';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faSearch} from '@fortawesome/free-solid-svg-icons';

const AboutPage = () => {
    return (
        <div style={styles.container}>
            <h1 style={styles.heading}>About WikiSearch</h1>
            <p style={styles.paragraph}>
                WikiSearch is a server dedicated to providing information from Wikipedia. It allows users to search for
                articles and save their favorite articles to their account.
            </p>
            <p style={styles.paragraph}>
                <Link to="/home" style={styles.link}>
                    <FontAwesomeIcon icon={faSearch} style={styles.icon}/> Start searching...
                </Link>
            </p>
            <p style={styles.contact}>
                For any inquiries or assistance, please contact us at: hannah.kaliada@gmail.com
            </p>
        </div>
    );
};

const styles = {
    container: {
        maxWidth: '800px',
        margin: '0 auto',
        padding: '40px',
        textAlign: 'center',
        backgroundColor: '#f0f0f0',
        color: '#333',
        borderRadius: '10px',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
    },
    heading: {
        fontSize: '32px',
        fontWeight: 'bold',
        marginBottom: '20px',
        fontFamily: 'Rubik, sans-serif',
        color: '#333',
    },
    paragraph: {
        fontSize: '18px',
        lineHeight: '1.6',
        fontFamily: 'Rubik, sans-serif',
        color: '#333',
    },
    link: {
        color: '#000000',
        textDecoration: 'none',
        fontWeight: 'bold',
        marginLeft: '10px',
        display: 'inline-flex',
        alignItems: 'center',
    },
    icon: {
        marginRight: '5px',
    },
    contact: {
        fontSize: '16px',
        marginTop: '40px',
        fontFamily: 'Rubik, sans-serif',
        color: '#555',
    },
};

export default AboutPage;

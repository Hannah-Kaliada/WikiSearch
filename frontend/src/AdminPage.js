import React, {useState} from 'react';
import './AdminPage.css';
import CountryCrud from './components/CountryCrud';
import UserCrud from './components/UserCrud';
import ArticleCrud from "./components/ArticlesCrud";

const EntityBlock = ({entityName}) => {
    const [expanded, setExpanded] = useState(false);

    const handleToggle = () => {
        setExpanded(!expanded);
    };

    let entityInfo = null;

    switch (entityName) {
        case 'Countries':
            entityInfo = (
                <div>
                    <CountryCrud/>
                </div>
            );
            break;
        case 'Users':
            entityInfo = (
                <div>
                    <UserCrud/>
                </div>
            );
            break;
        case 'Articles':
            entityInfo = (
                <div>
                    <ArticleCrud/>
                </div>
            );
            break;
        default:
            entityInfo = <p>Нет информации для сущности</p>;
    }

    return (
        <div className="entity-block">
            <div className="block-header" onClick={handleToggle}>
                <h2>{entityName}</h2>
                <span>{expanded ? '-' : '+'}</span>
            </div>
            {expanded && <div className="block-content">{entityInfo}</div>}
        </div>
    );
};

const AdminPage = () => {
    const [password, setPassword] = useState('');
    const [authenticated, setAuthenticated] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const correctHash = '93969d193161ae6fbd17b30055a9ed9789cb4bd80a7df0c6df15d3dd98fd7591';


        const enteredPasswordHash = await sha256(password);

        if (enteredPasswordHash === correctHash) {
            setAuthenticated(true);
        } else {
            alert('Incorrect password!');
        }
    };

    const sha256 = async (str) => {
        const buffer = new TextEncoder('utf-8').encode(str);
        const hashBuffer = await crypto.subtle.digest('SHA-256', buffer);
        const hashArray = Array.from(new Uint8Array(hashBuffer));
        const hashHex = hashArray.map(b => ('00' + b.toString(16)).slice(-2)).join('');
        return hashHex;
    };

    return (
        <div className={`admin-container ${authenticated ? 'authenticated' : ''}`}>
            <div className="content">
                <h1 className="title">Master page</h1>

                {authenticated ? (
                    <div>
                        <EntityBlock entityName="Countries"/>
                        <EntityBlock entityName="Users"/>
                        <EntityBlock entityName="Articles"/>
                    </div>
                ) : (
                    <form onSubmit={handleSubmit}>
                        <label className="label">
                            Password:
                            <input
                                className="input"
                                type="password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </label>
                        <button className="button" type="submit">
                            Login
                        </button>
                    </form>
                )}
            </div>
        </div>
    );
};

export default AdminPage;
import React, {useState} from 'react';
import './AdminPage.css';
import CountryCrud from "./components/CountryCrud";
import UserCrud from "./components/UserCrud";


// Компонент для отображения блока с CRUD операциями для одной сущности
const EntityBlock = ({entityName}) => {
    const [expanded, setExpanded] = useState(false);

    const handleToggle = () => {
        setExpanded(!expanded);
    };

    let entityInfo = null;

    // В зависимости от имени сущности, отображаем разную информацию
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
                    <p>Информация о сущности 3</p>
                    {/* Дополнительная информация или формы для CRUD операций для сущности 3 */}
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
            {expanded && (
                <div className="block-content">
                    {/* Отображаем информацию о сущности */}
                    {entityInfo}
                </div>
            )}
        </div>
    );
};

// Компонент административной страницы
const AdminPage = () => {
    const [password, setPassword] = useState('');
    const [authenticated, setAuthenticated] = useState(false);

    const handleSubmit = (e) => {
        e.preventDefault();

        // Проверяем, является ли введенный пароль правильным
        if (password === 'hp') {
            setAuthenticated(true);
        } else {
            alert('Incorrect password!');
        }
    };

    return (
        <div className={`admin-container ${authenticated ? 'authenticated' : ''}`}>
            <div className="content">
                <h1 className="title">
                    Master page
                    <img src="https://clan.akamai.steamstatic.com/images/37745685/6cb74d92fb1d51dd011db596961a42754b14b004.gif" alt="иконка" style={{width: '24px', height: '24px'}}/>
                </h1>


                {authenticated ? (
                    // Отображаем блоки с CRUD операциями для сущностей
                    <div>
                        <EntityBlock entityName="Countries"/>
                        <EntityBlock entityName="Users"/>
                        <EntityBlock entityName="Articles"/>
                    </div>
                ) : (
                    // Отображаем форму для ввода пароля для неаутентифицированных пользователей
                    <form onSubmit={handleSubmit}>
                        <label className="label">
                            Password:
                            <input className="input" type="password" value={password}
                                   onChange={(e) => setPassword(e.target.value)}/>
                        </label>
                        <button className="button" type="submit">Login</button>
                    </form>
                )}
            </div>
        </div>
    );
};

export default AdminPage;

import React, { useContext } from 'react';
import AuthService from '../../../API/AuthService'
import { AuthContext } from "../../../context";
import { useNavigate } from "react-router-dom";
import { useFetching } from '../../../hooks/UseFetching'

const Navbar = () => {
    const { isAuth, setIsAuth, authToken, setAuthToken, curentUser, setCurentUser, setRefToken } = useContext(AuthContext);
    const navigate = useNavigate();

    const [fetchLogout, ,] = useFetching(async () => {
        const response = await AuthService.postLogout(authToken);
        if (response.status === 200) {
            
        }
    });

    const logout = () => {
        fetchLogout();
        setAuthToken('');
        setCurentUser('');
        setIsAuth(false);
        setRefToken('');
        localStorage.removeItem('authorized');
        localStorage.removeItem('JWT-token');
        localStorage.removeItem('CurentUser');
        localStorage.removeItem('CurentUserId');
        localStorage.removeItem('JWT-refresh-token');
    };

    const navLogin = event => {
        event.preventDefault();
        navigate("/login");
    };

    const navReg = event => {
        event.preventDefault();
        navigate("/reg");
    };

    const navChats = event => {
        event.preventDefault();
        navigate("/chats");
    };

    const navContacts = event => {
        event.preventDefault();
        navigate("/contacts");
    };

    return (
        <div className='col-12 m-0 p-0' style={{ backgroundColor: "#e3f2fd" }}>
            <header className="d-flex flex-wrap align-items-center justify-content-md-between py-2 mb-1">
                <div className="col-4 mb-2 mb-md-0">
                    <a href="/" className="d-flex align-items-center mx-4 mb-3 mb-md-0 me-md-auto link-body-emphasis text-decoration-none">
                        <span className="fs-4">Мессенджер</span>
                    </a>
                </div>
                {isAuth ?
                    <ul className="nav col-6 col-md-auto mb-2 justify-content-center mb-md-0">
                        <li><a href="/" className="nav-link px-2 link-secondary" onClick={navChats}>Главная</a></li>
                        <li><a href="/" className="nav-link px-2" onClick={navContacts}>Контакты</a></li>
                        <li><a href="/" className="nav-link px-2">О нас</a></li>
                    </ul>
                    :
                    <ul class="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">

                    </ul>
                }
                {isAuth ?
                    <div className="col-4 text-end">
                        <div className='row justify-content-end'>
                            <div className='col-4 p-1 m-1 me-2'>
                                <strong className=''>{curentUser}</strong>
                            </div>
                            <div className='col-2 me-5'>
                                <button type="button" className="btn btn-outline-primary me-2" onClick={logout}>Выйти</button>
                            </div>
                        </div>
                    </div>
                    :
                    <div className="col-md-3 text-end">
                        <button type="button" className="btn btn-sm btn-outline-primary me-2" onClick={navLogin}>Войти</button>
                        <button type="button" className="btn btn-sm btn-primary me-2" onClick={navReg}>Регистрация</button>
                    </div>
                }
            </header >
        </div >
    );
};

export default Navbar;
import { useContext, useState } from "react"
import { AuthContext } from "../context";
import AuthService from "../API/AuthService"
import { useNavigate } from "react-router-dom";
import { useFetching } from '../hooks/UseFetching'

const Login = () => {
    const { setIsAuth, setAuthToken, setCurentUser, setCurentUserId, setRefToken } = useContext(AuthContext);
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [fetchLogin, , error] = useFetching(async () => {
        const response = await AuthService.postLogin(username, password);
        if (response.data.token.length !== 0) {
            setAuthToken(response.data.token);
            setIsAuth(true);
            setCurentUser(response.data.username);
            setCurentUserId(response.data.idUser);
            setRefToken(response.data.refreshToken);
            localStorage.setItem('authorized', true);
            localStorage.setItem('JWT-token', response.data.token);
            localStorage.setItem('CurentUser', response.data.username);
            localStorage.setItem('CurentUserId', response.data.idUser);
            localStorage.setItem('JWT-refresh-token', response.data.refreshToken)
            console.log(response.data)
            console.log(response.data.refreshToken)
        }
    });
    const navigate = useNavigate();

    const login = event => {
        event.preventDefault();
        if (username.length !== 0 || password.length !== 0) {
            fetchLogin();
            if (typeof error !== "undefined" && error.status === 401) {
                console.log("Введены неверные данные")
            }

        }
    }

    const createNav = event => {
        event.preventDefault();
        navigate("/reg");
    }

    return (
        <div className="container">
            <div className="row vh-100 align-items-center justify-content-center">
                <form className="col-5 p-4 mb-5 p-md-5 border rounded-4 bg-body-tertiary" onSubmit={login}>
                    <h1 className="text-center mb-4">Вход пользователя</h1>
                    <div className="mb-3">
                        <label className="form-label">Имя пользователя</label>
                        <input type="username" className="form-control" placeholder="" value={username}
                            onChange={
                                (e) => setUsername(e.target.value)
                            } />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Пароль</label>
                        <input type="password" className="form-control" placeholder="********" value={password}
                            onChange={
                                (e) => setPassword(e.target.value)
                            } />
                    </div>
                    <p>Нет аккаунта?<a href="/" onClick={createNav}> Создайте.</a></p>
                    <div className="row justify-content-center">
                        <button type="submit" className="col-4 m-2 mt- p-2 fs-2 btn btn-lg btn-success">Войти</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default Login;
import { useState } from "react"
import AuthService from "../API/AuthService"
import { useNavigate } from "react-router-dom";
import { useFetching } from "../hooks/UseFetching";

const Registration = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');
    const navigate = useNavigate();
    const [fetchReg, , error] = useFetching(async () => {
        const response = await AuthService.postRegistration(username, password, email, phone);
        if (response.status === 200) {
            createNav();
        }
    });

    const registration = event => {
        event.preventDefault();
        if (username.length !== 0 || password.length !== 0 || email.length !== 0 || phone.length === 10) {
            fetchReg();
            if (typeof error !== "undefined" && error.status === 401) {
                console.log("Введены неверные данные");
                return;
            }
        }
    }

    const createNav = event => {
        event.preventDefault();
        navigate("/login");
    }

    return (
        <div className="container">
            <div className="row  vh-100 align-items-center justify-content-center">
                <form className="col-5 p-4 mt-3 mb-3 p-md-5 border rounded-4 bg-body-tertiary" onSubmit={registration}>
                    <h1 className="text-center mb-4">Регистрация</h1>
                    <div className="mb-3">
                        <label className="form-label">Имя пользователя</label>
                        <input type="username" className="form-control" placeholder="" value={username}
                            onChange={
                                (e) => setUsername(e.target.value)
                            } />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Электронная почта</label>
                        <input type="email" className="form-control" placeholder="name@example.com" value={email}
                            onChange={
                                (e) => setEmail(e.target.value)
                            } />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Телефон</label>
                        <input type="phone" className="form-control" placeholder="+7(999)999-99-99" value={phone}
                            onChange={
                                (e) => setPhone(e.target.value)
                            } />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Пароль</label>
                        <input type="password" className="form-control" placeholder="********" value={password}
                            onChange={
                                (e) => setPassword(e.target.value)
                            } />
                    </div>
                    <p>Уже создали аккаунт?<a href="/" onClick={(e) => createNav(e)}> Войдите здесь.</a></p>
                    <div className="row justify-content-center">
                        <button type="submit" className="col-8 m-2 mt-3 p-2 fs-2 btn btn-lg btn-primary">Регистрация</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default Registration;
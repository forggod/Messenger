import React, { useEffect, useState } from 'react';
import { BrowserRouter } from 'react-router-dom';
import AppRouter from './components/AppRouter';
import { AuthContext } from "./context";
import Navbar from './components/UI/Navbar/Navbar';
import './styles/App.css'

const App = () => {
    const [isAuth, setIsAuth] = useState(false)
    const [authToken, setAuthToken] = useState('')
    const [refToken, setRefToken] = useState('')
    const [isLoading, setIsLoading] = useState(true)
    const [curentUser, setCurentUser] = useState('')
    const [curentUserId, setCurentUserId] = useState('')
    
    useEffect(() => {
        setIsAuth(localStorage.getItem('authorized'))
        setAuthToken(localStorage.getItem('JWT-token'))
        setRefToken(localStorage.getItem('JWT-refresh-token'))
        setCurentUser(localStorage.getItem('CurentUser'))
        setCurentUserId(localStorage.getItem('CurentUserId'))
        setIsLoading(false)
    }, [])

    return (
        <div>
            <AuthContext.Provider value={{
                isAuth,
                setIsAuth,
                authToken,
                setAuthToken,
                refToken,
                setRefToken,
                isLoading,
                curentUser,
                setCurentUser,
                curentUserId,
                setCurentUserId
            }}>
                <BrowserRouter>
                    <header style={{padding: 0, margin: 0}}>
                        <Navbar />
                    </header>
                    <div style={{ backgroundColor: "rgb(153 204 255)", padding: 0, margin: 0 }}>
                        <AppRouter />
                    </div>
                </BrowserRouter>
            </AuthContext.Provider>
            <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy" crossorigin="anonymous"></script>
        </div>
    )
}

export default App
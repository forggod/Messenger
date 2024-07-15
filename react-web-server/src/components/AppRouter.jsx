import React, { useContext } from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import { privateRoutes, publicRoutes } from '../router'
import { AuthContext } from "../context";
import Loader from '../components/UI/Loading/Loading'

const AppRouter = () => {
    const { isAuth, authToken, isLoading, curentUser, curentUserId, refToken } = useContext(AuthContext);
    // console.log('Authorized: ' + isAuth)
    // console.log('Token: ' + authToken)
    // console.log('CurentUser: ' + curentUser)
    // console.log('CerentUserId: ' + curentUserId)
    // console.log('RefreshToken: ' + refToken)

    if (isLoading) {
        return <Loader />
    }

    return (
        isAuth
            ?
            <Routes>
                {privateRoutes.map(route =>
                    <Route
                        element={route.element}
                        path={route.path}
                        exact={route.exact}
                        key={route.path}
                    />
                )}
                <Route path="*" element={<Navigate to='/chats' />} />
            </Routes>
            :
            <Routes>
                {publicRoutes.map(route =>
                    <Route
                        element={route.element}
                        path={route.path}
                        exact={route.exact}
                        key={route.path}
                    />
                )}
                <Route path="*" element={<Navigate to='/login' />} />
            </Routes >
    );
};

export default AppRouter;
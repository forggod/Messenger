import Login from "../pages/Login";
import Registration from "../pages/Registration"
import Chats from "../pages/Chats"
import Contacts from "../pages/Contacts"


export const privateRoutes = [
    { path: '/chats', element: <Chats />, exact: true },
    { path: '/contacts', element: <Contacts />, exact: true },
]

export const publicRoutes = [
    { path: '/login', element: <Login />, exact: true },
    { path: '/reg', element: <Registration />, exact: true },
]

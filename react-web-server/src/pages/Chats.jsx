import { useContext, useEffect, useState } from "react"
import { AuthContext } from "../context";
import AuthService from "../API/AuthService";
import { useFetching } from '../hooks/UseFetching';
import iconSend from '../img/SendIcon.png';
import iconAdd from '../img/AddIcon.png';

const Login = () => {
    const { authToken, curentUserId, setCurentUser, curentUser, setCurentUserId, setIsAuth, setAuthToken, refToken, setRefToken } = useContext(AuthContext);
    const [chats, setChats] = useState([]);
    const [usersInChat, setUsersInChat] = useState([]);
    const [usersToAdd, setUsersToAdd] = useState([]);
    const [contacts, setContacts] = useState([]);
    const [messages, setMessages] = useState([]);
    const [messageForm, setMessageForm] = useState('');
    const [nameChat, setNameChat] = useState('');
    const [descriptionChat, setDescriptionChat] = useState('');
    const [activeChat, setActiveChat] = useState(-1);
    const [idMessage, setIdMessage] = useState();

    const [fetchRefreshToken, , errorRefreshToken] = useFetching(async () => {
        const response = await AuthService.refreshToken(refToken, curentUser);
        if (typeof error !== "undefined" && errorRefreshToken.status === 401) {
            // setAuthToken('');
            // setCurentUser('')
            // setIsAuth(false);
            // setRefToken('');
            // localStorage.removeItem('authorized');
            // localStorage.removeItem('JWT-token');
            // localStorage.removeItem('CurentUser');
            // localStorage.removeItem('CurentUserId');
            // localStorage.removeItem('JWT-refresh-token');
        } else if (response.status === 200) {
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
        }
    });

    const [fetchChats, ,] = useFetching(async () => {
        try {
            const response = await AuthService.getChats(authToken);
            if (response.status === 200) {
                setChats(response.data.chats)
            }
        } catch (error) {
            if (error.response.status === 401) {
                refreshCurentToken();
            }
        };
    });

    const [fetchAddChat, ,] = useFetching(async () => {
        try {
            const response = await AuthService.postNewChat(authToken, nameChat, descriptionChat)
            if (response.status === 200) {
                setChats(chats.concat(response.data));
            }
        } catch (error) {
            if (error.response.status === 401) {
                refreshCurentToken();
            }
        };
    });

    const [fetchLoadChatHistory, ,] = useFetching(async () => {
        try {
            const response = await AuthService.getChatHistory(authToken, chats[activeChat].id)
            if (response.status === 200) {
                setMessages(response.data.messages);
            }
        } catch (error) {
            if (error.response.status === 401) {
                refreshCurentToken();
            }
        };
    });

    const [fetchAddMessage, ,] = useFetching(async () => {
        try {
            const response = await AuthService.postNewMessage(authToken, chats[activeChat].id, messageForm)
            if (response.status === 200) {
                setMessages(messages.concat(response.data));
            }
        } catch (error) {
            if (error.response.status === 401) {
                refreshCurentToken();
            }
        };
    });

    const [fetchLoadChatUsers, ,] = useFetching(async () => {
        try {
            const response = await AuthService.getChatUsers(authToken, chats[activeChat].id)
            if (response.status === 200) {
                setUsersInChat(response.data.users);
            }
        } catch (error) {
            if (error.response.status === 401) {
                refreshCurentToken();
            }
        };
    });

    const [fetchAddChatUsers, ,] = useFetching(async () => {
        try {
            const transformedArray = usersToAdd.map(item => ({ id: item.id }));
            const response = await AuthService.postChatAddUsers(authToken, chats[activeChat].id, transformedArray);
            if (response.status === 200) {
                setUsersInChat(usersInChat.concat(response.data));
            }
        } catch (error) {
            if (error.response.status === 401) {
                refreshCurentToken();
            }
        };
    });

    const [fetchingLoadContacts, ,] = useFetching(async () => {
        try {
            const response = await AuthService.getContacts(authToken);
            if (response.status === 200) {
                setContacts(response.data.users);
            }
        } catch (error) {
            if (error.response.status === 401) {
                refreshCurentToken();
            }
        };
    });

    const [fetchDeleteMessage, ,] = useFetching(async () => {
        try {
            const response = await AuthService.postDeleteMessage(authToken, idMessage);
        } catch (error) {
            if (error.response.status === 401) {
                refreshCurentToken();
            }
        };
    });

    useEffect(() => {
        loadCahts();
        const chatTimer = setInterval(() => {
            refreshChats();
        }, 10000);
        const historyTimer = setInterval(() => {
            refreshMessages();
        }, 5000);
    }, []);

    useEffect(() => {
        loadChatHistory();
        loadChatUsers();
    }, [activeChat]);

    const refreshCurentToken = () => {
        fetchRefreshToken();
    };

    const refreshChats = () => {
        loadCahts();
    };

    const refreshMessages = () => {
        loadChatHistory();
    };

    const loadCahts = () => {
        fetchChats();
    };

    const addChat = (event) => {
        event.preventDefault();
        fetchAddChat();
    };

    const changeChat = (index) => {
        setActiveChat(index);
    };

    const loadChatHistory = () => {
        if (chats[activeChat]) {
            fetchLoadChatHistory();
        }
    };

    const addNewMessage = (event) => {
        event.preventDefault();
        if (chats[activeChat] && messageForm.length) {
            fetchAddMessage();
            setMessageForm('');
        }
    };

    const messageDelete = (id) => {
        if (chats[activeChat]){
            setIdMessage(id);
            fetchDeleteMessage();
        }
    };

    const loadChatUsers = () => {
        if (chats[activeChat]) {
            fetchLoadChatUsers();
        }
    };

    const loadContacts = () => {
        fetchingLoadContacts();
    };

    const addChatUsers = () => {
        if (usersToAdd.length) {
            console.log(usersToAdd)
            fetchAddChatUsers();
        }
    };

    const isUserInList = (contact) => {
        return usersToAdd !== undefined && usersToAdd !== null && usersToAdd.some(item =>
            Object.keys(contact).every(key => item[key] === contact[key])
        );
    };


    const userAddListChange = (contact) => {
        if (isUserInList(contact)) {
            setUsersToAdd(usersToAdd.filter(item => item !== contact));
        } else {
            setUsersToAdd(usersToAdd.concat(contact));
        }
    };

    const formatDate = (isoDateString) => {
        const date = new Date(isoDateString);
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');

        return `${day}.${month}.${year}-${hours}:${minutes}`;
    }

    return (
        <div className="container">
            <div className="row justify-content-center" style={{ height: "90vh" }}>
                <div id="chats" className="col-3 p-3 m-3 me-1 rounded-end rounded-5 border bg-body-tertiary">
                    <div class="d-flex flex-column align-items-stretch flex-shrink-0 bg-body-tertiary" style={{ height: "80vh" }}>
                        <div className="row justify-content-center align-items-center p-0">
                            <h3 class="col-8 m-2 p-2 fw-semibold">Список чатов</h3>
                            <div className="col-2 justify-content-center align-items-center p-0">
                                <button type="button" class="btn p-0 m-0" data-bs-toggle="modal" data-bs-target="#staticBackdrop"
                                    style={{ width: "45px", height: "45px" }}>
                                    <img src={iconAdd} alt="Add Icon" style={{ with: "30px", height: "30px" }} />
                                </button>
                            </div>
                        </div>
                        <div class="overflow-auto list-group list-group-flush border-bottom scrollarea" style={{ overflow: 'hidden' }}>
                            {chats.length === 0 ?
                                (<h6 class="m-2 p-2 fw-semibold" style={{ textAlign: 'center' }}>
                                    Чаты не найдены!
                                </h6>
                                )
                                : (
                                    chats.map((chat, index) => {
                                        return (
                                            <div className="chat" key={index}>
                                                <a href="#" class={`list-group-item list-group-item-action  py-3 lh-sm ${index === activeChat ? 'active' : ''}`} aria-current="true"
                                                    onClick={() => changeChat(index)}>
                                                    <div class="d-flex w-100 align-items-center justify-content-between">
                                                        <strong class="mb-1">{chat.name}</strong>
                                                    </div>
                                                    <div class="col-10 mb-1 small">{chat.description}</div>
                                                </a>
                                            </div>
                                        )
                                    })
                                )}
                        </div>
                    </div>
                </div>
                <div id="messages" className="col-5 p-3 m-3 mx-1 me-1 rounded-2 border bg-body-tertiary">
                    <div class="container d-flex flex-column align-items-stretch flex-shrink-0 bg-body-tertiary" style={{ height: "80vh", position: "relative" }}>
                        <div className="row justify-content-center align-items-center text-center">
                            <h4 class="col-8 m-2 p-2 fw-semibold">{chats[activeChat] !== undefined ? chats[activeChat].name : 'Выберите чат'}</h4>
                        </div>
                        <div class="overflow-auto list-group list-group-flush border-bottom scrollarea">
                            {messages.length === 0 ?
                                (<h6 class="m-2 p-2 fw-semibold" style={{ textAlign: 'center' }}>
                                    {chats[activeChat] !== undefined ? "Сообщений нет" : ''}
                                </h6>
                                )
                                : (
                                    messages.map((message, index) => {
                                        return (
                                            <div key={index}>
                                                <a href="#" class="list-group-item list-group-item-action  py-3 lh-sm " aria-current="true">
                                                    <div className="row justify-content-between">
                                                        <div class="col-6 mb-1 small">{message.author}</div>
                                                        <div class="col-4 mb-1 small">{formatDate(message.time)}</div>
                                                    </div>
                                                    <div class="col-10 mb-1">{message.body}</div>
                                                </a>
                                            </div>
                                        )
                                    })
                                )}
                        </div>
                        {!chats[activeChat] ?
                            (
                                <div>
                                </div>
                            )
                            : (
                                <form className="row mt-2 justify-content-between mt-auto pt-2" onSubmit={(e) => addNewMessage(e)} style={{}}>
                                    <div className="col-10">
                                        <input type="text" class="form-control" placeholder="Сообщение..." value={messageForm} onChange={
                                            (e) => setMessageForm(e.target.value)
                                        } />
                                    </div>
                                    <div className="col-2">
                                        <button type="submit" class="btn btn-light form-control" style={{ with: "30px", height: "40px" }}>
                                            <img src={iconSend} alt="Download Icon" style={{ with: "30px", height: "30px" }} /></button>
                                    </div>
                                </form>
                            )}
                    </div>
                </div>
                <div id="chatUsers" className="col-3 p-3 m-3 mx-1 rounded-start rounded-5 border bg-body-tertiary">
                    <div class="d-flex flex-column align-items-stretch flex-shrink-0 bg-body-tertiary" style={{ height: "80vh" }}>
                        <div className="row justify-content-center align-items-center">
                            <div className="col-2 justify-content-center align-items-center p-0">
                                {chats[activeChat] === undefined ? '' :
                                    <button type="button" class="btn p-0 m-0" data-bs-toggle="modal" data-bs-target="#addUsersInChat"
                                        style={{ width: "45px", height: "45px" }} onClick={loadContacts}>
                                        <img src={iconAdd} alt="Add Icon" style={{ with: "30px", height: "30px" }} />
                                    </button>
                                }
                            </div>
                            <h3 class="col-8 m-2 p-2 fw-semibold">Пользователи</h3>
                        </div>
                        <div class="overflow-auto list-group list-group-flush border-bottom scrollarea" style={{ overflow: 'hidden' }}>
                            {usersInChat.length === 0 ?
                                (<h6 class="m-2 p-2 fw-semibold" style={{ textAlign: 'center' }}>
                                    Пользователей нет!
                                </h6>
                                )
                                : (
                                    usersInChat.map((user, index) => {
                                        return (
                                            <div className="user" key={index}>
                                                <a href="#" class={`list-group-item list-group-item-action  py-3 lh-sm " + ${curentUserId === user.id ? 'active' : ''}`} aria-current="true">
                                                    <div class="d-flex w-100 align-items-center justify-content-between">
                                                        <strong class="mb-1 small">{user.username}</strong>
                                                    </div>
                                                </a>
                                            </div>
                                        )
                                    })
                                )}
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="addUsersInChat" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h1 class="modal-title fs-3" id="staticBackdropLabel">Добавить пользователей</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form className="" onSubmit={addChatUsers}>
                                <div class="modal-body">
                                    <div class="overflow-auto list-group list-group-flush border-bottom scrollarea" style={{ overflow: 'hidden' }}>
                                        {contacts.length === 0 ?
                                            (<h6 class="m-2 p-2 fw-semibold" style={{ textAlign: 'center' }}>
                                                Нет контактов!
                                            </h6>
                                            )
                                            : (
                                                contacts.map((contact, index) => {
                                                    return (
                                                        <div className="user" key={index}>
                                                            <a href="#" class={`list-group-item list-group-item-action py-3 lh-sm ${isUserInList(contact) ? "active" : ""}`} aria-current="true"
                                                                onClick={() => userAddListChange(contact)}>
                                                                <div class="d-flex w-100 align-items-center justify-content-between">
                                                                    <strong class="mb-1 small">{contact.username}</strong>
                                                                </div>
                                                            </a>
                                                        </div>
                                                    )
                                                })
                                            )}
                                    </div>
                                </div>
                                <div className="row justify-content-center">
                                    <button type="submit" className="col-4 m-2 mt- p-2 fs-4 btn btn-lg btn-success" data-bs-dismiss="modal" onClick={addChatUsers}>Добавить</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h1 class="modal-title fs-3" id="staticBackdropLabel">Создание чата</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form className="" onSubmit={addChat}>
                                <div class="modal-body">
                                    <div className="mb-3">
                                        <label className="fs-4 form-label">Название</label>
                                        <input type="username" className="form-control" placeholder="Чат пример" value={nameChat}
                                            onChange={
                                                (e) => setNameChat(e.target.value)
                                            } />
                                    </div>
                                    <div className="mb-3">
                                        <label className="fs-4 form-label">Описание</label>
                                        <input type="text" className="form-control" placeholder="Немного описания" value={descriptionChat}
                                            onChange={
                                                (e) => setDescriptionChat(e.target.value)
                                            } />
                                    </div>
                                    <div className="row justify-content-center">
                                        <button type="submit" className="col-4 m-2 mt- p-2 fs-2 btn btn-lg btn-success" data-bs-dismiss="modal">Создать</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Login;
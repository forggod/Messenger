import axios from "axios";
import UrlConstrains from "../utils/UrlConstants"

export default class AuthService {

    static async postLogin(username, password) {
        const response = await axios
            .post(UrlConstrains.BASE_URL + '/api/auth/login', {
                username: username,
                password: password
            })
        return response;
    }

    static async refreshToken(refreshToken, username) {
        const response = await axios
            .post(UrlConstrains.BASE_URL + '/api/auth/token/refresh', {
                refreshToken: refreshToken,
                username: username
            })
        return response;
    }

    static async postLogout(token) {
        const config = {
            headers: {
                Authorization: "Bearer " + token
            }
        };
        const response = await axios
            .post(UrlConstrains.BASE_URL + '/api/auth/logout', {}, config)
        return response;
    }

    static async postRegistration(username, password, email, phone) {
        const response = await axios
            .post(UrlConstrains.BASE_URL + '/api/auth/reg', {
                username: username,
                password: password,
                email: email,
                phone: phone
            })
        return response
    }

    static async getChats(token) {
        const config = {
            headers: {
                Authorization: "Bearer " + token
            }
        };
        const response = await axios
            .get(UrlConstrains.BASE_URL + '/api/messenger/chats', config)
        return response
    }

    static async postNewChat(token, name, description) {
        const config = {
            headers: {
                Authorization: "Bearer " + token
            }
        };
        const response = await axios
            .post(UrlConstrains.BASE_URL + '/api/messenger/chats/add', {
                name: name,
                description: description
            }, config);
        return response;
    }

    static async getChatHistory(token, chatId) {
        const config = {
            headers: {
                Authorization: "Bearer " + token
            }
        };
        const response = await axios
            .get(UrlConstrains.BASE_URL + `/api/messenger/chats/${chatId}/history`, config)
        return response
    }

    static async postNewMessage(token, chatId, message) {
        const config = {
            headers: {
                Authorization: "Bearer " + token
            }
        };
        const response = await axios
            .post(UrlConstrains.BASE_URL + `/api/messenger/chats/${chatId}/history/add-message`, {
                message: message
            }, config);
        return response;
    }

    static async postDeleteMessage(token, chatId, id) {
        const config = {
            headers: {
                Authorization: "Bearer " + token
            }
        };
        const response = await axios
            .post(UrlConstrains.BASE_URL + `/api/messenger/chats/${chatId}/history/delete-message`, {
                id: id
            }, config);
        return response;
    }

    static async getChatUsers(token, chatId) {
        const config = {
            headers: {
                Authorization: "Bearer " + token
            }
        };
        const response = await axios
            .get(UrlConstrains.BASE_URL + `/api/messenger/chats/${chatId}/show/users`, config)
        return response
    }

    static async postChatAddUsers(token, chatId, users) {
        const config = {
            headers: {
                Authorization: "Bearer " + token
            }
        };
        const response = await axios
            .post(UrlConstrains.BASE_URL + `/api/messenger/chats/${chatId}/add/user`, {
                users: users
            }, config);
        return response;
    }

    static async getContacts(token) {
        const config = {
            headers: {
                Authorization: "Bearer " + token
            }
        };
        const response = await axios
            .get(UrlConstrains.BASE_URL + `/api/messenger/contacts`, config)
        return response
    }

    static async getContactByName(token, username) {
        const config = {
            headers: {
                Authorization: "Bearer " + token
            }
        };
        const response = await axios
            .get(UrlConstrains.BASE_URL + `/api/messenger/contacts/findBy/${username}`, config)
        return response
    }

    static async postAddContact(token, contactId) {
        const config = {
            headers: {
                Authorization: "Bearer " + token
            }
        };
        const response = await axios
            .post(UrlConstrains.BASE_URL + `/api/messenger/contacts/add-contact/${contactId}`, {}, config);
        return response;
    }
}
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "../context";
import { useFetching } from "../hooks/UseFetching";
import AuthService from "../API/AuthService";
import iconAdd from "../img/AddIcon.png";

const Contacts = () => {
    const { authToken } = useContext(AuthContext);
    const [contacts, setContacts] = useState([]);
    const [contactName, setContactName] = useState('');
    const [contactFoudnId, setContactFoudnId] = useState();
    const [isLoadingFindByNameContact, setIsLoadingFindByNameContact] = useState(true);

    const [fetchingLoadContacts, isLoadingContacts] = useFetching(async () => {
        const response = await AuthService.getContacts(authToken);
        if (response.status === 200) {
            setContacts(response.data.users);
        }
    });


    const [fetchingFindByNameContact] = useFetching(async () => {
        setIsLoadingFindByNameContact(true);
        const response = await AuthService.getContactByName(authToken, contactName);
        console.log("Search response: " + response.data);
        if (response.status === 200) {
            setContactFoudnId(response.data.id);
            setIsLoadingFindByNameContact(false);
        }
    });

    const [fetchingAddContact, isLoadingAddContact] = useFetching(async () => {
        const response = await AuthService.postAddContact(authToken, contactFoudnId);
        console.log("Adding response: " + response.data);
        if (response.status === 200) {
            setContacts(contacts.concat(response.data));
        }
    });

    useEffect(() => {
        loadContacts();
    }, []);

    useEffect(() => {
        if (!isLoadingFindByNameContact) {
            console.log(isLoadingFindByNameContact)
            console.log(contactFoudnId)
            fetchingAddContact();
        }
    }, [isLoadingFindByNameContact]);

    const loadContacts = () => {
        fetchingLoadContacts();
    };

    const addContact = () => {
        fetchingFindByNameContact();
    };

    return (
        <div className="container">
            <div className="row justify-content-center" style={{ height: "90vh" }}>
                <div id="chats" className="col-6 p-3 m-3 me-1 rounded-5 border bg-body-tertiary">
                    <div class="d-flex flex-column align-items-stretch flex-shrink-0 bg-body-tertiary" style={{ height: "80vh" }}>
                        <div className="row justify-content-center align-items-center p-0">
                            <h3 class="col-8 m-2 p-2 fw-semibold">Список контактов</h3>
                            <div className="col-2 justify-content-center align-items-center p-0">
                                <button type="button" class="btn p-0 m-0" data-bs-toggle="modal" data-bs-target="#addContactForm"
                                    style={{ width: "45px", height: "45px" }}>
                                    <img src={iconAdd} alt="Add Icon" style={{ with: "30px", height: "30px" }} />
                                </button>
                            </div>
                        </div>
                        <div class="overflow-auto list-group list-group-flush border-bottom scrollarea" style={{ overflow: 'hidden' }}>
                            {contacts.length === 0 ?
                                (<h6 class="m-2 p-2 fw-semibold" style={{ textAlign: 'center' }}>
                                    Контакты не найдены!
                                </h6>
                                )
                                : (
                                    contacts.map((contact, index) => {
                                        return (
                                            <div className="chat" key={index}>
                                                <a href="#" class="list-group-item list-group-item-action  py-3 lh-sm " aria-current="true">
                                                    <div class="d-flex w-100 align-items-center justify-content-between">
                                                        <strong class="mb-1">{contact.username}</strong>
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

            <div class="modal fade" id="addContactForm" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h1 class="modal-title fs-3">Добавить контакт по имени</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form className="" onSubmit={addContact}>
                                <div class="modal-body">
                                    <div className="mb-3">
                                        <label className="fs-4 form-label">Имя пользователя</label>
                                        <input type="username" className="form-control" placeholder="example" value={contactName}
                                            onChange={
                                                (e) => setContactName(e.target.value)
                                            } />
                                    </div>
                                    <div className="row justify-content-center">
                                        <button type="submit" className="col-4 m-2 mt- p-2 fs-4 btn btn-lg btn-success" data-bs-dismiss="modal">Добавить</button>
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

export default Contacts;
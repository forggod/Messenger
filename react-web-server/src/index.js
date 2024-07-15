import * as ReactDOMClient from 'react-dom/client'
import App from './App'
import 'bootstrap/dist/css/bootstrap.min.css';
import $ from 'jquery';
import Popper from 'popper.js';
import 'bootstrap/dist/js/bootstrap.bundle.min';

const app = ReactDOMClient.createRoot(document.getElementById("root"))

app.render(<App />)
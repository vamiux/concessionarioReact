import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

// Configura axios per gestire CORS e errori
axios.defaults.withCredentials = true;
axios.defaults.headers.common['Content-Type'] = 'application/json';

// Configura axios per inviare il token JWT in ogni richiesta
axios.interceptors.request.use(
  config => {
    console.log('Invio richiesta:', {
      url: config.url,
      method: config.method,
      data: config.data,
      headers: config.headers
    });
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    console.error('Errore nella richiesta:', error);
    return Promise.reject(error);
  }
);

// Interceptor per gestire le risposte
axios.interceptors.response.use(
  response => {
    console.log('Risposta ricevuta:', {
      status: response.status,
      data: response.data,
      headers: response.headers
    });
    return response;
  },
  error => {
    console.error('Errore nella risposta:', {
      message: error.message,
      response: error.response,
      config: error.config
    });
    return Promise.reject(error);
  }
);

// Gestione degli utenti
const searchUtenti = async (params) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/utenti/search`, { params });
    return response.data;
  } catch (error) {
    console.error('Errore durante la ricerca degli utenti:', error);
    throw error;
  }
};

const getUtenti = async () => {
  try {
    console.log('Recupero utenti dal database...');
    const response = await axios.get(`${API_BASE_URL}/utenti`);
    console.log('Risposta dal server:', response.data);
    return response.data;
  } catch (error) {
    console.error('Errore durante il recupero degli utenti:', error);
    throw error;
  }
};

const getUtente = async (id) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/utenti/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Errore durante il recupero dell'utente con ID ${id}:`, error);
    throw error;
  }
};

const createUtente = async (utente) => {
  try {
    console.log('Invio richiesta POST a /api/utenti con dati:', utente);
    const response = await axios.post(`${API_BASE_URL}/utenti`, utente);
    console.log('Risposta dal server:', response.data);
    return response.data;
  } catch (error) {
    console.error('Errore durante la creazione dell\'utente:', error);
    console.error('Dettagli errore:', {
      status: error.response?.status,
      statusText: error.response?.statusText,
      data: error.response?.data,
      headers: error.response?.headers
    });
    throw error;
  }
};

const updateUtente = async (id, utente) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/utenti/${id}`, utente);
    return response.data;
  } catch (error) {
    console.error(`Errore durante l'aggiornamento dell'utente con ID ${id}:`, error);
    throw error;
  }
};

const deleteUtente = async (id) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/utenti/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Errore durante l'eliminazione dell'utente con ID ${id}:`, error);
    throw error;
  }
};

// Gestione dei veicoli
const searchVeicoli = async (params) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/veicoli/search`, { params });
    return response.data;
  } catch (error) {
    console.error('Errore durante la ricerca dei veicoli:', error);
    throw error;
  }
};

const getVeicoli = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/veicoli`);
    return response.data;
  } catch (error) {
    console.error('Errore durante il recupero dei veicoli:', error);
    throw error;
  }
};

const getVeicoliDisponibili = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/veicoli/disponibili`);
    return response.data;
  } catch (error) {
    console.error('Errore durante il recupero dei veicoli disponibili:', error);
    throw error;
  }
};

const getVeicolo = async (id) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/veicoli/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Errore durante il recupero del veicolo con ID ${id}:`, error);
    throw error;
  }
};

const createVeicolo = async (veicolo) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/veicoli`, veicolo);
    return response.data;
  } catch (error) {
    console.error('Errore durante la creazione del veicolo:', error);
    throw error;
  }
};

const updateVeicolo = async (id, veicolo) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/veicoli/${id}`, veicolo);
    return response.data;
  } catch (error) {
    console.error(`Errore durante l'aggiornamento del veicolo con ID ${id}:`, error);
    throw error;
  }
};

const deleteVeicolo = async (id) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/veicoli/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Errore durante l'eliminazione del veicolo con ID ${id}:`, error);
    throw error;
  }
};

// Gestione dei movimenti
const searchMovimenti = async (params) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/movimenti`, { params });
    return response.data;
  } catch (error) {
    console.error('Errore durante la ricerca dei movimenti:', error);
    throw error;
  }
};

const getMovimenti = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/movimenti`);
    return response.data;
  } catch (error) {
    console.error('Errore durante il recupero dei movimenti:', error);
    throw error;
  }
};

const getMovimento = async (id) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/movimenti/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Errore durante il recupero del movimento con ID ${id}:`, error);
    throw error;
  }
};

const createMovimento = async (movimento) => {
  try {
    console.log('Invio richiesta POST a /api/movimenti con dati:', movimento);
    const response = await axios.post(`${API_BASE_URL}/movimenti`, movimento);
    console.log('Risposta ricevuta:', response.data);
    return response.data;
  } catch (error) {
    console.error('Errore durante la creazione del movimento:', error);
    if (error.response) {
      console.error('Dettagli errore:', {
        status: error.response.status,
        data: error.response.data,
        headers: error.response.headers
      });
    }
    throw error;
  }
};

const updateMovimento = async (id, movimento) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/movimenti/${id}`, movimento);
    return response.data;
  } catch (error) {
    console.error(`Errore durante l'aggiornamento del movimento con ID ${id}:`, error);
    throw error;
  }
};

const deleteMovimento = async (id) => {
  try {
    const response = await axios.delete(`${API_BASE_URL}/movimenti/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Errore durante l'eliminazione del movimento con ID ${id}:`, error);
    throw error;
  }
};

// Gestione delle configurazioni
const getConfigurazioni = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/configurazioni`);
    return response.data;
  } catch (error) {
    console.error('Errore durante il recupero delle configurazioni:', error);
    throw error;
  }
};

const updateConfigurazioni = async (id, configurazione) => {
  try {
    // Il backend richiede una PUT su /api/configurazioni?id= con il body come ConfigurazioneRequestDto
    const response = await axios.put(`${API_BASE_URL}/configurazioni`, configurazione, { params: { id } });
    return response.data;
  } catch (error) {
    console.error("Errore durante l'aggiornamento delle configurazioni:", error);
    throw error;
  }
};

// Esporta tutte le funzioni
const apiService = {
  // Utenti
  getUtenti,
  getUtente,
  createUtente,
  updateUtente,
  deleteUtente,
  searchUtenti,
  
  // Veicoli
  getVeicoli,
  getVeicoliDisponibili,
  getVeicolo,
  createVeicolo,
  updateVeicolo,
  deleteVeicolo,
  searchVeicoli,
  
  // Movimenti
  getMovimenti,
  getMovimento,
  createMovimento,
  updateMovimento,
  deleteMovimento,
  searchMovimenti,
  
  // Configurazioni
  getConfigurazioni,
  updateConfigurazioni
};

export default apiService;

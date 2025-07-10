import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

const login = async (email, password) => {
  try {
    const response = await axios.post(`${API_URL}/login`, { email, password });
    console.log('Risposta login:', response.data);
    
    if (response.data.success) {
      localStorage.setItem('token', response.data.token);
      return response.data;
    } else {
      return {
        success: false,
        message: response.data.message || 'Errore durante il login'
      };
    }
  } catch (error) {
    console.error('Errore di login:', error);
    
    if (error.response) {
      // La richiesta è stata fatta e il server ha risposto con un codice di stato
      // che è fuori dal range 2xx
      return {
        success: false,
        message: error.response.data?.message || 'Credenziali non valide'
      };
    } else if (error.request) {
      // La richiesta è stata fatta ma non è stata ricevuta risposta
      return {
        success: false,
        message: 'Impossibile connettersi al server. Verifica la tua connessione.'
      };
    } else {
      // Qualcosa è andato storto nella configurazione della richiesta
      return {
        success: false,
        message: 'Errore durante la configurazione della richiesta'
      };
    }
  }
};

const authService = {
  login
};

export default authService;

import React, { createContext, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [message, setMessage] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    // Verifica se l'utente è già autenticato
    const storedUser = localStorage.getItem('user');
    const token = localStorage.getItem('token');
    if (storedUser && token) {
      setUser(JSON.parse(storedUser));
    }
    setLoading(false);
  }, []);

  const login = async (email, password) => {
    try {
      setLoading(true);
      setError(null);
      console.log('AuthContext: Tentativo di login con', email);
      const response = await authService.login(email, password);
      console.log('AuthContext: Risposta login', response);
      
      if (response.success) {
        const userData = {
          username: response.username
        };
        console.log('AuthContext: Login riuscito, impostazione utente', userData);
        setUser(userData);
        localStorage.setItem('user', JSON.stringify(userData));
        localStorage.setItem('token', response.token); // Aggiunto per gestire il token JWT
        setMessage('Login effettuato con successo');
        return true;
      } else {
        console.log('AuthContext: Login fallito', response.message);
        setError(response.message || 'Errore durante il login');
        return false;
      }
    } catch (err) {
      console.error('AuthContext: Errore durante il login', err);
      setError(err.message || 'Errore durante il login');
      return false;
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    setUser(null);
    setMessage('Logout effettuato con successo');
    navigate('/login');
  };

  const isAuthenticated = () => {
    return !!user;
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        loading,
        error,
        login,
        logout,
        isAuthenticated,
        message,
        setMessage
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

import React, { useContext } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { AuthProvider, AuthContext } from './context/AuthContext';
import SimpleSnackbar from './components/common/SimpleSnackbar';
import ProtectedRoute from './components/auth/ProtectedRoute';
import LoginPage from './components/auth/LoginPage';
import Dashboard from './components/dashboard/Dashboard';
import Layout from './components/common/Layout';
import UtentiPage from './components/utenti/UtentiPage';
import VeicoliPage from './components/veicoli/VeicoliPage';
import MovimentiPage from './components/movimenti/MovimentiPage';
import ConfigurazioniPage from './components/common/ConfigurazioniPage';
import './App.css';
import './styles/index.css';

// Creazione del tema personalizzato
const theme = createTheme({
  palette: {
    primary: {
      main: '#2196f3',
      light: '#64b5f6',
      dark: '#1976d2',
    },
    secondary: {
      main: '#f50057',
      light: '#ff4081',
      dark: '#c51162',
    },
    background: {
      default: '#f5f5f5',
      paper: '#ffffff',
    },
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    h1: {
      fontWeight: 700,
      letterSpacing: '0.5px',
    },
    h2: {
      fontWeight: 600,
      letterSpacing: '0.5px',
    },
    h3: {
      fontWeight: 600,
      letterSpacing: '0.5px',
    },
    h4: {
      fontWeight: 600,
      letterSpacing: '0.5px',
    },
    h5: {
      fontWeight: 500,
      letterSpacing: '0.3px',
    },
    h6: {
      fontWeight: 500,
      letterSpacing: '0.3px',
    },
  },
  shape: {
    borderRadius: 12,
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
          fontWeight: 500,
          padding: '8px 16px',
          borderRadius: 8,
          boxShadow: 'none',
          '&:hover': {
            boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
          },
        },
        contained: {
          boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
          '&:hover': {
            boxShadow: '0 4px 8px rgba(0,0,0,0.2)',
          },
        },
      },
    },
    MuiPaper: {
      styleOverrides: {
        root: {
          backgroundImage: 'none',
          borderRadius: 12,
          boxShadow: '0 4px 6px rgba(0,0,0,0.1)',
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 12,
          transition: 'transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out',
          '&:hover': {
            transform: 'translateY(-4px)',
            boxShadow: '0 8px 16px rgba(0,0,0,0.1)',
          },
        },
      },
    },
    MuiTableCell: {
      styleOverrides: {
        root: {
          padding: '16px',
          borderBottom: '1px solid rgba(224, 224, 224, 0.4)',
        },
        head: {
          backgroundColor: '#f5f5f5',
          fontWeight: 600,
        },
      },
    },
    MuiDialog: {
      styleOverrides: {
        paper: {
          borderRadius: 12,
        },
      },
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            borderRadius: 8,
          },
        },
      },
    },
  },
});

function App() {
  // Snackbar globale per messaggi di login/logout
  const GlobalSnackbar = () => {
    const { message, setMessage } = useContext(AuthContext) || {};
    return (
      <SimpleSnackbar
        open={!!message}
        message={message || ''}
        onClose={() => setMessage && setMessage(null)}
      />
    );
  };

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Router>
        <AuthProvider>
          <GlobalSnackbar />
          <Routes>
  <Route path="/login" element={<LoginPage />} />
  <Route element={<ProtectedRoute />}>
    <Route element={<Layout />}>
      <Route path="/" element={<Dashboard />} />
      <Route path="/utenti" element={<UtentiPage />} />
      <Route path="/veicoli" element={<VeicoliPage />} />
      <Route path="/movimenti" element={<MovimentiPage />} />
      <Route path="/configurazioni" element={<ConfigurazioniPage />} />
    </Route>
  </Route>
  <Route path="*" element={<Navigate to="/" replace />} />
</Routes>
        </AuthProvider>
      </Router>
    </ThemeProvider>
  );
}

export default App;

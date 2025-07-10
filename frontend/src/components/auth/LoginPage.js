import React, { useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  Container, 
  Box, 
  Typography, 
  TextField, 
  Button, 
  Paper, 
  Alert, 
  CircularProgress 
} from '@mui/material';
import { motion } from 'framer-motion';
import { AuthContext } from '../../context/AuthContext';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';

const LoginSchema = Yup.object().shape({
  email: Yup.string()
    .email('Email non valida')
    .required('Email obbligatoria'),
  password: Yup.string()
    .required('Password obbligatoria')
});

const LoginPage = () => {
  const { login, error, loading, isAuthenticated } = useContext(AuthContext);
  const navigate = useNavigate();
  const [loginError, setLoginError] = useState(null);

  useEffect(() => {
    // Se l'utente è già autenticato, reindirizza alla dashboard
    if (isAuthenticated()) {
      navigate('/');
    }
  }, [isAuthenticated, navigate]);

  const handleSubmit = async (values, { setSubmitting }) => {
    setLoginError(null);
    try {
      console.log('Tentativo di login con:', values.email, values.password);
      const success = await login(values.email, values.password);
      console.log('Risultato login:', success);
      if (success) {
        console.log('Login riuscito, reindirizzamento alla dashboard');
        navigate('/');
      } else {
        console.log('Login fallito');
        setLoginError('Credenziali non valide. Riprova.');
      }
    } catch (error) {
      console.error('Errore durante il login:', error);
      setLoginError('Errore durante la connessione al server. Riprova più tardi.');
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Container component="main" maxWidth="xs">
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
      >
        <Paper 
          elevation={6} 
          sx={{ 
            mt: 8, 
            p: 4, 
            display: 'flex', 
            flexDirection: 'column', 
            alignItems: 'center',
            borderRadius: 2,
            background: 'linear-gradient(to right bottom, #ffffff, #f8f9fa)'
          }}
        >
          <Typography 
            component="h1" 
            variant="h4" 
            sx={{ 
              mb: 3, 
              fontWeight: 'bold',
              color: '#1976d2'
            }}
          >
            Accesso Amministratore
          </Typography>
          
          {(error || loginError) && (
            <Alert severity="error" sx={{ width: '100%', mb: 2 }}>
              {error || loginError}
            </Alert>
          )}
          
          <Formik
            initialValues={{ email: '', password: '' }}
            validationSchema={LoginSchema}
            onSubmit={handleSubmit}
          >
            {({ isSubmitting, errors, touched }) => (
              <Form style={{ width: '100%' }}>
                <Box mb={3}>
                  <Field
                    as={TextField}
                    name="email"
                    label="Email"
                    variant="outlined"
                    fullWidth
                    error={errors.email && touched.email}
                    helperText={<ErrorMessage name="email" />}
                  />
                </Box>
                
                <Box mb={4}>
                  <Field
                    as={TextField}
                    name="password"
                    label="Password"
                    type="password"
                    variant="outlined"
                    fullWidth
                    error={errors.password && touched.password}
                    helperText={<ErrorMessage name="password" />}
                  />
                </Box>
                
                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  color="primary"
                  size="large"
                  disabled={isSubmitting || loading}
                  sx={{ 
                    py: 1.5,
                    borderRadius: 2,
                    fontWeight: 'bold',
                    boxShadow: '0 4px 10px rgba(25, 118, 210, 0.3)',
                    '&:hover': {
                      transform: 'translateY(-2px)',
                      boxShadow: '0 6px 15px rgba(25, 118, 210, 0.4)',
                    },
                    transition: 'all 0.3s ease'
                  }}
                >
                  {(isSubmitting || loading) ? (
                    <CircularProgress size={24} color="inherit" />
                  ) : (
                    'Accedi'
                  )}
                </Button>
              </Form>
            )}
          </Formik>
        </Paper>
      </motion.div>
    </Container>
  );
};

export default LoginPage;

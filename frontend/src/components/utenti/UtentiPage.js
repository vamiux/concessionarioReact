import React, { useState, useEffect } from 'react';
import { 
  Typography, 
  Paper, 
  Box, 
  TextField, 
  Dialog, 
  DialogTitle, 
  DialogContent, 
  DialogActions,
  Grid,
  Snackbar,
  Alert,
  CircularProgress,
  Card,
  CardContent,
  CardActions,
  Avatar,
  Chip
} from '@mui/material';
import CommonButton from '../common/CommonButton';
import { 
  Add as AddIcon, 
  Edit as EditIcon, 
  Search as SearchIcon,
  Clear as ClearIcon,
  Person as PersonIcon,
  Email as EmailIcon,
  Phone as PhoneIcon,
  LocationOn as LocationIcon,
  CalendarToday as CalendarTodayIcon
} from '@mui/icons-material';
import { motion, AnimatePresence } from 'framer-motion';
import { Formik, Form, Field } from 'formik';
import * as Yup from 'yup';
import apiService from '../../services/apiService';

const UtenteSchema = Yup.object().shape({
  nome: Yup.string()
    .required('Nome obbligatorio'),
  cognome: Yup.string()
    .required('Cognome obbligatorio'),
  codiceFiscaleUtente: Yup.string()
    .required('Codice fiscale obbligatorio')
    .length(16, 'Il codice fiscale deve essere di 16 caratteri'),
  email: Yup.string()
    .email('Email non valida')
    .required('Email obbligatoria'),
  telefono: Yup.string()
    .required('Telefono obbligatorio'),
  dataNascita: Yup.date()
    .nullable()
    .transform((curr, orig) => orig === '' ? null : curr)
    .required('Data di nascita obbligatoria'),
  indirizzo: Yup.string()
    .required('Indirizzo obbligatorio')
});

const UtentiPage = () => {
  const [utenti, setUtenti] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [openAddDialog, setOpenAddDialog] = useState(false);
  const [openEditDialog, setOpenEditDialog] = useState(false);
  const [openSearchDialog, setOpenSearchDialog] = useState(false);
  const [selectedUtente, setSelectedUtente] = useState(null);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: '',
    severity: 'success'
  });

  useEffect(() => {
    fetchUtenti();
  }, []);

  const fetchUtenti = async () => {
    try {
      setLoading(true);
      const data = await apiService.getUtenti();
      setUtenti(data);
      setError(null);
    } catch (err) {
      console.error('Errore durante il recupero degli utenti:', err);
      setError('Errore durante il recupero degli utenti. Riprova più tardi.');
    } finally {
      setLoading(false);
    }
  };

  const handleAddUtente = async (values, { resetForm }) => {
    try {
      // Converti la data in formato ISO
      const dataFormattata = values.dataNascita ? new Date(values.dataNascita).toISOString() : null;
      
      // Crea l'oggetto utente con i dati formattati
      const utenteData = {
        codiceFiscaleUtente: values.codiceFiscaleUtente.toUpperCase(),
        nome: values.nome.trim(),
        cognome: values.cognome.trim(),
        dataNascita: dataFormattata,
        telefono: values.telefono.trim(),
        email: values.email.trim(),
        indirizzo: values.indirizzo.trim()
      };
      
      // Log dei dati prima dell'invio
      console.log('Dati utente da inviare:', utenteData);

      const response = await apiService.createUtente(utenteData);
      console.log('Risposta dal server:', response);
      
      resetForm();
      setOpenAddDialog(false);
      fetchUtenti();
      setSnackbar({
        open: true,
        message: 'Utente aggiunto con successo',
        severity: 'success'
      });
    } catch (err) {
      console.error('Errore durante l\'aggiunta dell\'utente:', err);
      console.error('Dettagli errore:', {
        status: err.response?.status,
        statusText: err.response?.statusText,
        data: err.response?.data
      });
      setSnackbar({
        open: true,
        message: err.response?.data || 'Errore durante l\'aggiunta dell\'utente',
        severity: 'error'
      });
    }
  };

  // Funzione per aggiornare un utente
  const handleEditUtente = async (values) => {
    try {
      // Converti la data in formato ISO
      const dataFormattata = values.dataNascita ? new Date(values.dataNascita).toISOString() : null;
      const utenteData = {
        ...values,
        dataNascita: dataFormattata
      };
      await apiService.updateUtente(values.codiceFiscaleUtente, utenteData);
      setOpenEditDialog(false);
      fetchUtenti();
      setSnackbar({
        open: true,
        message: 'Utente aggiornato con successo',
        severity: 'success'
      });
    } catch (err) {
      console.error('Errore durante l\'aggiornamento dell\'utente:', err);
      setSnackbar({
        open: true,
        message: err.response?.data || 'Errore durante l\'aggiornamento dell\'utente',
        severity: 'error'
      });
    }
  };

  const handleSearch = async (values) => {
    try {
      setLoading(true);
      // Rimuovi i campi vuoti dall'oggetto di ricerca
      const searchParams = Object.fromEntries(
        Object.entries(values).filter(([_, v]) => v !== '')
      );
      
      const data = await apiService.searchUtenti(searchParams);
      setUtenti(data);
      setOpenSearchDialog(false);
    } catch (err) {
      console.error('Errore durante la ricerca degli utenti:', err);
      setSnackbar({
        open: true,
        message: 'Errore durante la ricerca degli utenti',
        severity: 'error'
      });
    } finally {
      setLoading(false);
    }
  };

  const handleCloseSnackbar = () => {
    setSnackbar(prev => ({ ...prev, open: false }));
  };

  const handleEditClick = (utente) => {
    setSelectedUtente(utente);
    setOpenEditDialog(true);
  };

  const resetSearch = () => {
    fetchUtenti();
  };

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1
      }
    }
  };

  const itemVariants = {
    hidden: { y: 20, opacity: 0 },
    visible: {
      y: 0,
      opacity: 1,
      transition: {
        duration: 0.5
      }
    }
  };

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      variants={containerVariants}
    >
      <Box mb={4}>
        <Typography 
          variant="h4" 
          component="h1" 
          gutterBottom 
          fontWeight="bold"
          sx={{
            background: 'linear-gradient(45deg, #2196f3 30%, #21CBF3 90%)',
            WebkitBackgroundClip: 'text',
            WebkitTextFillColor: 'transparent',
            mb: 2
          }}
        >
          Gestione Utenti
        </Typography>
      </Box>

      <Paper 
        elevation={3} 
        sx={{ 
          p: 2, 
          mb: 4, 
          borderRadius: 2,
          background: 'linear-gradient(145deg, #ffffff 0%, #f5f5f5 100%)'
        }}
      >
        <Grid container spacing={2} alignItems="center" sx={{ mb: 3 }}>
          <Grid item xs={12} md={6}>
            <CommonButton
              variant="contained"
              color="primary"
              startIcon={<AddIcon />}
              onClick={() => setOpenAddDialog(true)}
              sx={{ 
                mr: 2,
                background: 'linear-gradient(45deg, #2196f3 30%, #21CBF3 90%)',
                '&:hover': {
                  background: 'linear-gradient(45deg, #1976d2 30%, #1CB5E0 90%)',
                }
              }}
            >
              Nuovo Utente
            </CommonButton>
          </Grid>
          <Grid item xs={12} md={6} sx={{ textAlign: 'right' }}>
            <CommonButton
              variant="outlined"
              startIcon={<SearchIcon />}
              onClick={() => setOpenSearchDialog(true)}
              sx={{ 
                mr: 2,
                borderColor: '#2196f3',
                color: '#2196f3',
                '&:hover': {
                  borderColor: '#1976d2',
                  backgroundColor: 'rgba(33, 150, 243, 0.04)',
                }
              }}
            >
              Ricerca
            </CommonButton>
            <CommonButton
              variant="outlined"
              startIcon={<ClearIcon />}
              onClick={resetSearch}
              sx={{
                borderColor: '#f50057',
                color: '#f50057',
                '&:hover': {
                  borderColor: '#c51162',
                  backgroundColor: 'rgba(245, 0, 87, 0.04)',
                }
              }}
            >
              Reset
            </CommonButton>
          </Grid>
        </Grid>

        {loading ? (
          <Box display="flex" justifyContent="center" my={4}>
            <CircularProgress sx={{ color: '#2196f3' }} />
          </Box>
        ) : error ? (
          <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>
        ) : (
          <Grid container spacing={2} justifyContent="center">
            <AnimatePresence>
              {utenti.map((utente) => (
                <Grid item xs={12} sm={6} md={4} lg={4} key={utente.codiceFiscaleUtente}>
                  <motion.div
                    variants={itemVariants}
                    initial="hidden"
                    animate="visible"
                    exit="hidden"
                  >
                    <Card 
                      sx={{ 
                        height: '100%',
                        display: 'flex',
                        flexDirection: 'column',
                        transition: 'all 0.3s ease-in-out',
                        '&:hover': {
                          transform: 'translateY(-8px)',
                          boxShadow: '0 12px 24px rgba(0,0,0,0.15)',
                        }
                      }}
                    >
                      <CardContent sx={{ flexGrow: 1, p: 2 }}>
                        <Box display="flex" alignItems="center" mb={2}>
  <Avatar 
    sx={{ 
      bgcolor: '#2196f3',
      mr: 2,
      width: 56,
      height: 56
    }}
  >
    <PersonIcon />
  </Avatar>
  <Box>
    <Typography variant="h6" component="h2" gutterBottom fontWeight="500">
      {utente.nome} {utente.cognome}
    </Typography>
    <Chip 
      label={`CF: ${utente.codiceFiscaleUtente}`}
      size="small"
      sx={{ 
        bgcolor: 'rgba(33, 150, 243, 0.1)',
        color: '#2196f3',
        fontWeight: 500,
        fontSize: '0.85rem',
        mb: 0.5
      }}
    />
    <Box display="flex" alignItems="center" mt={0.5}>
      <CalendarTodayIcon sx={{ mr: 1, color: '#2196f3', fontSize: '1.1rem' }} />
      <Typography variant="body2" color="text.secondary" sx={{ fontSize: '0.9rem' }}>
        {utente.dataNascita ? new Date(utente.dataNascita).toLocaleDateString() : '-'}
      </Typography>
    </Box>
  </Box>
</Box>

<Box display="flex" alignItems="center" mb={1.5}>
  <EmailIcon sx={{ mr: 1, color: '#2196f3', fontSize: '1.1rem' }} />
  <Typography variant="body2" color="text.primary" sx={{ wordBreak: 'break-word', fontSize: '0.9rem' }}>
    {utente.email}
  </Typography>
</Box>

<Box display="flex" alignItems="center" mb={1.5}>
  <PhoneIcon sx={{ mr: 1, color: '#2196f3', fontSize: '1.1rem' }} />
  <Typography variant="body2" color="text.primary" sx={{ fontSize: '0.9rem' }}>
    {utente.telefono}
  </Typography>
</Box>

<Box display="flex" alignItems="flex-start" mb={1.5}>
  <LocationIcon sx={{ mr: 1, color: '#2196f3', fontSize: '1.1rem', mt: 0.2 }} />
  <Typography variant="body2" color="text.primary" sx={{ wordBreak: 'break-word', fontSize: '0.9rem' }}>
    {utente.indirizzo}
  </Typography>
</Box>
                      </CardContent>
                      
                      <CardActions sx={{ p: 2, pt: 0 }}>
                        <CommonButton
                          size="small"
                          startIcon={<EditIcon sx={{ fontSize: '0.9rem' }} />}
                          onClick={() => handleEditClick(utente)}
                          sx={{
                            color: '#2196f3',
                            fontSize: '0.75rem',
                            py: 0.5,
                            px: 1.5,
                            minWidth: '80px',
                            '&:hover': {
                              backgroundColor: 'rgba(33, 150, 243, 0.04)',
                            }
                          }}
                        >
                          Modifica
                        </CommonButton>
                      </CardActions>
                    </Card>
                  </motion.div>
                </Grid>
              ))}
            </AnimatePresence>
          </Grid>
        )}
      </Paper>

      {/* Dialog per aggiungere un nuovo utente */}
      <Dialog 
        open={openAddDialog} 
        onClose={() => setOpenAddDialog(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>Nuovo Utente</DialogTitle>
        <Formik
          initialValues={{
            nome: '',
            cognome: '',
            codiceFiscaleUtente: '',
            email: '',
            telefono: '',
            dataNascita: new Date().toISOString().split('T')[0],
            indirizzo: ''
          }}
          validationSchema={UtenteSchema}
          onSubmit={handleAddUtente}
        >
          {({ errors, touched, isSubmitting }) => (
            <Form>
              <DialogContent>
                <Grid container spacing={2}>
                  <Grid item xs={12} md={6}>
                    <Field
                      as={TextField}
                      name="nome"
                      label="Nome"
                      fullWidth
                      margin="normal"
                      error={errors.nome && touched.nome}
                      helperText={touched.nome && errors.nome}
                    />
                  </Grid>
                  <Grid item xs={12} md={6}>
                    <Field
                      as={TextField}
                      name="cognome"
                      label="Cognome"
                      fullWidth
                      margin="normal"
                      error={errors.cognome && touched.cognome}
                      helperText={touched.cognome && errors.cognome}
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <Field
                      as={TextField}
                      name="codiceFiscaleUtente"
                      label="Codice Fiscale"
                      fullWidth
                      margin="normal"
                      error={errors.codiceFiscaleUtente && touched.codiceFiscaleUtente}
                      helperText={touched.codiceFiscaleUtente && errors.codiceFiscaleUtente}
                    />
                  </Grid>
                  <Grid item xs={12} md={6}>
                    <Field
                      as={TextField}
                      name="email"
                      label="Email"
                      fullWidth
                      margin="normal"
                      error={errors.email && touched.email}
                      helperText={touched.email && errors.email}
                    />
                  </Grid>
                  <Grid item xs={12} md={6}>
                    <Field
                      as={TextField}
                      name="telefono"
                      label="Telefono"
                      fullWidth
                      margin="normal"
                      error={errors.telefono && touched.telefono}
                      helperText={touched.telefono && errors.telefono}
                    />
                  </Grid>
                  <Grid item xs={12} md={6}>
                    <Field
                      as={TextField}
                      name="dataNascita"
                      label="Data di Nascita"
                      fullWidth
                      margin="normal"
                      type="date"
                      InputLabelProps={{ shrink: true }}
                      error={errors.dataNascita && touched.dataNascita}
                      helperText={touched.dataNascita && errors.dataNascita}
                    />
                  </Grid>
                  <Grid item xs={12} md={6}>
                    <Field
                      as={TextField}
                      name="indirizzo"
                      label="Indirizzo"
                      fullWidth
                      margin="normal"
                      error={errors.indirizzo && touched.indirizzo}
                      helperText={touched.indirizzo && errors.indirizzo}
                    />
                  </Grid>
                </Grid>
              </DialogContent>
              <DialogActions>
                <CommonButton onClick={() => setOpenAddDialog(false)}>Annulla</CommonButton>
                <CommonButton 
                  type="submit" 
                  variant="contained" 
                  color="primary"
                  disabled={isSubmitting}
                >
                  {isSubmitting ? <CircularProgress size={24} /> : 'Salva'}
                </CommonButton>
              </DialogActions>
            </Form>
          )}
        </Formik>
      </Dialog>

      {/* Dialog per modificare un utente */}
      <Dialog 
        open={openEditDialog} 
        onClose={() => setOpenEditDialog(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>Modifica Utente</DialogTitle>
        {selectedUtente && (
          <Formik
            initialValues={{
  nome: selectedUtente.nome,
  cognome: selectedUtente.cognome,
  codiceFiscaleUtente: selectedUtente.codiceFiscaleUtente,
  telefono: selectedUtente.telefono || '',
  email: selectedUtente.email || '',
  indirizzo: selectedUtente.indirizzo || '',
  dataNascita: selectedUtente.dataNascita ? selectedUtente.dataNascita.substring(0, 10) : ''
}}
            validationSchema={UtenteSchema}
            onSubmit={handleEditUtente}
          >
            {({ errors, touched, isSubmitting }) => (
              <Form>
                <DialogContent>
                  <Grid container spacing={2}>
                    <Grid item xs={12} md={6}>
  <Field
    as={TextField}
    name="nome"
    label="Nome"
    fullWidth
    margin="normal"
    disabled
    error={errors.nome && touched.nome}
    helperText={touched.nome && errors.nome}
  />
</Grid>
                    <Grid item xs={12} md={6}>
  <Field
    as={TextField}
    name="cognome"
    label="Cognome"
    fullWidth
    margin="normal"
    disabled
    error={errors.cognome && touched.cognome}
    helperText={touched.cognome && errors.cognome}
  />
</Grid>
                    <Grid item xs={12}>
  <Field
    as={TextField}
    name="codiceFiscaleUtente"
    label="Codice Fiscale"
    fullWidth
    margin="normal"
    disabled
    error={errors.codiceFiscaleUtente && touched.codiceFiscaleUtente}
    helperText={touched.codiceFiscaleUtente && errors.codiceFiscaleUtente}
  />
</Grid>
                    <Grid item xs={12} md={6}>
                      <Field
                        as={TextField}
                        name="email"
                        label="Email"
                        fullWidth
                        margin="normal"
                        error={errors.email && touched.email}
                        helperText={touched.email && errors.email}
                      />
                    </Grid>
                    <Grid item xs={12} md={6}>
                      <Field
                        as={TextField}
                        name="telefono"
                        label="Telefono"
                        fullWidth
                        margin="normal"
                        error={errors.telefono && touched.telefono}
                        helperText={touched.telefono && errors.telefono}
                      />
                    </Grid>
                    <Grid item xs={12} md={6}>
  <Field
    as={TextField}
    name="dataNascita"
    label="Data di Nascita"
    fullWidth
    margin="normal"
    type="date"
    InputLabelProps={{ shrink: true }}
    disabled
    error={errors.dataNascita && touched.dataNascita}
    helperText={touched.dataNascita && errors.dataNascita}
  />
</Grid>
                    <Grid item xs={12} md={6}>
                      <Field
                        as={TextField}
                        name="indirizzo"
                        label="Indirizzo"
                        fullWidth
                        margin="normal"
                        error={errors.indirizzo && touched.indirizzo}
                        helperText={touched.indirizzo && errors.indirizzo}
                      />
                    </Grid>
                  </Grid>
                </DialogContent>
                <DialogActions>
                  <CommonButton onClick={() => setOpenEditDialog(false)}>Annulla</CommonButton>
                  <CommonButton 
                    type="submit" 
                    variant="contained" 
                    color="primary"
                    disabled={isSubmitting}
                  >
                    {isSubmitting ? <CircularProgress size={24} /> : 'Salva'}
                  </CommonButton>
                </DialogActions>
              </Form>
            )}
          </Formik>
        )}
      </Dialog>

      {/* Dialog per la ricerca */}
      <Dialog 
        open={openSearchDialog} 
        onClose={() => setOpenSearchDialog(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>Ricerca Utenti</DialogTitle>
        <Formik
          initialValues={{
            nome: '',
            cognome: '',
            email: ''
          }}
          onSubmit={handleSearch}
        >
          {({ isSubmitting }) => (
            <Form>
              <DialogContent>
                <Grid container spacing={2}>
                  <Grid item xs={12} md={6}>
                    <Field
                      as={TextField}
                      name="nome"
                      label="Nome"
                      fullWidth
                      margin="normal"
                    />
                  </Grid>
                  <Grid item xs={12} md={6}>
                    <Field
                      as={TextField}
                      name="cognome"
                      label="Cognome"
                      fullWidth
                      margin="normal"
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <Field
                      as={TextField}
                      name="email"
                      label="Email"
                      fullWidth
                      margin="normal"
                    />
                  </Grid>
                </Grid>
              </DialogContent>
              <DialogActions>
                <CommonButton onClick={() => setOpenSearchDialog(false)}>Annulla</CommonButton>
                <CommonButton 
                  type="submit" 
                  variant="contained" 
                  color="primary"
                  disabled={isSubmitting}
                  startIcon={<SearchIcon />}
                >
                  {isSubmitting ? <CircularProgress size={24} /> : 'Cerca'}
                </CommonButton>
              </DialogActions>
            </Form>
          )}
        </Formik>
      </Dialog>

      <Snackbar 
        open={snackbar.open} 
        autoHideDuration={6000} 
        onClose={handleCloseSnackbar}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        <Alert 
          onClose={handleCloseSnackbar} 
          severity={snackbar.severity}
          sx={{ width: '100%' }}
        >
          {snackbar.message}
        </Alert>
      </Snackbar>
    </motion.div>
  );
};

export default UtentiPage;

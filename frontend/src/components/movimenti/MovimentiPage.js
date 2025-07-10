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
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
  Grid,
  Snackbar,
  Alert,
  CircularProgress,
  MenuItem,
  FormControl,
  InputLabel,
  Select,
  Chip,
  Autocomplete,
  Checkbox,
  FormControlLabel
} from '@mui/material';
import CommonButton from '../common/CommonButton';
import { 
  Add as AddIcon, 
  Search as SearchIcon,
  Clear as ClearIcon,
  Visibility as ViewIcon,
  Settings as SettingsIcon
} from '@mui/icons-material';
import { motion } from 'framer-motion';
import { Formik, Form } from 'formik';
import * as Yup from 'yup';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { it } from 'date-fns/locale';
import apiService from '../../services/apiService';
import { useNavigate } from 'react-router-dom';

const MovimentoSchema = Yup.object().shape({
  tipoMovimento: Yup.string()
    .required('Tipo movimento obbligatorio'),
  dataMovimento: Yup.date()
    .nullable()
    .transform((curr, orig) => orig === '' ? null : curr)
    .required('Data movimento obbligatoria'),
  importo: Yup.number()
    .typeError('Importo deve essere un numero')
    .required('Importo obbligatorio')
    .positive('Importo deve essere positivo'),
  codiceFiscaleUtente: Yup.string()
    .required('Cliente obbligatorio'),
  numeroTelaio: Yup.string()
    .required('Veicolo obbligatorio'),
  note: Yup.string(),
  hasComproprietario: Yup.boolean(),
  codiceFiscaleComproprietario: Yup.string()
    .test('comproprietario-required', 'Comproprietario obbligatorio', function(value) {
      return !this.parent.hasComproprietario || (value && value.length > 0);
    })
    .test('comproprietario-different', 'Il comproprietario deve essere diverso dal proprietario principale', function(value) {
      return !this.parent.hasComproprietario || value !== this.parent.codiceFiscaleUtente;
    })
});

const MovimentiPage = () => {
  const navigate = useNavigate();
  const [movimenti, setMovimenti] = useState([]);
  const [utenti, setUtenti] = useState([]);
  const [veicoli, setVeicoli] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [openAddDialog, setOpenAddDialog] = useState(false);
  const [openViewDialog, setOpenViewDialog] = useState(false);
  const [openSearchDialog, setOpenSearchDialog] = useState(false);
  const [selectedMovimento, setSelectedMovimento] = useState(null);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: '',
    severity: 'success'
  });

  useEffect(() => {
    fetchMovimenti();
    fetchUtenti();
    fetchVeicoli();
  }, []);

  const fetchMovimenti = async () => {
    try {
      setLoading(true);
      const data = await apiService.getMovimenti();
      setMovimenti(data);
      setError(null);
    } catch (err) {
      console.error('Errore durante il recupero dei movimenti:', err);
      setError('Errore durante il recupero dei movimenti. Riprova più tardi.');
    } finally {
      setLoading(false);
    }
  };

  const fetchUtenti = async () => {
    try {
      const data = await apiService.getUtenti();
      setUtenti(data);
    } catch (err) {
      console.error('Errore durante il recupero degli utenti:', err);
    }
  };

  const fetchVeicoli = async () => {
    try {
      const data = await apiService.getVeicoli();
      setVeicoli(data);
    } catch (err) {
      console.error('Errore durante il recupero dei veicoli:', err);
      setVeicoli([]);
    }
  };

  const handleAddMovimento = async (values, { resetForm }) => {
    try {
      console.log('Valori del form:', values); // Log dei valori del form
      
      // Converti la data in formato ISO
      const dataFormattata = values.dataMovimento ? new Date(values.dataMovimento).toISOString() : null;
      
      // Crea l'oggetto movimento con i dati nel formato corretto
      const movimentoData = {
        codiceFiscaleUtente: values.codiceFiscaleUtente,
        numeroTelaio: values.numeroTelaio,
        dataMovimento: dataFormattata,
        tipoMovimento: values.tipoMovimento,
        prezzo: parseFloat(values.importo),
        note: values.note || null,
        hasComproprietario: values.hasComproprietario || false,
        codiceFiscaleComproprietario: values.hasComproprietario ? values.codiceFiscaleComproprietario : null
      };

      console.log('Dati movimento da inviare:', movimentoData); // Log dei dati formattati
      console.log('Chiamata API createMovimento...'); // Log prima della chiamata API
      
      // Verifica che tutti i campi obbligatori siano presenti
      if (!movimentoData.codiceFiscaleUtente || !movimentoData.numeroTelaio || !movimentoData.dataMovimento || !movimentoData.tipoMovimento || !movimentoData.prezzo) {
        throw new Error('Campi obbligatori mancanti');
      }

      const response = await apiService.createMovimento(movimentoData);
      console.log('Risposta API:', response); // Log della risposta
      
      if (!response) {
        throw new Error('Nessuna risposta dal server');
      }
      
      resetForm();
      setOpenAddDialog(false);
      fetchMovimenti();
      setSnackbar({
        open: true,
        message: 'Movimento aggiunto con successo',
        severity: 'success'
      });
    } catch (err) {
      console.error('Errore durante l\'aggiunta del movimento:', err);
      console.error('Dettagli errore:', {
        status: err.response?.status,
        statusText: err.response?.statusText,
        data: err.response?.data,
        message: err.message
      });
      setSnackbar({
        open: true,
        message: err.response?.data || err.message || 'Errore durante l\'aggiunta del movimento',
        severity: 'error'
      });
    }
  };

  const handleSearch = async (values) => {
    console.log('Valori del form di ricerca:', values);
    const params = {};
    if (values.codiceFiscaleUtente) {
      params.codiceFiscale = values.codiceFiscaleUtente;
    }
    if (values.numeroTelaio) {
      params.numeroTelaio = values.numeroTelaio;
    }
    console.log('Parametri di ricerca:', params);

    try {
      setLoading(true);
      if (Object.keys(params).length > 0) {
        const data = await apiService.searchMovimenti(params);
        setMovimenti(data);
      } else {
        await fetchMovimenti();
      }
      setOpenSearchDialog(false);
    } catch (err) {
      console.error('Errore durante la ricerca dei movimenti:', err);
      setSnackbar({
        open: true,
        message: 'Errore durante la ricerca dei movimenti',
        severity: 'error'
      });
    } finally {
      setLoading(false);
    }
  };

  const handleCloseSnackbar = () => {
    setSnackbar(prev => ({ ...prev, open: false }));
  };

  const handleViewClick = (movimento) => {
    setSelectedMovimento(movimento);
    setOpenViewDialog(true);
  };

  const resetSearch = () => {
    fetchMovimenti();
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('it-IT', {
      style: 'currency',
      currency: 'EUR'
    }).format(amount);
  };

  const formatDate = (dateString) => {
    const options = { day: '2-digit', month: '2-digit', year: 'numeric' };
    return new Date(dateString).toLocaleDateString('it-IT', options);
  };

  // Le funzioni di utilità sono state rimosse perché non utilizzate


  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
    >
      <Box mb={4}>
        <Typography variant="h4" component="h1" gutterBottom fontWeight="bold">
          Gestione Movimenti
        </Typography>
      </Box>

      <Paper elevation={3} sx={{ p: 3, mb: 4, borderRadius: 2 }}>
        <Grid container spacing={2} alignItems="center" sx={{ mb: 3 }}>
          <Grid item xs={12} md={6}>
            <CommonButton
              variant="contained"
              color="primary"
              startIcon={<AddIcon />}
              onClick={() => setOpenAddDialog(true)}
              sx={{ mr: 2 }}
            >
              Nuovo Movimento
            </CommonButton>
          </Grid>
          <Grid item xs={12} md={6} sx={{ textAlign: 'right' }}>
             <CommonButton
               variant="outlined"
               startIcon={<SearchIcon />}
               onClick={() => setOpenSearchDialog(true)}
               sx={{ mr: 2 }}
             >
               Ricerca
             </CommonButton>
             <CommonButton
               variant="outlined"
               color="secondary"
               startIcon={<SettingsIcon />}
               onClick={() => navigate('/configurazioni')}
               sx={{ mr: 2 }}
             >
               Configurazioni
             </CommonButton>
            <CommonButton
              variant="outlined"
              startIcon={<ClearIcon />}
              onClick={resetSearch}
            >
              Reset
            </CommonButton>
          </Grid>
        </Grid>

        {loading ? (
          <Box display="flex" justifyContent="center" my={4}>
            <CircularProgress />
          </Box>
        ) : error ? (
          <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>
        ) : (
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>ID</TableCell>
                  <TableCell>Tipo</TableCell>
                  <TableCell>Data</TableCell>
                  <TableCell>Cliente</TableCell>
                  <TableCell>Comproprietario</TableCell>
                  <TableCell>Veicolo</TableCell>
                  <TableCell>Importo</TableCell>
                  <TableCell>Azioni</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {movimenti.length > 0 ? (
                  movimenti.map((movimento) => (
                    <TableRow key={movimento.idMovimento}>
                      <TableCell>{movimento.idMovimento}</TableCell>
                      <TableCell>
                        <Chip 
                          label={movimento.tipoMovimento === 'ACQUISTO' ? 'Acquisto' : 'Vendita'} 
                          color={movimento.tipoMovimento === 'ACQUISTO' ? 'primary' : 'success'}
                          size="small"
                        />
                      </TableCell>
                      <TableCell>{formatDate(movimento.dataMovimento)}</TableCell>
                      <TableCell>
                        <span style={{fontWeight: 500}}>{movimento.nomeUtente} {movimento.cognomeUtente}</span>
                        <br />
                        <span style={{color: '#888', fontSize: '0.85em'}}>{movimento.codiceFiscaleUtente}</span>
                      </TableCell>
                      <TableCell>
                        {movimento.codiceFiscaleComproprietario ? (
                          <>
                            <span style={{fontWeight: 500}}>{movimento.nomeComproprietario} {movimento.cognomeComproprietario}</span>
                            <br />
                            <span style={{color: '#888', fontSize: '0.85em'}}>{movimento.codiceFiscaleComproprietario}</span>
                          </>
                        ) : (
                          <span>-</span>
                        )}
                      </TableCell>
                      <TableCell>
                        <span style={{fontWeight: 500}}>{movimento.marcaVeicolo} {movimento.modelloVeicolo}</span>
                        <br />
                        <span style={{color: '#888', fontSize: '0.85em'}}>{movimento.numeroTelaio}</span>
                      </TableCell>
                      <TableCell>{formatCurrency(movimento.prezzo)}</TableCell>
                      <TableCell>
                        <IconButton 
                          color="primary" 
                          onClick={() => handleViewClick(movimento)}
                          size="small"
                        >
                          <ViewIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))
                ) : (
                  <TableRow>
                    <TableCell colSpan={7} align="center">
                      Nessun movimento trovato
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </Paper>

      {/* Dialog per l'aggiunta */}
      <Dialog 
        open={openAddDialog} 
        onClose={() => setOpenAddDialog(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>Nuovo Movimento</DialogTitle>
        <Formik
          initialValues={{
            tipoMovimento: 'VENDITA',
            dataMovimento: new Date(),
            codiceFiscaleUtente: '',
            numeroTelaio: '',
            importo: '',
            hasComproprietario: false,
            codiceFiscaleComproprietario: ''
          }}
          validationSchema={MovimentoSchema}
          onSubmit={handleAddMovimento}
        >
          {({ errors, touched, isSubmitting, values, handleChange, setFieldValue }) => (
            <Form>
              <DialogContent>
                <Box sx={{ mb: 2 }}>
                  <FormControl fullWidth>
                    <InputLabel id="tipo-movimento-label">Tipo Movimento</InputLabel>
                    <Select
                      labelId="tipo-movimento-label"
                      id="tipoMovimento"
                      name="tipoMovimento"
                      value={values.tipoMovimento}
                      onChange={handleChange}
                      label="Tipo Movimento"
                    >
                      <MenuItem value="VENDITA">Vendita</MenuItem>
                      <MenuItem value="ACQUISTO">Acquisto</MenuItem>
                    </Select>
                  </FormControl>
                </Box>
                
                <Box sx={{ mb: 2 }}>
                  <LocalizationProvider dateAdapter={AdapterDateFns} adapterLocale={it}>
                    <DatePicker
                      label="Data Movimento"
                      value={values.dataMovimento}
                      onChange={(date) => setFieldValue('dataMovimento', date)}
                      renderInput={(params) => (
                        <TextField 
                          {...params} 
                          fullWidth
                          error={errors.dataMovimento && touched.dataMovimento}
                          helperText={touched.dataMovimento && errors.dataMovimento}
                        />
                      )}
                    />
                  </LocalizationProvider>
                </Box>
                
                <Box sx={{ mb: 2 }}>
                  <Autocomplete
                    options={utenti}
                    getOptionLabel={(option) => {
                      if (!option) return '';
                      return `${option.nome} ${option.cognome} (${option.codiceFiscaleUtente})`;
                    }}
                    value={utenti.find((u) => u.codiceFiscaleUtente === values.codiceFiscaleUtente) || null}
                    onChange={(_, value) => {
                      setFieldValue('codiceFiscaleUtente', value ? value.codiceFiscaleUtente : '');
                    }}
                    renderInput={(params) => (
                      <TextField
                        {...params}
                        label="Cliente"
                        fullWidth
                        error={errors.codiceFiscaleUtente && touched.codiceFiscaleUtente}
                        helperText={touched.codiceFiscaleUtente && errors.codiceFiscaleUtente}
                      />
                    )}
                  />
                </Box>
                
                <Box sx={{ mb: 2 }}>
                  <FormControlLabel
                    control={
                      <Checkbox
                        name="hasComproprietario"
                        checked={values.hasComproprietario}
                        onChange={handleChange}
                        color="primary"
                      />
                    }
                    label="Aggiungi comproprietario"
                  />
                </Box>
                
                {values.hasComproprietario && (
                  <Box sx={{ mb: 2 }}>
                    <Autocomplete
                      options={utenti.filter(u => u.codiceFiscaleUtente !== values.codiceFiscaleUtente)}
                      getOptionLabel={(option) => {
                        if (!option) return '';
                        return `${option.nome} ${option.cognome} (${option.codiceFiscaleUtente})`;
                      }}
                      value={utenti.find((u) => u.codiceFiscaleUtente === values.codiceFiscaleComproprietario) || null}
                      onChange={(_, value) => {
                        setFieldValue('codiceFiscaleComproprietario', value ? value.codiceFiscaleUtente : '');
                      }}
                      renderInput={(params) => (
                        <TextField
                          {...params}
                          label="Comproprietario"
                          fullWidth
                          error={errors.codiceFiscaleComproprietario && touched.codiceFiscaleComproprietario}
                          helperText={touched.codiceFiscaleComproprietario && errors.codiceFiscaleComproprietario}
                        />
                      )}
                    />
                  </Box>
                )}
                
                <Box sx={{ mb: 2 }}>
                  <Autocomplete
                    options={veicoli}
                    getOptionLabel={(option) => `${option.marca} ${option.modello} (${option.numeroTelaio})`}
                    value={veicoli.find((v) => v.numeroTelaio === values.numeroTelaio) || null}
                    onChange={(_, value) => {
                      setFieldValue('numeroTelaio', value ? value.numeroTelaio : '');
                    }}
                    renderInput={(params) => (
                      <TextField
                        {...params}
                        label="Veicolo"
                        fullWidth
                        error={errors.numeroTelaio && touched.numeroTelaio}
                        helperText={touched.numeroTelaio && errors.numeroTelaio}
                      />
                    )}
                  />
                </Box>
                
                <Box sx={{ mb: 2 }}>
                  <TextField
                    fullWidth
                    label="Importo"
                    name="importo"
                    type="number"
                    value={values.importo}
                    onChange={handleChange}
                    error={errors.importo && touched.importo}
                    helperText={touched.importo && errors.importo}
                  />
                </Box>
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

      {/* Dialog per visualizzare un movimento */}
      <Dialog 
        open={openViewDialog} 
        onClose={() => setOpenViewDialog(false)}
        maxWidth="md"
        fullWidth
      >
        <DialogTitle>Dettaglio Movimento</DialogTitle>
        {selectedMovimento && (
          <DialogContent>
            <Grid container spacing={2}>
              <Grid item xs={12} md={6}>
                <Typography variant="subtitle1" fontWeight="bold">ID:</Typography>
                <Typography variant="body1" gutterBottom>{selectedMovimento.id}</Typography>
              </Grid>
              <Grid item xs={12} md={6}>
                <Typography variant="subtitle1" fontWeight="bold">Tipo Movimento:</Typography>
                <Typography variant="body1" gutterBottom>
                  <Chip 
                    label={selectedMovimento.tipoMovimento === 'ACQUISTO' ? 'Acquisto' : 'Vendita'} 
                    color={selectedMovimento.tipoMovimento === 'ACQUISTO' ? 'primary' : 'success'}
                    size="small"
                  />
                </Typography>
              </Grid>
              <Grid item xs={12} md={6}>
                <Typography variant="subtitle1" fontWeight="bold">Data:</Typography>
                <Typography variant="body1" gutterBottom>{formatDate(selectedMovimento.dataMovimento)}</Typography>
              </Grid>
              <Grid item xs={12} md={6}>
                <Typography variant="subtitle1" fontWeight="bold">Importo:</Typography>
                <Typography variant="body1" gutterBottom>{formatCurrency(selectedMovimento.prezzo)}</Typography>
              </Grid>
              <Grid item xs={12} md={6}>
                <Typography variant="subtitle1" fontWeight="bold">Cliente:</Typography>
                <Typography variant="body1" gutterBottom>
                  {selectedMovimento.nomeUtente} {selectedMovimento.cognomeUtente}
                  <br />
                  <span style={{color: '#888', fontSize: '0.85em'}}>{selectedMovimento.codiceFiscaleUtente}</span>
                </Typography>
              </Grid>
              {selectedMovimento.codiceFiscaleComproprietario && (
                <Grid item xs={12} md={6}>
                  <Typography variant="subtitle1" fontWeight="bold">Comproprietario:</Typography>
                  <Typography variant="body1" gutterBottom>
                    {selectedMovimento.nomeComproprietario} {selectedMovimento.cognomeComproprietario}
                    <br />
                    <span style={{color: '#888', fontSize: '0.85em'}}>{selectedMovimento.codiceFiscaleComproprietario}</span>
                  </Typography>
                </Grid>
              )}
              <Grid item xs={12} md={6}>
                <Typography variant="subtitle1" fontWeight="bold">Veicolo:</Typography>
                <Typography variant="body1" gutterBottom>
                  {selectedMovimento.marcaVeicolo} {selectedMovimento.modelloVeicolo}
                  <br />
                  <span style={{color: '#888', fontSize: '0.85em'}}>{selectedMovimento.numeroTelaio}</span>
                </Typography>
              </Grid>
            </Grid>
          </DialogContent>
        )}
        <DialogActions>
          <CommonButton onClick={() => setOpenViewDialog(false)}>Chiudi</CommonButton>
        </DialogActions>
      </Dialog>

      {/* Dialog per la ricerca */}
      <Dialog 
        open={openSearchDialog} 
        onClose={() => setOpenSearchDialog(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>Ricerca Movimenti</DialogTitle>
        <Formik
          initialValues={{
            codiceFiscaleUtente: '',
            numeroTelaio: ''
          }}
          onSubmit={handleSearch}
        >
          {({ isSubmitting, values, handleChange, setFieldValue }) => (
            <Form>
              <DialogContent sx={{ overflowY: 'visible', py: 3, px: 2 }}>
                <Grid container spacing={3}>
                  <Grid item xs={12} md={6} sx={{ minWidth: 250, maxWidth: '100%' }}>
                    <Autocomplete
                      options={utenti}
                      getOptionLabel={(option) => {
                        if (!option) return '';
                        const nomeCompleto = `${option.nome} ${option.cognome}`;
                        return option.codiceFiscale ? `${nomeCompleto} (${option.codiceFiscale})` : nomeCompleto;
                      }}
                      onChange={(_, value) => {
                        setFieldValue('codiceFiscaleUtente', value ? value.codiceFiscale : '');
                      }}
                      renderInput={(params) => (
                        <TextField
                          {...params}
                          label="Cliente"
                          margin="normal"
                          fullWidth
                          sx={{ minWidth: 220 }}
                        />
                      )}
                      fullWidth
                      sx={{ minWidth: 220 }}
                      ListboxProps={{ style: { maxHeight: 300 } }}
                    />
                  </Grid>
                  <Grid item xs={12} md={6} sx={{ minWidth: 250, maxWidth: '100%' }}>
                    <Autocomplete
                      options={veicoli}
                      getOptionLabel={(option) => `${option.marca} ${option.modello} (${option.numeroTelaio})`}
                      onChange={(_, value) => {
                        setFieldValue('numeroTelaio', value ? value.numeroTelaio : '');
                      }}
                      renderInput={(params) => (
                        <TextField
                          {...params}
                          label="Veicolo"
                          margin="normal"
                          fullWidth
                          sx={{ minWidth: 220 }}
                        />
                      )}
                      fullWidth
                      sx={{ minWidth: 220 }}
                      ListboxProps={{ style: { maxHeight: 300 } }}
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

export default MovimentiPage;

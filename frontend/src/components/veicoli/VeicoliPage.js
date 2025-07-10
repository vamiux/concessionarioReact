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
  Chip
} from '@mui/material';
import CommonButton from '../common/CommonButton';
import { 
  Add as AddIcon, 
  Edit as EditIcon, 
  Search as SearchIcon,
  Clear as ClearIcon,

} from '@mui/icons-material';
import { motion } from 'framer-motion';
import { Formik, Form, Field } from 'formik';
import * as Yup from 'yup';
import apiService from '../../services/apiService';

const VeicoloSchema = Yup.object().shape({
  numeroTelaio: Yup.string()
    .required('Numero telaio obbligatorio'),
  marca: Yup.string()
    .required('Marca obbligatoria'),
  modello: Yup.string()
    .required('Modello obbligatorio'),

  annoImmatricolazione: Yup.number()
    .required('Anno immatricolazione obbligatorio')
    .min(1900, 'Anno non valido')
    .max(new Date().getFullYear(), 'Anno non può essere nel futuro'),

  disponibile: Yup.boolean()
    .required('Disponibilità obbligatoria')
});

const VeicoliPage = () => {
  const [veicoli, setVeicoli] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [openAddDialog, setOpenAddDialog] = useState(false);
  const [openEditDialog, setOpenEditDialog] = useState(false);
  const [openSearchDialog, setOpenSearchDialog] = useState(false);
  const [selectedVeicolo, setSelectedVeicolo] = useState(null);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: '',
    severity: 'success'
  });

  useEffect(() => {
    fetchVeicoli();
  }, []);

  const fetchVeicoli = async () => {
    try {
      setLoading(true);
      let data;
      try {
        data = await apiService.getVeicoli();
      } catch (err) {
        if (err.response && err.response.status === 404) {
          setError('Endpoint /veicoli non trovato. Verifica che il backend sia attivo.');
          setVeicoli([]);
          return;
        } else {
          throw err;
        }
      }
      setVeicoli(data);
      setError(null);
    } catch (err) {
      console.error('Errore durante il recupero dei veicoli:', err);
      setError('Errore durante il recupero dei veicoli. Riprova più tardi.');
      setVeicoli([]);
    } finally {
      setLoading(false);
    }
  };

  const handleAddVeicolo = async (values, { resetForm }) => {
    try {
      await apiService.createVeicolo(values);
      resetForm();
      setOpenAddDialog(false);
      fetchVeicoli();
      setSnackbar({
        open: true,
        message: 'Veicolo aggiunto con successo',
        severity: 'success'
      });
    } catch (err) {
      console.error('Errore durante l\'aggiunta del veicolo:', err);
      setSnackbar({
        open: true,
        message: 'Errore durante l\'aggiunta del veicolo',
        severity: 'error'
      });
    }
  };

  const handleEditVeicolo = async (values) => {
    try {
      await apiService.updateVeicolo(values.numeroTelaio, values);
      setOpenEditDialog(false);
      fetchVeicoli();
      setSnackbar({
        open: true,
        message: 'Veicolo aggiornato con successo',
        severity: 'success'
      });
    } catch (err) {
      console.error('Errore durante l\'aggiornamento del veicolo:', err);
      setSnackbar({
        open: true,
        message: 'Errore durante l\'aggiornamento del veicolo',
        severity: 'error'
      });
    }
  };

  const handleSearch = async (values) => {
    try {
      setLoading(true);
      // Rimuovi i campi vuoti dall'oggetto di ricerca
      // Invia solo i parametri supportati dal backend
      const searchParams = {
        numeroTelaio: values.numeroTelaio,
        marca: values.marca,
        modello: values.modello
      };
      // Rimuovi i parametri vuoti
      Object.keys(searchParams).forEach(key => {
        if (!searchParams[key]) delete searchParams[key];
      });
      const data = await apiService.searchVeicoli(searchParams);
      setVeicoli(data);
      setOpenSearchDialog(false);
    } catch (err) {
      console.error('Errore durante la ricerca dei veicoli:', err);
      setSnackbar({
        open: true,
        message: 'Errore durante la ricerca dei veicoli',
        severity: 'error'
      });
    } finally {
      setLoading(false);
    }
  };

  const handleCloseSnackbar = () => {
    setSnackbar(prev => ({ ...prev, open: false }));
  };

  const handleEditClick = (veicolo) => {
    setSelectedVeicolo(veicolo);
    setOpenEditDialog(true);
  };

  const resetSearch = () => {
    fetchVeicoli();
  };

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
    >
      <Box mb={4}>
        <Typography variant="h4" component="h1" gutterBottom fontWeight="bold">
          Gestione Veicoli
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
              Nuovo Veicolo
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
                  <TableCell>Numero Telaio</TableCell>
                  <TableCell>Marca</TableCell>
                  <TableCell>Modello</TableCell>
                  <TableCell>Anno</TableCell>
                  <TableCell>Stato</TableCell>
                  <TableCell>Azioni</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {veicoli.length > 0 ? (
                  veicoli.map((veicolo) => (
                    <TableRow key={veicolo.numeroTelaio}>
                      <TableCell>{veicolo.numeroTelaio}</TableCell>
                      <TableCell>{veicolo.marca}</TableCell>
                      <TableCell>{veicolo.modello}</TableCell>
                      <TableCell>{veicolo.annoImmatricolazione}</TableCell>
                      <TableCell>
                        <Chip 
                          label={veicolo.disponibile ? "Disponibile" : "Non disponibile"} 
                          color={veicolo.disponibile ? "success" : "error"}
                          size="small"
                        />
                      </TableCell>
                      <TableCell>
                        <IconButton 
                          color="primary" 
                          onClick={() => handleEditClick(veicolo)}
                          size="small"
                        >
                          <EditIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))
                ) : (
                  <TableRow>
                    <TableCell colSpan={6} align="center">
                      Nessun veicolo trovato
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </Paper>

      {/* Dialog per aggiungere un nuovo veicolo */}
      <Dialog 
        open={openAddDialog} 
        onClose={() => setOpenAddDialog(false)}
        maxWidth="md"
        fullWidth
      >
        <DialogTitle>Nuovo Veicolo</DialogTitle>
        <Formik
          initialValues={{
            numeroTelaio: '',
            marca: '',
            modello: '',

            annoImmatricolazione: new Date().getFullYear(),
            disponibile: true
          }}
          validationSchema={VeicoloSchema}
          onSubmit={handleAddVeicolo}
        >
          {({ errors, touched, isSubmitting, values, handleChange }) => (
            <Form>
              <DialogContent>
                <Grid container spacing={2}>
                  <Grid item xs={12} md={6}>
                    <Field
                      as={TextField}
                      name="numeroTelaio"
                      label="Numero Telaio"
                      fullWidth
                      margin="normal"
                      error={errors.numeroTelaio && touched.numeroTelaio}
                      helperText={touched.numeroTelaio && errors.numeroTelaio}
                    />
                  </Grid>
                  <Grid item xs={12} md={6}>
                    <Field
                      as={TextField}
                      name="marca"
                      label="Marca"
                      fullWidth
                      margin="normal"
                      error={errors.marca && touched.marca}
                      helperText={touched.marca && errors.marca}
                    />
                  </Grid>
                  <Grid item xs={12} md={6}>
                    <Field
                      as={TextField}
                      name="modello"
                      label="Modello"
                      fullWidth
                      margin="normal"
                      error={errors.modello && touched.modello}
                      helperText={touched.modello && errors.modello}
                    />
                  </Grid>
                  <Grid item xs={12} md={6}>
                    <Field
                      as={TextField}
                      name="annoImmatricolazione"
                      label="Anno Immatricolazione"
                      type="number"
                      fullWidth
                      margin="normal"
                      error={errors.annoImmatricolazione && touched.annoImmatricolazione}
                      helperText={touched.annoImmatricolazione && errors.annoImmatricolazione}
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <FormControl fullWidth margin="normal">
                      <InputLabel id="disponibile-label">Disponibilità</InputLabel>
                      <Select
                        labelId="disponibile-label"
                        name="disponibile"
                        value={values.disponibile}
                        onChange={handleChange}
                        label="Disponibilità"
                      >
                        <MenuItem value={true}>Disponibile</MenuItem>
                        <MenuItem value={false}>Non disponibile</MenuItem>
                      </Select>
                    </FormControl>
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

      {/* Dialog per modificare un veicolo */}
      <Dialog 
        open={openEditDialog} 
        onClose={() => setOpenEditDialog(false)}
        maxWidth="md"
        fullWidth
      >
        <DialogTitle>Modifica Veicolo</DialogTitle>
        {selectedVeicolo && (
          <Formik
            initialValues={{
              numeroTelaio: selectedVeicolo.numeroTelaio,
              marca: selectedVeicolo.marca,
              modello: selectedVeicolo.modello,

              annoImmatricolazione: selectedVeicolo.annoImmatricolazione,
              disponibile: selectedVeicolo.disponibile
            }}
            validationSchema={VeicoloSchema}
            onSubmit={handleEditVeicolo}
          >
            {({ errors, touched, isSubmitting, values, handleChange }) => (
              <Form>
                <DialogContent>
                  <Grid container spacing={2}>
                    <Grid item xs={12} md={6}>
                      <Field
                        as={TextField}
                        name="numeroTelaio"
                        label="Numero Telaio"
                        fullWidth
                        margin="normal"
                        disabled
                      />
                    </Grid>

                    <Grid item xs={12} md={6}>
  <Field
    as={TextField}
    name="marca"
    label="Marca"
    fullWidth
    margin="normal"
    disabled
    error={errors.marca && touched.marca}
    helperText={touched.marca && errors.marca}
  />
</Grid>
                    <Grid item xs={12} md={6}>
  <Field
    as={TextField}
    name="modello"
    label="Modello"
    fullWidth
    margin="normal"
    disabled
    error={errors.modello && touched.modello}
    helperText={touched.modello && errors.modello}
  />
</Grid>
                    <Grid item xs={12} md={6}>
  <Field
    as={TextField}
    name="annoImmatricolazione"
    label="Anno Immatricolazione"
    type="number"
    fullWidth
    margin="normal"
    disabled
    error={errors.annoImmatricolazione && touched.annoImmatricolazione}
    helperText={touched.annoImmatricolazione && errors.annoImmatricolazione}
  />
</Grid>
                    <Grid item xs={12}>
                      <FormControl fullWidth margin="normal">
                        <InputLabel id="disponibile-edit-label">Disponibilità</InputLabel>
                        <Select
                          labelId="disponibile-edit-label"
                          name="disponibile"
                          value={values.disponibile}
                          onChange={handleChange}
                          label="Disponibilità"
                        >
                          <MenuItem value={true}>Disponibile</MenuItem>
                          <MenuItem value={false}>Non disponibile</MenuItem>
                        </Select>
                      </FormControl>
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
        <DialogTitle>Ricerca Veicoli</DialogTitle>
        <Formik
          initialValues={{
            numeroTelaio: '',
            marca: '',
            modello: ''
          }}
          onSubmit={handleSearch}
        >
          {({ isSubmitting, values, handleChange }) => (
            <Form>
              <DialogContent>
                <Grid container spacing={2}>
                  <Grid item xs={12} md={4}>
                    <Field
                      as={TextField}
                      name="numeroTelaio"
                      label="Numero Telaio"
                      fullWidth
                      margin="normal"
                    />
                  </Grid>
                  <Grid item xs={12} md={4}>
                    <Field
                      as={TextField}
                      name="marca"
                      label="Marca"
                      fullWidth
                      margin="normal"
                    />
                  </Grid>
                  <Grid item xs={12} md={4}>
                    <Field
                      as={TextField}
                      name="modello"
                      label="Modello"
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

export default VeicoliPage;

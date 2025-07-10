import React, { useState, useEffect } from 'react';
import { Typography, Paper, Box, Snackbar, Alert, CircularProgress, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Dialog, DialogTitle, DialogContent, DialogActions, TextField } from '@mui/material';
import CommonButton from '../common/CommonButton';
import { motion } from 'framer-motion';
import apiService from '../../services/apiService';


const ConfigurazioniPage = () => {
  const [configurazioni, setConfigurazioni] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: '',
    severity: 'success'
  });
  const [editDialogOpen, setEditDialogOpen] = useState(false);
  const [editConfig, setEditConfig] = useState(null);
  const [editValue, setEditValue] = useState('');

  useEffect(() => {
    fetchConfigurazioni();
  }, []);

  const fetchConfigurazioni = async () => {
    try {
      setLoading(true);
      const data = await apiService.getConfigurazioni();
      setConfigurazioni(data || []);
      setError(null);
    } catch (err) {
      console.error('Errore durante il recupero delle configurazioni:', err);
      setError('Errore durante il recupero delle configurazioni. Riprova più tardi.');
    } finally {
      setLoading(false);
    }
  };

  

  const handleCloseSnackbar = () => {
    setSnackbar(prev => ({ ...prev, open: false }));
  };
  // Gestione apertura dialog di modifica
  const handleEditClick = (config) => {
    setEditConfig(config);
    setEditValue(config.nomeConfigurazione);
    setEditDialogOpen(true);
  };

  // Gestione chiusura dialog
  const handleEditDialogClose = () => {
    setEditDialogOpen(false);
    setEditConfig(null);
    setEditValue('');
  };

  // Salva modifica
  const handleEditSave = async () => {
    if (!editConfig) return;
    try {
      setLoading(true);
      const newConfig = { ...editConfig, nomeConfigurazione: editValue };
      // Passa id e oggetto configurazione come richiesto dal backend e dal nuovo apiService
      await apiService.updateConfigurazioni(newConfig.idConfigurazione, newConfig);
      setSnackbar({ open: true, message: 'Configurazione aggiornata con successo', severity: 'success' });
      setConfigurazioni(prev => prev.map(c => c.idConfigurazione === newConfig.idConfigurazione ? newConfig : c));
      handleEditDialogClose();
    } catch (err) {
      setSnackbar({ open: true, message: 'Errore durante l\'aggiornamento', severity: 'error' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
    >
      {/* Dialog di modifica configurazione */}
      <Dialog open={editDialogOpen} onClose={handleEditDialogClose}>
        <DialogTitle>Modifica Configurazione</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Nome Configurazione"
            type="text"
            fullWidth
            value={editValue}
            onChange={e => setEditValue(e.target.value)}
          />
        </DialogContent>
        <DialogActions>
          <CommonButton onClick={handleEditDialogClose} color="secondary">Annulla</CommonButton>
          <CommonButton onClick={handleEditSave} variant="contained" color="primary">Salva</CommonButton>
        </DialogActions>
      </Dialog>
      <Box mb={4}>
        <Typography variant="h4" component="h1" gutterBottom fontWeight="bold">
          Configurazioni Sistema
        </Typography>
      </Box>

      {loading ? (
        <Box display="flex" justifyContent="center" my={4}>
          <CircularProgress />
        </Box>
      ) : error ? (
        <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>
      ) : configurazioni.length > 0 ? (
        <TableContainer component={Paper} sx={{ mt: 4 }}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>Nome Configurazione</TableCell>
                <TableCell>Azioni</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {configurazioni.map(config => (
                <TableRow key={config.idConfigurazione}>
                  <TableCell>{config.idConfigurazione}</TableCell>
                  <TableCell>{config.nomeConfigurazione}</TableCell>
                  <TableCell>
                    <CommonButton 
                      variant="contained" 
                      color="primary" 
                      size="small"
                      onClick={() => handleEditClick(config)}
                      sx={{ 
                        fontSize: '0.75rem',
                        py: 0.5,
                        minWidth: '80px'
                      }}
                    >
                      Modifica
                    </CommonButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      ) : (
        <Alert severity="info">Nessuna configurazione trovata</Alert>
      )}

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

export default ConfigurazioniPage;

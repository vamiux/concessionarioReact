import React from 'react';
import { Link } from 'react-router-dom';
import { 
  Container, 
  Box, 
  Typography, 
  Grid, 
  Paper
} from '@mui/material';
import { 
  People as PeopleIcon, 
  DirectionsCar as CarIcon, 
  CompareArrows as MovimentiIcon
} from '@mui/icons-material';
import { motion } from 'framer-motion';


const Dashboard = () => {

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: { 
      opacity: 1,
      transition: { 
        staggerChildren: 0.2
      }
    }
  };

  const itemVariants = {
    hidden: { y: 20, opacity: 0 },
    visible: { 
      y: 0, 
      opacity: 1,
      transition: { 
        type: 'spring',
        stiffness: 100
      }
    }
  };

  const cardData = [
    {
      title: 'Gestione Utenti',
      description: 'Gestisci i clienti del concessionario',
      icon: <PeopleIcon sx={{ fontSize: 60, color: '#1976d2' }} />,
      path: '/utenti'
    },
    {
      title: 'Gestione Veicoli',
      description: 'Gestisci l\'inventario dei veicoli',
      icon: <CarIcon sx={{ fontSize: 60, color: '#1976d2' }} />,
      path: '/veicoli'
    },
    {
      title: 'Gestione Movimenti',
      description: 'Gestisci vendite e acquisti',
      icon: <MovimentiIcon sx={{ fontSize: 60, color: '#1976d2' }} />,
      path: '/movimenti'
    }
  ];

  return (
  <Container maxWidth="lg" sx={{ mt: 6, mb: 6 }}>
    <motion.div
      variants={containerVariants}
      initial="hidden"
      animate="visible"
    >
          <Typography 
            variant="h3" 
            component="h1" 
            align="center" 
            gutterBottom
            sx={{ 
              fontWeight: 'bold',
              color: '#1976d2',
              mb: 4
            }}
          >
            Dashboard Concessionario
          </Typography>
          
          <Typography 
            variant="h6" 
            align="center" 
            color="textSecondary" 
            paragraph
            sx={{ mb: 6 }}
          >
            Benvenuto nel sistema di gestione del concessionario. Seleziona una delle opzioni sottostanti per iniziare.
          </Typography>
          
          <Grid container spacing={4}>
            {cardData.map((card, index) => (
              <Grid item xs={12} md={4} key={index}>
                <motion.div variants={itemVariants}>
                  <Paper
                    component={Link}
                    to={card.path}
                    sx={{
                      p: 4,
                      height: '100%',
                      display: 'flex',
                      flexDirection: 'column',
                      alignItems: 'center',
                      textAlign: 'center',
                      textDecoration: 'none',
                      color: 'inherit',
                      borderRadius: 2,
                      transition: 'all 0.3s ease',
                      '&:hover': {
                        transform: 'translateY(-8px)',
                        boxShadow: '0 10px 20px rgba(0,0,0,0.1)',
                        bgcolor: '#f5f9ff'
                      }
                    }}
                    elevation={3}
                  >
                    <Box mb={2}>
                      {card.icon}
                    </Box>
                    <Typography variant="h5" component="h2" gutterBottom fontWeight="bold">
                      {card.title}
                    </Typography>
                    <Typography variant="body1" color="textSecondary">
                      {card.description}
                    </Typography>
                  </Paper>
                </motion.div>
              </Grid>
            ))}
          </Grid>
    </motion.div>
  </Container>
);
};

export default Dashboard;

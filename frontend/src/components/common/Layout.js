import React, { useContext } from 'react';
import { Outlet, Link, useLocation, useNavigate } from 'react-router-dom';
import { 
  AppBar, 
  Toolbar, 
  Typography, 
  Container, 
  Box, 
  IconButton,
  Drawer,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Divider
} from '@mui/material';
import CommonButton from './CommonButton';
import { 
  Menu as MenuIcon, 
  Home as HomeIcon,
  People as PeopleIcon,
  DirectionsCar as CarIcon,
  CompareArrows as MovimentiIcon,
  Settings as SettingsIcon,
  Logout as LogoutIcon
} from '@mui/icons-material';
import { motion } from 'framer-motion';
import { AuthContext } from '../../context/AuthContext';

const Layout = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [drawerOpen, setDrawerOpen] = React.useState(false);
  const { user, logout } = useContext(AuthContext);

  const toggleDrawer = (open) => (event) => {
    if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
      return;
    }
    setDrawerOpen(open);
  };

  const menuItems = [
    { text: 'Dashboard', icon: <HomeIcon />, path: '/' },
    { text: 'Utenti', icon: <PeopleIcon />, path: '/utenti' },
    { text: 'Veicoli', icon: <CarIcon />, path: '/veicoli' },
    { text: 'Movimenti', icon: <MovimentiIcon />, path: '/movimenti' },
    { text: 'Configurazioni', icon: <SettingsIcon />, path: '/configurazioni' }
  ];

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <AppBar
  position="static"
  elevation={3}
  sx={{
    borderRadius: 0,
    background: 'linear-gradient(90deg, #1976d2 0%, #1565c0 100%)',
    boxShadow: '0 2px 8px rgba(25, 118, 210, 0.10)',
    px: 2,
    minHeight: 64,
    justifyContent: 'center',
  }}
>
  <Toolbar
    sx={{
      minHeight: 64,
      px: 0,
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
    }}
  >
    <Box sx={{ display: 'flex', alignItems: 'center' }}>
      <IconButton
        edge="start"
        color="inherit"
        aria-label="menu"
        onClick={toggleDrawer(true)}
        sx={{ mr: 1, p: 1.2 }}
      >
        <MenuIcon />
      </IconButton>
      <Typography
        variant="h6"
        component="div"
        sx={{
          fontWeight: 700,
          letterSpacing: 1,
          color: '#fff',
          textShadow: '0 1px 4px rgba(0,0,0,0.08)'
        }}
      >
        Concessionario
      </Typography>
    </Box>
    <Box display="flex" alignItems="center" sx={{ gap: 2 }}>
      <Typography
        variant="body1"
        sx={{ color: '#e3f2fd', fontWeight: 500, mr: 1 }}
      >
        {user?.username}
      </Typography>
      <IconButton
  color="inherit"
  onClick={logout}
  title="Logout"
  sx={{
    background: 'rgba(255,255,255,0.08)',
    transition: 'background 0.2s',
    '&:hover': {
      background: 'rgba(244,67,54,0.12)',
      color: 'error.main',
      '& .MuiSvgIcon-root': {
        color: 'error.main',
      }
    }
  }}
>
  <LogoutIcon />
</IconButton>
    </Box>
  </Toolbar>
</AppBar>

      <Drawer
  anchor="left"
  open={drawerOpen}
  onClose={toggleDrawer(false)}
  PaperProps={{
    sx: {
      borderRadius: 0,
      boxShadow: '0 4px 24px rgba(25, 118, 210, 0.10)',
      bgcolor: 'background.paper',
      minWidth: 260
    }
  }}
>
  <Box
    sx={{ width: 260, p: 0, height: '100%' }}
    role="presentation"
    onClick={toggleDrawer(false)}
    onKeyDown={toggleDrawer(false)}
  >
    <Box sx={{ p: 2, bgcolor: 'primary.main', color: 'white', borderBottomLeftRadius: 0, borderBottomRightRadius: 0 }}>
      <Typography variant="h6" sx={{ fontWeight: 700, letterSpacing: 1 }}>Menu</Typography>
    </Box>
    <Divider />
    <List sx={{ py: 1 }}>
      {menuItems.map((item, index) => (
        <ListItem
          button
          key={index}
          component={Link}
          to={item.path}
          sx={{
            borderRadius: 1,
            mx: 1,
            my: 0.5,
            transition: 'background 0.2s',
            '&:hover': {
              bgcolor: 'rgba(25, 118, 210, 0.12)',
              boxShadow: '0 2px 8px rgba(25, 118, 210, 0.08)'
            }
          }}
        >
          <ListItemIcon sx={{ color: 'primary.main', minWidth: 36 }}>{item.icon}</ListItemIcon>
          <ListItemText primary={item.text} sx={{ '.MuiListItemText-primary': { fontWeight: 500 } }} />
        </ListItem>
      ))}
    </List>
    <Divider sx={{ my: 1 }} />
    <List>
      <ListItem
  button
  onClick={logout}
  sx={{
    borderRadius: 1,
    mx: 1,
    my: 0.5,
    transition: 'background 0.2s',
    '&:hover': {
      bgcolor: 'rgba(244, 67, 54, 0.18)',
      boxShadow: '0 2px 8px rgba(244, 67, 54, 0.08)',
      '& .MuiListItemIcon-root, & .MuiSvgIcon-root': {
        color: 'error.main',
      },
      '& .MuiListItemText-primary': {
        color: 'error.main',
      }
    }
  }}
>
  <ListItemIcon sx={{ color: 'error.main', minWidth: 36 }}><LogoutIcon /></ListItemIcon>
  <ListItemText primary="Logout" sx={{ '.MuiListItemText-primary': { fontWeight: 500 } }} />
</ListItem>
    </List>
  </Box>
</Drawer>

      <Container component="main" sx={{ flexGrow: 1, py: 4 }}>
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 0.3 }}
        >
          {location.pathname !== '/' && (
            <Box mb={3}>
              <CommonButton 
                variant="outlined" 
                startIcon={<HomeIcon />} 
                onClick={() => navigate('/')} 
                sx={{ mb: 2 }}
              >
                Torna alla Dashboard
              </CommonButton>
            </Box>
          )}
          <Outlet />
        </motion.div>
      </Container>

      <Box component="footer" sx={{ py: 3, bgcolor: '#f5f5f5', mt: 'auto' }}>
        <Container maxWidth="lg">
          <Typography variant="body2" color="text.secondary" align="center">
            © {new Date().getFullYear()} Concessionario - Tutti i diritti riservati
          </Typography>
        </Container>
      </Box>
    </Box>
  );
};

export default Layout;

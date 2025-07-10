import React from 'react';
import Button from '@mui/material/Button';

/**
 * CommonButton - Un pulsante riutilizzabile con stile coerente in tutta l'app.
 * Props: Tutte le props standard di MUI Button sono supportate.
 */
const CommonButton = ({
  children,
  variant = 'contained',
  color = 'primary',
  size = 'medium',
  sx = {},
  ...props
}) => {
  // Stile base coerente per tutti i pulsanti secondo Material Design
  const baseStyle = {
    borderRadius: 4,
    textTransform: 'none',
    fontWeight: 500,
    letterSpacing: '0.02em',
    transition: 'all 0.2s ease-in-out',
    ...sx,
  };

  // Stili specifici per dimensione
  const sizeStyles = {
    small: {
      fontSize: '0.8125rem',
      px: 2,
      py: 0.6,
    },
    medium: {
      fontSize: '0.875rem',
      px: 2.5,
      py: 0.8,
    },
    large: {
      fontSize: '0.9375rem',
      px: 3,
      py: 1,
    }
  };

  // Stili per varianti secondo Material Design
  const variantStyles = {
    contained: {
      background: 'linear-gradient(45deg, #2196f3 30%, #21CBF3 90%)',
      color: '#fff',
      boxShadow: '0 2px 4px rgba(33,150,243,0.25)',
      '&:hover': {
        background: 'linear-gradient(45deg, #1976d2 30%, #1CB5E0 90%)',
        boxShadow: '0 4px 8px rgba(33,150,243,0.3)',
        transform: 'translateY(-1px)'
      },
      '&:active': {
        boxShadow: '0 1px 3px rgba(33,150,243,0.4)',
        transform: 'translateY(0)'
      }
    },
    outlined: {
      borderColor: '#2196f3',
      color: '#2196f3',
      background: 'transparent',
      borderWidth: '1px',
      '&:hover': {
        borderColor: '#1976d2',
        backgroundColor: 'rgba(33,150,243,0.04)'
      }
    },
    text: {
      color: '#2196f3',
      '&:hover': {
        backgroundColor: 'rgba(33,150,243,0.08)'
      }
    }
  };

  // Combina gli stili in base alle props
  const combinedStyles = {
    ...baseStyle,
    ...sizeStyles[size],
    ...variantStyles[variant]
  };

  return (
    <Button
      variant={variant}
      color={color}
      size={size}
      sx={combinedStyles}
      disableElevation={variant === 'contained'}
      {...props}
    >
      {children}
    </Button>
  );
};

export default CommonButton;

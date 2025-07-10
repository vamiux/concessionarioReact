# Concessionario React

Applicazione per la gestione di un concessionario auto, sviluppata con Spring Boot e React.

## Tecnologie utilizzate

### Backend
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL

### Frontend
- React
- React Router
- Material-UI
- Formik e Yup
- Framer Motion
- Axios

## Funzionalità

L'applicazione consente di gestire:
- Utenti/Clienti
- Veicoli
- Movimenti (acquisti e vendite)
- Configurazioni del sistema

## Requisiti

- Java 11 o superiore
- Node.js 14 o superiore
- MySQL

## Installazione e avvio

1. Clonare il repository
2. Configurare il database MySQL nel file `application.properties`
3. Installare le dipendenze frontend:
   ```
   cd src/main/frontend
   npm install
   ```
4. Avviare l'applicazione:
   ```
   ./mvnw spring-boot:run
   ```
   
L'applicazione sarà disponibile all'indirizzo: http://localhost:8080

## Struttura del progetto

### Backend
- `src/main/java/com/stage/concessionario/controller`: Controller REST
- `src/main/java/com/stage/concessionario/model`: Entità JPA
- `src/main/java/com/stage/concessionario/repository`: Repository JPA
- `src/main/java/com/stage/concessionario/service`: Servizi
- `src/main/java/com/stage/concessionario/dto`: Data Transfer Objects
- `src/main/java/com/stage/concessionario/config`: Configurazioni
- `src/main/java/com/stage/concessionario/security`: Configurazioni di sicurezza

### Frontend
- `src/main/frontend/src/components`: Componenti React
- `src/main/frontend/src/services`: Servizi per le chiamate API
- `src/main/frontend/src/context`: Context API di React
- `src/main/frontend/src/styles`: File CSS
- `src/main/frontend/src/assets`: Risorse statiche

## Autenticazione

L'applicazione utilizza Spring Security per l'autenticazione. Le credenziali di default sono:
- Email: admin@example.com
- Password: password

## Licenza

Questo progetto è rilasciato sotto licenza MIT.

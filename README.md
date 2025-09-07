# 📝 API Notas Privadas Seguras

API REST desarrollada con Spring Boot 3 que implementa un sistema de autenticación JWT con refresh tokens y control de acceso basado en roles para gestionar notas privadas de forma segura.

## 🚀 Requisitos

- Java 17+
- Maven 3.8+
- MySQL 8+

## ⚙️ Configuración

### 1. Base de datos

```sql
-- Crear base de datos
CREATE DATABASE notas_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE notas_db;

-- Crear tabla usuarios
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    admin BOOLEAN DEFAULT FALSE,
    failed_attempts INT DEFAULT 0,
    lock_until BIGINT DEFAULT NULL
);

-- Crear tabla roles de usuario
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    roles VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Crear tabla notas
CREATE TABLE notes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    content TEXT NOT NULL,
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users(id)
);

-- Crear usuarios demo (contraseña: Password123)
INSERT INTO users (email, password, admin) VALUES 
('user@demo.com', '$2a$12$LQV3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LsY3.71GwQCFMhiD2', false),
('admin@demo.com', '$2a$12$LQV3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LsY3.71GwQCFMhiD2', true);

-- Asignar roles
INSERT INTO user_roles (user_id, roles) VALUES 
(1, 'USER'),
(2, 'USER'),
(2, 'ADMIN');
```

### 2. Ejecutar aplicación

```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

## 👤 Usuarios Demo

### Usuario Normal
- Email: `user@demo.com`
- Password: `Password123`
- Rol: USER
- Permisos: Gestionar sus propias notas

### Administrador  
- Email: `admin@demo.com`
- Password: `Password123`
- Roles: USER, ADMIN
- Permisos: Gestionar todas las notas + usuarios

## 🛡️ Características de Seguridad

- ✅ Autenticación JWT con tokens de acceso de corta duración (15 min)
- ✅ Refresh Tokens en cookies HttpOnly para mayor seguridad
- ✅ Bloqueo de cuenta tras 5 intentos fallidos por 15 minutos
- ✅ Control de acceso basado en roles (USER/ADMIN)
- ✅ Validación de datos con Jakarta Bean Validation
- ✅ Protección contra XSS con headers de seguridad
- ✅ Control de propiedad de recursos
- ✅ Manejo seguro de contraseñas con BCrypt

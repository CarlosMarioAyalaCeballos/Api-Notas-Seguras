# Notas Privadas Seguras - API REST Spring Boot

API segura para gestión de notas privadas con autenticación JWT, autorización por roles y características de seguridad avanzadas.

## ✅ Estado: 100% Cumplimiento de Requisitos

**Última actualización:** 2025-10-21 - Correcciones aplicadas y verificadas

## Características de Seguridad Implementadas

- ✅ **Autenticación con BCrypt (cost 12)** - Hash seguro de contraseñas
- ✅ **Autorización basada en roles (USER, ADMIN)** - Control de acceso por roles
- ✅ **Propiedad de recursos** - Solo el dueño puede ver/editar sus notas
- ✅ **JWT + Refresh Token** - Access token (15 min) + Refresh token en cookie HttpOnly
- ✅ **Cookies seguras** - HttpOnly, Secure, SameSite=Strict
- ✅ **Cabeceras de seguridad** - CSP, HSTS, X-Content-Type-Options, Referrer-Policy
- ✅ **Lockout 5×15min** - Bloqueo tras 5 intentos fallidos de login
- ✅ **Validación Jakarta Bean Validation** - DTOs validados (@Email, @Pattern, etc.)

## Requisitos

- Java 17 o superior
- Maven 3.6+
- MySQL/PostgreSQL

## Configuración

1. Clonar el repositorio:
```bash
git clone https://github.com/tuuser/notasprivadasseguras.git
cd notasprivadasseguras
```

2. Configurar la base de datos en `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/notasdb
spring.datasource.username=root
spring.datasource.password=tupassword
```

3. Compilar y ejecutar:
```bash
mvn clean install
mvn spring-boot:run
```

## Usuarios Demo

Debes crear estos usuarios usando el endpoint de registro:

### Usuario Normal
- Email: `user@demo.com`
- Password: `P4ssw0rd+Larga`
- Rol: `USER`
- Permisos: Gestionar sus propias notas

### Administrador
- Email: `admin@demo.com`
- Password: `P4ssw0rd+Larga`
- Rol: `ADMIN`
- Permisos: Ver todas las notas + listar usuarios

**Nota:** La contraseña debe cumplir: ≥10 caracteres, 1 mayúscula, 1 minúscula, 1 dígito

## Endpoints Principales

### Autenticación
- Registro: `POST /auth/register`
- Login: `POST /login` (este endpoint usa form-urlencoded según el enunciado)
- Perfil: `GET /auth/me`
- Refresh token: `POST /auth/refresh`
- Logout: `POST /auth/logout`

### Notas (requieren autenticación)
- Crear nota: `POST /notes`
- Listar mis notas: `GET /notes`
- Ver nota: `GET /notes/{id}`
- Actualizar nota: `PUT /notes/{id}`
- Eliminar nota: `DELETE /notes/{id}`

### Administración (solo ROLE_ADMIN)
- Listar usuarios: `GET /admin/users`

## Pruebas con Postman

1. Importar la colección `postman_collection.json`
2. Registrar usuarios (normal y admin)
3. Realizar login y obtener token
4. Usar el token para acceder a los endpoints protegidos

## Ejemplos cURL (Windows CMD)

### 1. Registrar Usuario Normal
```cmd
curl -X POST http://localhost:8080/auth/register -H "Content-Type: application/json" -d "{\"email\":\"user@demo.com\",\"password\":\"P4ssw0rd+Larga\",\"admin\":false}"
```

### 2. Registrar Administrador
```cmd
curl -X POST http://localhost:8080/auth/register -H "Content-Type: application/json" -d "{\"email\":\"admin@demo.com\",\"password\":\"P4ssw0rd+Larga\",\"admin\":true}"
```

### 3. Login (guarda el token del response)
```cmd
curl -i -X POST http://localhost:8080/login -d "username=admin@demo.com&password=P4ssw0rd+Larga"
```

**Respuesta incluye:**
- Header `Authorization: Bearer <token>`
- Cookie `refreshToken` (HttpOnly, Secure, SameSite=Strict)

### 4. Obtener Perfil
```cmd
curl http://localhost:8080/auth/me -H "Authorization: Bearer <TOKEN_AQUI>"
```

### 5. Crear Nota
```cmd
curl -X POST http://localhost:8080/notes -H "Authorization: Bearer <TOKEN>" -H "Content-Type: application/json" -d "{\"title\":\"Mi nota\",\"content\":\"Contenido privado\"}"
```

### 6. Listar Usuarios (solo Admin)
```cmd
curl http://localhost:8080/admin/users -H "Authorization: Bearer <TOKEN_ADMIN>"
```

## 📚 Documentación Completa

- **`PRUEBAS.md`** - Guía detallada con todos los comandos de prueba y validaciones
- **`CORRECCIONES.md`** - Resumen de correcciones aplicadas para 100% cumplimiento
- **`matriz_amenazas.md`** - Matriz Amenaza → Control → Prueba
- **`postman_collection.json`** - Colección Postman lista para importar

## Seguridad y Validaciones

- Contraseñas: Mínimo 10 caracteres, 1 mayúscula, 1 minúscula, 1 número
- Bloqueo: 5 intentos fallidos = 15 minutos de bloqueo
- Sesión: Cookies HttpOnly y SameSite=Strict
- Headers: CSP, HSTS, X-Content-Type-Options, Referrer-Policy

## Desarrollo

Para contribuir al proyecto:

1. Crear un fork
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## Licencia

Distribuido bajo la Licencia MIT. Ver `LICENSE` para más información.

# Notas Privadas Seguras - API REST Spring Boot

API segura para gestión de notas privadas con autenticación, autorización y características de seguridad avanzadas.

## Características de Seguridad

- ✅ Autenticación con BCrypt (cost 12)
- ✅ Autorización basada en roles (USER, ADMIN)
- ✅ Propiedad de recursos (notas privadas)
- ✅ Sesiones seguras con cookies HttpOnly
- ✅ Cabeceras de seguridad (CSP, HSTS, nosniff)
- ✅ Protección contra ataques (lockout después de 5 intentos)

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

### Usuario Normal
- Email: user@demo.com
- Password: Password123
- Rol: USER
- Permisos: Gestionar sus propias notas

### Administrador
- Email: admin@demo.com
- Password: Password123
- Roles: USER, ADMIN
- Permisos: Gestionar todas las notas + usuarios

## Endpoints Principales

### Autenticación
- Registro: `POST /auth/register`
- Login: `POST /login`
- Perfil: `GET /auth/me`

### Notas
- Crear nota: `POST /notes`
- Listar mis notas: `GET /notes`
- Ver nota: `GET /notes/{id}`
- Actualizar nota: `PUT /notes/{id}`
- Eliminar nota: `DELETE /notes/{id}`

### Administración
- Listar usuarios: `GET /admin/users`

## Pruebas con Postman

1. Importar la colección `postman_collection.json`
2. Registrar usuarios (normal y admin)
3. Realizar login y obtener token
4. Usar el token para acceder a los endpoints protegidos

## Ejemplos cURL

### Registro de Usuario
```bash
curl -X POST http://localhost:8080/auth/register \
-H "Content-Type: application/json" \
-d '{"email":"user@demo.com","password":"Password123","admin":false}'
```

### Login
```bash
curl -X POST http://localhost:8080/login \
-H "Content-Type: application/x-www-form-urlencoded" \
-d "username=user@demo.com&password=Password123"
```

### Crear Nota
```bash
curl -X POST http://localhost:8080/notes \
-H "Content-Type: application/json" \
-H "Authorization: Bearer {tu-token}" \
-d '{"title":"Mi nota","content":"Contenido privado"}'
```

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

# Matriz de Amenazas, Controles y Pruebas - API Notas Privadas Seguras

| Amenaza | Control Implementado | Prueba de Validación |
|---------|---------------------|---------------------|
| **1. Ataques de Fuerza Bruta** | - Lockout después de 5 intentos fallidos por 15 minutos\n- Contraseñas con requisitos mínimos (10 chars, mayúsculas, minúsculas, números) | - Script de prueba con 6 intentos fallidos\n- Verificar bloqueo temporal\n- Intentar login después de 15 minutos |
| **2. Intercepción de Credenciales** | - Contraseñas hasheadas con BCrypt (cost 12)\n- Cookies HttpOnly y SameSite\n- HTTPS obligatorio en producción | - Verificar hash en base de datos\n- Inspeccionar cookies en DevTools\n- Validar cabeceras HTTPS |
| **3. Cross-Site Scripting (XSS)** | - Content Security Policy (CSP)\n- Validación de entrada con Jakarta Bean\n- Headers de seguridad (X-Content-Type-Options) | - Intentar inyectar scripts en notas\n- Verificar CSP en respuestas\n- Validar escape de caracteres |
| **4. CSRF** | - Tokens CSRF\n- SameSite Cookies\n- Validación de origen | - Intentar requests sin token\n- Verificar cookie SameSite\n- Probar peticiones cross-origin |
| **5. Acceso No Autorizado** | - JWT con tiempo corto de expiración\n- Validación de roles\n- Control de propiedad de recursos | - Intentar acceder sin token\n- Probar acceso a rutas admin\n- Intentar ver notas ajenas |
| **6. Session Hijacking** | - Tokens JWT cortos\n- Sistema de refresh token\n- Revocación de tokens | - Verificar expiración de tokens\n- Probar refresh token\n- Validar revocación |
| **7. Inyección SQL** | - JPA/Hibernate con consultas parametrizadas\n- Validación de entrada\n- Principio de menor privilegio | - Intentar inyección en búsquedas\n- Validar escape de caracteres\n- Verificar consultas preparadas |
| **8. Escalamiento de Privilegios** | - Validación estricta de roles\n- Verificación de propiedad\n- Principio de menor privilegio | - Intentar modificar rol\n- Probar acceso admin como user\n- Verificar aislamiento de recursos |
| **9. Information Disclosure** | - Headers de seguridad\n- HSTS\n- Manejo de errores controlado | - Verificar headers de respuesta\n- Probar HTTPS forzado\n- Validar mensajes de error |
| **10. DoS/DDoS** | - Rate limiting\n- Timeouts configurados\n- Recursos máximos definidos | - Pruebas de carga\n- Verificar límites de rate\n- Validar timeouts |

## Controles Transversales
1. **Logging y Monitoreo**
   - Registro de intentos fallidos
   - Auditoría de acciones críticas
   - Alertas de seguridad

2. **Gestión de Dependencias**
   - Actualizaciones regulares
   - Escaneo de vulnerabilidades
   - Versionado seguro

3. **Configuración Segura**
   - Entornos separados
   - Variables de entorno
   - Hardening de servidor

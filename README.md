# Guía para Ejecutar el Proyecto

## Requisitos

1. **Java Development Kit (JDK) 22**: Asegúrate de tener JDK 22 instalado en tu máquina local.
2. **Maven**: Verifica que Maven esté instalado.
3. **PostgreSQL**: Asegúrate de tener PostgreSQL instalado. Las credenciales predeterminadas son:
   - **URL**: `jdbc:postgresql://localhost:5432/postgres`
   - **Username**: `postgres`
   - **Password**: `123456789`

   Si usas credenciales diferentes, actualiza la configuración de los microservicios en tu proyecto en consecuencia. El esquema SQL utilizado es `public`, y las tablas se crean automáticamente mediante Hibernate.

## Acceso al Swagger

- **Swagger para Microservicio Login**: [http://localhost:8003/api/v1/doc/swagger-ui/index.html](http://localhost:8003/api/v1/doc/swagger-ui/index.html)
- **Swagger para Microservicio Reservation**: [http://localhost:8001/api/v1/doc/swagger-ui/index.html#/servicios-controller](http://localhost:8001/api/v1/doc/swagger-ui/index.html#/servicios-controller)

Si no se encuentran expuestos los respectivos endpoints en Swagger, asegúrate de que la siguiente ruta esté presente en la barra de búsqueda de Swagger: `/api/v1/v3/api-docs`.

## Orden de Arranque de los Microservicios

1. **Microservicio Commons**:
   - **Descripción**: Responsable de crear la base de datos y proporcionar funcionalidades globales para el sistema.
   - **Acción**: Inicia este servicio primero.
   - **Post-inicio**: Después de iniciar el microservicio Commons, ejecuta `mvn clean install` para generar una dependencia local de este servicio.

2. **Microservicio Login**:
   - **Descripción**: Se encarga de la autenticación mediante JWT (JSON Web Tokens).
   - **Acción**: Inicia este servicio después del microservicio Commons.
   - **Post-inicio**: Ejecuta `mvn clean install` para generar una dependencia local de este servicio tras su inicio.

3. **Microservicio Reservation**:
   - **Descripción**: Maneja las reservaciones.
   - **Acción**: Inicia este servicio después del microservicio Login.

## Configuración Adicional

- **Creación de Usuario**: Para que el sistema funcione correctamente, crea un usuario utilizando la API del Microservicio Login.

## Notas

- Siguiendo este orden y estos pasos, el proyecto debería iniciarse sin errores.
- Asegúrate de que todos los microservicios estén funcionando correctamente y revisa los logs en caso de problemas.


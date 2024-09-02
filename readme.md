# Guía para Ejecutar el Proyecto

## Requisitos

1. **Java Development Kit (JDK) 22**: Asegúrate de tener JDK 22 instalado en tu máquina local.
2. **Maven**: Debes tener Maven instalado.

3. **PostgreSQL**: Asegúrate de tener PostgreSQL instalado. Las credenciales predeterminadas son:
   - **URL**: `jdbc:postgresql://localhost:5432/postgres`
   - **Username**: `postgres`
   - **Password**: `123456789`

   Si usas credenciales diferentes, actualiza la configuración de los microservicios en tu proyecto en consecuencia.

   El esquema SQL utilizado es `public`, y las tablas se crean automáticamente mediante Hibernate.

## Orden de Arranque de los Microservicios

1. **Microservicio Commons**:
   - **Descripción**: Este microservicio es responsable de crear la base de datos y de proporcionar funcionalidades globales para el sistema.
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

- Si sigues este orden y estos pasos, el proyecto debería iniciarse sin errores.
- Asegúrate de que todos los microservicios estén funcionando correctamente y revisa los logs en caso de problemas.


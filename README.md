# SimpleApiRest

Este proyecto es una API REST desarrollada con Spring Boot que presenta una implementación sencilla pero completa de un servicio CRUD para la entidad `UserEntity`. Se destaca por haber sido desarrollado siguiendo la metodología de Desarrollo Guiado por Pruebas (Test-Driven Development - TDD).

## Características clave

- **Metodología TDD:** El desarrollo de servicios y controladores fue guiado por pruebas, lo que garantiza una cobertura sólida de pruebas unitarias a lo largo del proyecto.

- **Base de Datos en Memoria:** Se utiliza una base de datos SQL en memoria H2 para las pruebas, lo que facilita la ejecución de las pruebas de integración sin afectar la base de datos de producción.

- **Buenas Prácticas de Desarrollo:** Se siguieron prácticas de desarrollo robustas, incluyendo la utilización de anotaciones para la validación en la entidad, manejo de excepciones, inyección de dependencias y naming consistente en inglés.

- **Commits Significativos:** Se realizaron commits regulares con mensajes significativos en inglés, siguiendo estándares de redacción de commits para una mayor claridad en el historial del repositorio.

- **Validación de Endpoints con Postman:** Se realizaron pruebas adicionales utilizando Postman para asegurar el correcto funcionamiento de los endpoints y verificar la interoperabilidad con la API.

## Estructura del Proyecto

- **Entidad:** `UserEntity` - Representa la entidad principal del sistema con campos como `firstName`, `lastName` y `email`.

- **Repositorio:** `UserRepository` - Interactúa con la base de datos y proporciona métodos para acceder a la entidad `UserEntity`.

- **Servicio:** `UserService` - Implementa la lógica de negocio y utiliza el repositorio para gestionar las operaciones CRUD.

- **Controlador:** `UserController` - Define los endpoints de la API, invoca métodos del servicio y gestiona las respuestas HTTP.

## Pruebas

- **Pruebas de Servicio:** Se realizaron pruebas exhaustivas para los métodos del servicio, cubriendo casos de uso correctos y situaciones donde se lanzan excepciones.

- **Pruebas de Repositorio:** Se validaron los métodos del repositorio y la conexión con la base de datos mediante pruebas de integración.

- **Pruebas de Controlador:** Se probaron los distintos casos de uso de los métodos del controlador, verificando la correcta manipulación de las solicitudes HTTP.

## Ejecución Local

1. **Clonar el Repositorio:**
   ```bash
   git clone https://github.com/tu-usuario/SimpleApiRest.git
   cd SimpleApiRest
2. **Ejecutar la Aplicación:**
   ```bash
   ./mvnw spring-boot:run
3. **Acceder a la API:**
   La API estará disponible en http://localhost:8080.

## Contribuciones
¡Contribuciones son bienvenidas! Si encuentras algún problema o tienes ideas para mejoras, no dudes en abrir un problema o enviar una solicitud de extracción.

## Licencia
Este proyecto está bajo la Licencia MIT - consulta el archivo LICENSE.md para más detalles.

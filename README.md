# GestiónPro

GestiónPro es una aplicación web integral diseñada para la gestión eficiente de cajeros, empleados, usuarios y productos en una entidad comercial. La aplicación proporciona una interfaz intuitiva que permite a los administradores controlar el acceso y realizar operaciones sobre los datos de manera organizada y segura.

## Objetivos

- 🛠️ Proveer una plataforma intuitiva y eficiente para la gestión de empleados, cajeros, usuarios y productos.
- 🔐 Facilitar la asignación y control de permisos y roles de los usuarios.
- 📧 Automatizar el envío de correos electrónicos para notificaciones importantes.
- 📊 Proveer acceso a la información a través de una interfaz clara y accesible.
- 💻 Asegurar la plataforma utilizando estándares de seguridad modernos.

## Tecnologías Utilizadas

- **Spring Boot**: Para el desarrollo del backend y la creación de servicios RESTful.
- **Thymeleaf**: Motor de plantillas para la generación de vistas dinámicas en el frontend.
- **PostgreSQL**: Base de datos relacional para la gestión de la información.
- **Maven**: Herramienta para la gestión de dependencias y construcción del proyecto.
- **Spring Security**: Sistema de seguridad para la autenticación y autorización de usuarios.
- **JWT (JSON Web Tokens)**: Para la autenticación segura basada en tokens.
- **JavaMailSender**: Biblioteca utilizada para el envío automatizado de correos electrónicos.
- **Bootstrap**: Framework para el diseño de una interfaz moderna y responsiva.
- **CSS Personalizado**: Ajustes adicionales de estilo con colores y diseño personalizados.

## Requisitos del Sistema

- **Java**: JDK 17 o superior
- **Spring Boot**: Versión 2.7 o superior
- **PostgreSQL**: Versión 12 o superior
- **Maven**: Herramienta de gestión de proyectos
- **Navegador**: Cualquier navegador moderno (Chrome, Firefox, Edge)

## Instalación

1. **Descargar el Proyecto**: 
   - Clonar el repositorio o descargar el archivo ZIP del código fuente.
   
2. **Configurar la Base de Datos**: 
   - Crear una base de datos en PostgreSQL llamada `gestionpro`.
   - Configurar las credenciales y la URL en el archivo `application.properties` o mediante variables de entorno.

3. **Configuración de Correos**:
   - Proveer las credenciales para el servicio de correo en `application.properties`, incluyendo el servidor SMTP, puerto, usuario y contraseña.

5. **Construcción del Proyecto**: 
   - Utilizar Maven para compilar el proyecto y resolver dependencias con el comando `mvn clean install`.

6. **Ejecutar la Aplicación**: 
   - Iniciar la aplicación desde la clase principal de Spring Boot (GestionProApplication.java).

7. **Acceso a la Aplicación**: 
   - Abrir el navegador y navegar a `http://localhost:8080` para acceder a la interfaz de usuario.

## Funcionalidades Principales

1. **Inicio de Sesión y Seguridad**:
   
   **Login Seguro**: 
   - Autenticación de usuarios mediante un formulario seguro que valida credenciales de acceso.

   **Autorización Basada en Roles**: 
   - Los usuarios son clasificados en diferentes roles (Administrador, Usuario) con distintos niveles de acceso.

   **Autenticación JWT**: 
   - Los usuarios reciben tokens JWT al iniciar sesión, que se utilizan para mantener la sesión de forma segura.

   **Protección con Spring Security**: 
   - Toda la aplicación está protegida contra accesos no autorizados y utiliza estándares modernos de seguridad para proteger las rutas de la API y los recursos sensibles.

2. **Gestión de Empleados**:
   
   **Agregar, Editar y Eliminar Empleados**
   - Los administradores pueden gestionar los datos de los empleados como nombre, apellido, DNI, correo electrónico y celular.
   - Búsqueda de Empleados: La funcionalidad de búsqueda permite encontrar empleados fácilmente por nombre o correo electrónico.
   - Permisos de Usuario: Solo los usuarios con los roles adecuados pueden realizar modificaciones sobre los empleados.

3. **Gestión de Cajeros**:
   - Asignación de Rol de Cajero: Se permite a los administradores asignar empleados al rol de cajero, con permisos específicos.
   - Edición de Cajeros: Se puede modificar el usuario, permisos y contraseña de un cajero.
   - Eliminación de Cajeros: Opción para eliminar cajeros del sistema de forma segura, siempre que se confirme la acción.

4. **Gestión de Usuarios**:
   - Registro y Edición de Usuarios: Se permite el registro de nuevos usuarios, la edición de sus roles y la eliminación cuando sea necesario.
   - Asignación de Roles: Los administradores pueden asignar roles específicos a los usuarios, como "Administrador" o "Usuario Básico".

5. **Gestión de Productos**:

   **Agregar, Editar y Eliminar Producto**
   - Gestión completa de los productos, incluyendo nombre, categoría, precio, stock y la imagen del producto.
   - Carga de Imágenes de Productos: Permite subir imágenes que se asocian a cada producto y se muestran en la lista de productos.
   - Búsqueda de Productos: Funcionalidad para buscar productos por nombre, categoría o stock.

6. **Envío Automático de Correos**:
   - Notificaciones de Usuario: Los usuarios reciben notificaciones automáticas por correo en eventos importantes, como el registro exitoso o actualizaciones importantes en sus cuentas.
   - Soporte: La aplicación envía correos de soporte automatizados a los usuarios cuando ocurre algún error importante o solicitud de ayuda.


7. **Buscar Elementos**:
   - Utilizar las barras de búsqueda disponibles en cada gestión.
  
## Instrucciones de Uso

1. **Inicio de Sesión**:
   - Ingresar al sistema con las credenciales de un usuario con permisos adecuados (Administrador o Usuario).

2. **Acceder a las Gestiones**:
   - Desde el Panel de Control, se puede acceder a las distintas áreas de gestión, como empleados, cajeros, usuarios o productos.

3. **Operaciones CRUD**:
   - En cada módulo de gestión, se puede Agregar, Editar o Eliminar entradas como empleados, cajeros, usuarios o productos, utilizando los botones y formularios disponibles.

4. **Subida de Imágenes en Productos**:
   - Al gestionar productos, se pueden agregar imágenes que se visualizarán directamente en la lista de productos.

5. **Notificaciones por Correo**:
   - Los usuarios reciben correos automáticos tras registrarse o al modificar sus datos. Configurar correctamente los detalles del servidor de correo en `application.properties`.

## Soporte de la Aplicación

Si tienes alguna pregunta o necesitas ayuda, puedes contactarnos a través del siguiente correo electrónico: [funesapps.soporte@gmail.com](mailto:funesapps.soporte@gmail.com).

## Repositorio de GitHub

Puedes encontrar el código fuente de la aplicación en el siguiente enlace: [Repositorio de GitHub - GestiónPro](https://github.com/EmiFunes91/adm-empleados).

## Conclusión

GestiónPro es una solución robusta, segura y eficiente para la gestión de empleados, cajeros, usuarios y productos en cualquier entidad comercial. La aplicación ofrece una experiencia de usuario intuitiva y está diseñada para ayudar a las organizaciones a gestionar sus operaciones de manera eficaz.

## Recomendaciones

- 🔧 **Mantenimiento Regular**: Realizar mantenimientos periódicos de la base de datos y la aplicación para garantizar su correcto funcionamiento.
- 📚 **Capacitación de Usuarios**: Proporcionar formación a los usuarios sobre las funcionalidades de la aplicación para maximizar su eficiencia.
- 🔒 **Actualización de Seguridad**: Mantener actualizadas las dependencias y tecnologías utilizadas para garantizar la protección de la aplicación contra vulnerabilidades.

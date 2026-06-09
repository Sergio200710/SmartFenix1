# SmartFenix API

> API REST construida con **Spring Boot 3.2.5** para la gestión integral de proyectos, tareas, empleados y clientes.

---

## Índice

1. [Descripción del proyecto](#-descripción-del-proyecto)
2. [Tecnologías utilizadas](#-tecnologías-utilizadas)
3. [Requisitos previos](#-requisitos-previos)
4. [Configuración del entorno](#-configuración-del-entorno)
5. [Levantar el entorno Docker](#-levantar-el-entorno-docker)
6. [Arrancar la aplicación desde IntelliJ](#-arrancar-la-aplicación-desde-intellij)
7. [URLs de acceso](#-urls-de-acceso)
8. [Configuración de application.properties](#-configuración-de-applicationproperties)
9. [Estructura del proyecto](#-estructura-del-proyecto)
10. [Endpoints de la API](#-endpoints-de-la-api)

---

## Descripción del proyecto

**SmartFenix** es una API REST desarrollada con Spring Boot que permite gestionar de forma centralizada:

-**Clientes** — Registro y gestión de clientes de la empresa
-**Empleados** — Control del personal y sus asignaciones
-**Proyectos** — Seguimiento del ciclo de vida de los proyectos
-**Tareas** — Gestión de tareas asociadas a proyectos y empleados

La aplicación utiliza **MySQL** como base de datos relacional, orquestada mediante **Docker Compose**, y expone una API REST con arquitectura en capas (Controller → Service → Repository → Domain).

---

## Tecnologías utilizadas

| Tecnología               | Versión     | Uso                              |
|--------------------------|-------------|----------------------------------|
| Java                     | 21          | Lenguaje de programación         |
| Spring Boot              | 3.2.5       | Framework principal              |
| Spring Data JPA          | —           | Acceso a datos / ORM             |
| Spring Validation        | —           | Validación de entidades          |
| Hibernate                | —           | Dialecto MySQL                   |
| MySQL                    | 8.0         | Base de datos relacional         |
| Lombok                   | 1.18.32     | Reducción de boilerplate         |
| Docker / Docker Compose  | —           | Contenedores (MySQL + phpMyAdmin)|
| Maven                    | —           | Gestión de dependencias y build  |

---

## Requisitos previos

Antes de arrancar el proyecto, asegúrate de tener instalado lo siguiente:

### 1. Docker Desktop / Docker Engine
- Descarga: [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop)
- Verifica la instalación:
  ```bash
  docker --version
  docker compose version
  ```

### 2. Java Development Kit (JDK) 21
- Descarga: [https://adoptium.net/es/temurin/releases/](https://adoptium.net/es/temurin/releases/)
- Verifica la instalación:
  ```bash
  java -version
  ```
  Salida esperada: `openjdk version "21.x.x"`

### 3. IntelliJ IDEA
- Descarga: [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
- Se recomienda la versión **Community** o **Ultimate**
- Plugin recomendado: **Lombok** (normalmente ya viene preinstalado)

---

## Configuración del entorno

### Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd SmartFenix
```

### Verificar el archivo `compose.yaml`

El archivo `compose.yaml` en la raíz del proyecto define los servicios Docker necesarios:

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    container_name: mysql_server
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: smartfenix_db
    ports:
      - "3308:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  phpmyadmin:
    image: phpmyadmin/phpmyadmin:latest
    container_name: phpmyadmin
    restart: always
    environment:
      PMA_HOST: mysql
      PMA_PORT: 3306
      MYSQL_ROOT_PASSWORD: root
    ports:
      - "8090:80"
    depends_on:
      - mysql

volumes:
  mysql_data:
```

---

## Levantar el entorno Docker

Desde la **raíz del proyecto** (donde se encuentra el archivo `compose.yaml`), ejecuta:

```bash
docker compose up -d
```

Este comando:
1. Descarga las imágenes de **MySQL 8.0** y **phpMyAdmin** si no las tienes en local
2. Crea y arranca los contenedores en segundo plano (`-d` = detached)
3. Crea automáticamente la base de datos `smartfenix_db`
4. Expone MySQL en el puerto `3308` y phpMyAdmin en el puerto `8090`

### Verificar que los contenedores están corriendo

```bash
docker ps
```

Deberías ver algo similar a:

```
CONTAINER ID   IMAGE                          PORTS                    NAMES
xxxxxxxxxxxx   mysql:8.0                      0.0.0.0:3308->3306/tcp   mysql_server
xxxxxxxxxxxx   phpmyadmin/phpmyadmin:latest   0.0.0.0:8090->80/tcp     phpmyadmin
```

### Parar los contenedores

```bash
docker compose down
```

### Parar los contenedores y eliminar los datos

```bash
docker compose down -v
```

> **Precaución:** El flag `-v` elimina el volumen `mysql_data` y con él todos los datos de la base de datos.

---

## Arrancar la aplicación desde IntelliJ

### Paso 1 — Abrir el proyecto

1. Abre **IntelliJ IDEA**
2. Selecciona **File → Open...**
3. Navega hasta la carpeta `SmartFenix` y haz clic en **OK / Abrir**
4. Espera a que Maven descargue todas las dependencias (la barra de progreso aparece en la parte inferior)

### Paso 2 — Verificar la configuración de JDK

1. Ve a **File → Project Structure → Project**
2. Asegúrate de que el **SDK** está configurado en **Java 21**
3. Si no aparece, haz clic en el desplegable y selecciona **Add SDK → JDK** apuntando a tu instalación

### Paso 3 — Habilitar el procesador de anotaciones (Lombok)

1. Ve a **File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors**
2. Marca la casilla **Enable annotation processing**
3. Haz clic en **Apply → OK**

### Paso 4 — Asegúrate de que Docker está corriendo

Antes de arrancar la aplicación, los contenedores de Docker **deben estar activos**. Si no los has levantado aún:

```bash
docker compose up -d
```

### Paso 5 — Ejecutar la aplicación

**Opción A — Desde el panel de archivos:**
1. Navega en el árbol de proyecto hasta `src/main/java/com/smartfenix/SmartFenixApplication.java`
2. Haz clic derecho sobre el archivo → **Run 'SmartFenixApplication'**

**Opción B — Desde la barra de herramientas:**
1. En la esquina superior derecha, selecciona la configuración **SmartFenixApplication**
2. Haz clic en el botón**Run**

**Opción C — Desde el terminal de IntelliJ:**
```bash
./mvnw spring-boot:run
```

### Paso 6 — Verificar el arranque

La aplicación estará lista cuando veas en la consola:

```
Started SmartFenixApplication in X.XXX seconds (process running for X.XXX)
```

---

## URLs de acceso

| Servicio       | URL                              | Descripción                          |
|----------------|----------------------------------|--------------------------------------|
| API REST    | http://localhost:8099            | Aplicación Spring Boot principal     |
| phpMyAdmin  | http://localhost:8090            | Interfaz web de gestión de MySQL     |

### Acceso a phpMyAdmin

1. Abre el navegador y ve a: **http://localhost:8090**
2. Introduce las credenciales:
   - **Usuario:** `root`
   - **Contraseña:** `root`
3. Una vez dentro, verás la base de datos **`smartfenix_db`** en el panel lateral izquierdo

---

## Verificación y pruebas

| Caso | Qué se prueba | Datos de entrada | Resultado esperado |
|------|---------------|------------------|--------------------|
| Caso 1: Alta de cliente | Crear un cliente desde la aplicación | Nombre, empresa y teléfono del cliente | El cliente se guarda correctamente y aparece en el listado |
| Caso 2: Consulta de clientes | Consultar los clientes almacenados | Petición `GET /api/clientes` o acceso al listado | Se muestra la lista de clientes registrados |
| Caso 3: Modificación de proyecto | Editar los datos de un proyecto existente | Nuevo nombre y fechas del proyecto | El proyecto queda actualizado correctamente |
| Caso 4: Eliminación de empleado | Eliminar un registro existente | ID de un empleado existente | El registro se elimina y deja de estar disponible |
| Caso 5: Consulta de registro inexistente | Consultar un ID que no existe | Petición `GET /api/empleados/9999` | La aplicación responde de forma controlada con `404 Not Found` |

## Pruebas unitarias implementadas

Se han creado pruebas unitarias sobre servicios usando **JUnit 5** y **Mockito**, sin levantar toda la aplicación y sin usar base de datos real.

- `ClienteServiceTest`: crear cliente, listar clientes y eliminar cliente.
- `EmpleadoServiceTest`: crear empleado, listar empleados y eliminar empleado.
- `ProyectoServiceTest`: crear proyecto, listar proyectos y modificar proyecto.

Estas pruebas usan `@ExtendWith(MockitoExtension.class)`, `@Mock` para los repositorios y `@InjectMocks` para los servicios.

## Prueba de integración implementada

Se ha implementado una prueba de integración en `EmpleadoControllerIntegrationTest` con:

- `@SpringBootTest`
- `@AutoConfigureMockMvc`
- `@ActiveProfiles("test")`
- `MockMvc`
- base de datos **H2 en memoria**

En esta prueba se comprueban endpoints REST reales de empleados, incluyendo:

- `GET /api/empleados`
- `POST /api/empleados`
- `GET /api/empleados/9999`

## Cómo ejecutar las pruebas

Desde la raíz del proyecto ejecuta:

```bash
mvn test
```

Resumen de ejecución:

- Las pruebas unitarias no usan base de datos real.
- Las pruebas unitarias usan Mockito.
- La prueba de integración usa H2 en memoria con el perfil `test`.
- No hace falta levantar Docker ni MySQL/MariaDB para ejecutar los tests.

## Depuración

Para la demo se puede colocar un breakpoint en el método `ProyectoService.update(...)` o en otro método real de servicio como `ClienteService.save(...)`.

1. Ejecutar la aplicación o un test en modo **Debug** desde IntelliJ IDEA o Eclipse.
2. Crear o editar un cliente/proyecto desde la aplicación o desde una prueba.
3. Comprobar que el programa se detiene en el breakpoint.
4. Inspeccionar variables como `id`, `cliente`, `proyecto`, `nombre`, `email` o `estado`.
5. Avanzar con **Step Over** para ver cómo se ejecuta la lógica.
6. Continuar la ejecución y comprobar que el dato se guarda o actualiza correctamente.

## Resultados obtenidos

Con esta adaptación, el proyecto queda preparado para la actividad de verificación y pruebas con:

- pruebas unitarias de servicios desacopladas de la base de datos real
- una prueba de integración de varias capas con Spring Boot, MockMvc y H2
- casos de prueba documentados en este `README.md`
- guía básica de depuración para la demostración en vídeo

---

## Interfaz web

La aplicación incluye una interfaz web con Thymeleaf para gestionar desde el navegador las entidades principales del sistema.

- URL principal: `http://localhost:8099`
- Rutas disponibles:
  - `/clientes`
  - `/empleados`
  - `/proyectos`
  - `/tareas`

Desde esta interfaz se pueden crear, consultar, modificar y eliminar registros de clientes, empleados, proyectos y tareas, además de navegar desde un panel principal.

---

## Configuración de `application.properties`

El archivo de configuración se encuentra en:

```
src/main/resources/application.properties
```

Contenido actual:

```properties
# =============================================
# DATASOURCE — Conexión con MySQL (Docker)
# =============================================
spring.datasource.url=jdbc:mysql://localhost:3308/smartfenix_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# =============================================
# JPA / HIBERNATE
# =============================================
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# =============================================
# SERVIDOR
# =============================================
server.port=8099

# =============================================
# DEBUG
# =============================================
debug=true
```

### Descripción de los parámetros clave

| Parámetro                        | Valor                   | Descripción                                                  |
|----------------------------------|-------------------------|--------------------------------------------------------------|
| `spring.datasource.url`          | `...localhost:3308/...` | Conexión a MySQL en el puerto mapeado por Docker             |
| `spring.datasource.username`     | `root`                  | Usuario de MySQL                                             |
| `spring.datasource.password`     | `root`                  | Contraseña de MySQL                                          |
| `spring.jpa.hibernate.ddl-auto` | `create`                | Crea las tablas al arrancar ( borra datos previos)         |
| `spring.jpa.show-sql`            | `true`                  | Muestra las consultas SQL en consola                         |
| `server.port`                    | `8099`                  | Puerto en el que escucha la aplicación                       |

> **Nota:** El valor `ddl-auto=create` es útil en desarrollo pero **elimina y recrea las tablas** cada vez que la app arranca. En entornos de producción, cambia este valor a `validate` o `none`.

---

## Estructura del proyecto

```
SmartFenix/
├── compose.yaml                          # Orquestación Docker (MySQL + phpMyAdmin)
├── pom.xml                               # Dependencias y configuración Maven
├── README.md                             # Este archivo
│
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── smartfenix/
        │           ├── SmartFenixApplication.java        # Clase principal (punto de entrada)
        │           ├── DataLoader.java                   # Carga inicial de datos de prueba
        │           │
        │           ├── controller/                       # Capa de exposición REST
        │           │   ├── ClienteController.java
        │           │   ├── EmpleadoController.java
        │           │   ├── ProyectoController.java
        │           │   └── TareaController.java
        │           │
        │           ├── domain/                           # Entidades JPA (modelo de datos)
        │           │   ├── Cliente.java
        │           │   ├── Empleado.java
        │           │   ├── Proyecto.java
        │           │   └── Tarea.java
        │           │
        │           ├── repository/                       # Capa de acceso a datos (Spring Data JPA)
        │           │   ├── ClienteRepository.java
        │           │   ├── EmpleadoRepository.java
        │           │   ├── ProyectoRepository.java
        │           │   └── TareaRepository.java
        │           │
        │           └── service/                          # Capa de lógica de negocio
        │               ├── ClienteService.java
        │               ├── EmpleadoService.java
        │               ├── ProyectoService.java
        │               └── TareaService.java
        │
        └── resources/
            └── application.properties                    # Configuración de la aplicación
```

### Descripción de las capas

| Capa           | Paquete        | Responsabilidad                                              |
|----------------|----------------|--------------------------------------------------------------|
| **Controller** | `controller/`  | Recibe peticiones HTTP y devuelve respuestas JSON            |
| **Service**    | `service/`     | Contiene la lógica de negocio y orquesta operaciones         |
| **Repository** | `repository/`  | Acceso a la base de datos mediante Spring Data JPA           |
| **Domain**     | `domain/`      | Definición de las entidades mapeadas a tablas MySQL          |

---

## Endpoints de la API

La API base está disponible en: `http://localhost:8099`

### Clientes — `/api/clientes`

| Método | Endpoint              | Descripción                    |
|--------|-----------------------|--------------------------------|
| GET    | `/api/clientes`       | Listar todos los clientes      |
| GET    | `/api/clientes/{id}`  | Obtener un cliente por ID      |
| POST   | `/api/clientes`       | Crear un nuevo cliente         |
| PUT    | `/api/clientes/{id}`  | Actualizar un cliente          |
| DELETE | `/api/clientes/{id}`  | Eliminar un cliente            |

### Empleados — `/api/empleados`

| Método | Endpoint               | Descripción                    |
|--------|------------------------|--------------------------------|
| GET    | `/api/empleados`       | Listar todos los empleados     |
| GET    | `/api/empleados/{id}`  | Obtener un empleado por ID     |
| POST   | `/api/empleados`       | Crear un nuevo empleado        |
| PUT    | `/api/empleados/{id}`  | Actualizar un empleado         |
| DELETE | `/api/empleados/{id}`  | Eliminar un empleado           |

### Proyectos — `/api/proyectos`

| Método | Endpoint               | Descripción                    |
|--------|------------------------|--------------------------------|
| GET    | `/api/proyectos`       | Listar todos los proyectos     |
| GET    | `/api/proyectos/{id}`  | Obtener un proyecto por ID     |
| POST   | `/api/proyectos`       | Crear un nuevo proyecto        |
| PUT    | `/api/proyectos/{id}`  | Actualizar un proyecto         |
| DELETE | `/api/proyectos/{id}`  | Eliminar un proyecto           |

### Tareas — `/api/tareas`

| Método | Endpoint           | Descripción                    |
|--------|--------------------|--------------------------------|
| GET    | `/api/tareas`      | Listar todas las tareas        |
| GET    | `/api/tareas/{id}` | Obtener una tarea por ID       |
| POST   | `/api/tareas`      | Crear una nueva tarea          |
| PUT    | `/api/tareas/{id}` | Actualizar una tarea           |
| DELETE | `/api/tareas/{id}` | Eliminar una tarea             |

---

## Solución de problemas comunes

### `Communications link failure` al arrancar

**Causa:** MySQL no está corriendo o no es accesible en el puerto `3308`.

**Solución:**
```bash
docker compose up -d
docker ps  # Verificar que mysql_server está en estado "Up"
```

### `Access denied for user 'root'@'localhost'`

**Causa:** Las credenciales no coinciden.

**Solución:** Verifica que en `application.properties` las credenciales son `root` / `root`, y que el contenedor MySQL usa las mismas.

### Los datos desaparecen al reiniciar la app

**Causa:** `spring.jpa.hibernate.ddl-auto=create` recrea las tablas en cada arranque.

**Solución:** Cambia el valor a `update` para persistir los datos entre reinicios:
```properties
spring.jpa.hibernate.ddl-auto=update
```

### Puerto `8099` en uso

**Causa:** Otro proceso está usando ese puerto.

**Solución:**
```bash
# Encontrar el proceso que usa el puerto
sudo lsof -i :8099
# Matarlo
kill -9 <PID>
```

---

##  Autor

**SmartFenix API** — Proyecto de gestión empresarial con Spring Boot 3.2.5

---

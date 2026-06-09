# MEMORIA DEL PROYECTO SMARTFENIX

## Verificación y pruebas de una aplicación Spring Boot

### 1. Portada

**Proyecto:** SmartFenix  
**Actividad:** Verificación y pruebas de una aplicación Spring Boot  
**Asignatura:** Entornos de Desarrollo  
**Resultado de aprendizaje:** RA3 - Verifica el funcionamiento de programas diseñando y realizando pruebas  
**Alumno:** Sergio Hidalgo López  
**Curso:** 1º DAM  
**Fecha:** Junio 2026

---

### 2. Índice

1. Resumen del proyecto  
2. Tecnologías utilizadas  
3. Arquitectura del sistema  
4. Entidades principales  
5. Configuración del entorno  
6. Interfaz web y API REST  
7. Casos de prueba  
8. Pruebas unitarias  
9. Prueba de integración  
10. Depuración del sistema  
11. Ejecución de pruebas y resultados  
12. Incidencias encontradas y soluciones  
13. Conclusión

---

### 3. Resumen del proyecto

SmartFenix es una aplicación desarrollada con Spring Boot para gestionar clientes, empleados, proyectos y tareas de forma centralizada. La idea principal del proyecto es reunir en una sola aplicación información que normalmente estaría repartida en distintos sitios, de manera que sea más fácil consultarla, modificarla y comprobar su estado.

El proyecto resuelve el problema de llevar el control de datos empresariales de forma desordenada. En lugar de depender de herramientas separadas, SmartFenix permite centralizar:

- clientes o empresas con las que se trabaja
- empleados internos y su rol
- proyectos asociados a un cliente
- tareas asignadas a proyectos y empleados

Desde la aplicación, el usuario puede:

- crear registros nuevos
- consultar información guardada
- modificar datos existentes
- eliminar registros cuando sea necesario

También es importante diferenciar sus dos formas de uso:

- la **interfaz web con Thymeleaf** está pensada para trabajar desde el navegador
- la **API REST** está pensada para realizar peticiones HTTP desde herramientas como Postman o desde pruebas automáticas

---

### 4. Tecnologías utilizadas

| Tecnología | Versión | Explicación |
| --- | --- | --- |
| Java | 21 | Lenguaje principal del proyecto |
| Spring Boot | 3.2.5 | Permite crear la aplicación de forma rápida y organizada |
| Maven | - | Gestiona dependencias, compilación y pruebas |
| Spring Data JPA | - | Facilita el acceso a datos mediante repositorios |
| Spring Validation | - | Ayuda a validar formularios y peticiones |
| Lombok | 1.18.32 | Reduce código repetitivo |
| MySQL | 8.0 | Base de datos real usada por la aplicación |
| Docker Compose | - | Levanta MySQL y phpMyAdmin |
| phpMyAdmin | - | Permite visualizar la base de datos |
| Thymeleaf | - | Se usa para la interfaz web |
| JUnit 5 | - | Framework principal de pruebas |
| Mockito | - | Simula repositorios en unitarias |
| MockMvc | - | Simula peticiones HTTP en integración |
| H2 | - | Base de datos en memoria para integración |

---

### 5. Arquitectura del sistema

SmartFenix sigue una arquitectura por capas para separar responsabilidades y hacer que el proyecto sea más fácil de mantener, probar y explicar.

#### 5.1 Capa Domain

La capa **Domain** contiene las entidades JPA del sistema. Estas clases representan la estructura principal de los datos y están relacionadas con las tablas de la base de datos.

#### 5.2 Capa Repository

La capa **Repository** contiene interfaces que extienden `JpaRepository`. Gracias a esto, se pueden realizar operaciones como guardar, buscar, listar o eliminar registros sin escribir manualmente todas las consultas básicas.

#### 5.3 Capa Service

La capa **Service** contiene la lógica de negocio. Su función es evitar que el controlador acceda directamente al repositorio y concentrar en un punto intermedio las operaciones principales del sistema.

#### 5.4 Capa Controller

La capa **Controller** recibe peticiones HTTP. En este proyecto hay dos tipos de controlador:

- controladores REST, que devuelven respuestas JSON
- controladores web, que devuelven vistas Thymeleaf

#### 5.5 Flujo general

**Usuario / Navegador / Postman -> Controller -> Service -> Repository -> Base de datos MySQL**

En las pruebas unitarias, la capa `Repository` se simula con Mockito para no usar base de datos real.

---

### 6. Entidades principales

| Entidad | Campos principales | Función |
| --- | --- | --- |
| Cliente | nombre, empresa, telefono | Representa una empresa o persona que solicita trabajos |
| Empleado | nombre, email, rol | Representa trabajadores internos |
| Proyecto | nombre, fechaInicio, fechaFin, cliente | Representa trabajos o proyectos asociados a un cliente |
| Tarea | descripcion, estado, proyecto, empleado | Representa trabajos concretos dentro de un proyecto |

#### 6.1 Cliente

Representa una empresa o persona con la que se trabaja. Guarda datos como nombre, empresa y teléfono.

#### 6.2 Empleado

Representa trabajadores internos de SmartFenix. Guarda nombre, email y rol.

#### 6.3 Proyecto

Representa un trabajo o proyecto asociado a un cliente. Guarda su nombre, fechas y relación con el cliente correspondiente.

#### 6.4 Tarea

Representa un trabajo concreto dentro de un proyecto. Incluye descripción, estado, proyecto asociado y empleado asignado.

---

### 7. Configuración del entorno

Para trabajar con el proyecto se utiliza la siguiente configuración:

- **Java 21** para compilar y ejecutar la aplicación
- **Maven** para gestionar dependencias y ejecutar comandos como `mvn test` o `mvn spring-boot:run`
- **Docker Compose** para levantar MySQL y phpMyAdmin
- **MySQL real** para la ejecución normal de la aplicación
- **H2** solo para la prueba de integración
- **phpMyAdmin** para visualizar la base de datos desde el navegador

Configuración oficial:

- MySQL: `localhost:3308`
- phpMyAdmin: `http://localhost:8090`
- usuario MySQL: `root`
- contraseña MySQL: `root`
- aplicación Spring Boot: `http://localhost:8099`

#### 7.1 Comandos principales

```bash
docker compose up -d
docker compose ps
mvn test
mvn spring-boot:run
```

Esta separación es importante porque la aplicación real usa MySQL en Docker, pero la verificación automatizada no depende de ese entorno.

---

### 8. Interfaz web y API REST

#### 8.1 Interfaz web

La interfaz web permite usar la aplicación directamente desde el navegador. Desde la web se pueden gestionar:

- clientes
- empleados
- proyectos
- tareas

Cada sección permite trabajar con operaciones de listado, creación, edición y eliminación de registros.

Rutas principales:

- `/`
- `/dashboard`
- `/clientes`
- `/empleados`
- `/proyectos`
- `/tareas`

#### 8.2 API REST

La API REST permite interactuar con la aplicación mediante peticiones HTTP. Es útil para probar manualmente con Postman o para automatizar verificaciones con herramientas como MockMvc.

Cada entidad tiene operaciones CRUD:

- `GET` para consultar datos
- `POST` para crear datos
- `PUT` para actualizar datos
- `DELETE` para eliminar datos

| Método | Endpoint | Descripción |
| --- | --- | --- |
| `GET` | `/api/clientes` | Listar clientes |
| `GET` | `/api/clientes/{id}` | Obtener cliente por id |
| `POST` | `/api/clientes` | Crear cliente |
| `PUT` | `/api/clientes/{id}` | Actualizar cliente |
| `DELETE` | `/api/clientes/{id}` | Eliminar cliente |
| `GET` | `/api/empleados` | Listar empleados |
| `GET` | `/api/empleados/{id}` | Obtener empleado por id |
| `POST` | `/api/empleados` | Crear empleado |
| `PUT` | `/api/empleados/{id}` | Actualizar empleado |
| `DELETE` | `/api/empleados/{id}` | Eliminar empleado |
| `GET` | `/api/proyectos` | Listar proyectos |
| `GET` | `/api/proyectos/{id}` | Obtener proyecto por id |
| `POST` | `/api/proyectos` | Crear proyecto |
| `PUT` | `/api/proyectos/{id}` | Actualizar proyecto |
| `DELETE` | `/api/proyectos/{id}` | Eliminar proyecto |
| `GET` | `/api/tareas` | Listar tareas |
| `GET` | `/api/tareas/{id}` | Obtener tarea por id |
| `POST` | `/api/tareas` | Crear tarea |
| `PUT` | `/api/tareas/{id}` | Actualizar tarea |
| `DELETE` | `/api/tareas/{id}` | Eliminar tarea |

---

### 9. Casos de prueba

Este apartado cumple la parte del enunciado que pide definir al menos cinco casos de prueba, indicando qué se prueba, qué datos se usan y cuál es el resultado esperado.

| Caso | Qué se prueba | Datos de entrada | Resultado esperado |
| --- | --- | --- | --- |
| Alta de cliente | Crear un cliente | Nombre, empresa y teléfono | El cliente se guarda y aparece en el listado |
| Consulta de clientes | Consultar clientes existentes | GET `/api/clientes` o ruta `/clientes` | Se muestra la lista de clientes |
| Modificación de proyecto | Editar un proyecto | Nuevo nombre o fechas | El proyecto queda actualizado |
| Eliminación de empleado | Eliminar un empleado existente | ID de empleado | El empleado desaparece del sistema |
| Consulta de registro inexistente | Consultar un ID inexistente | GET `/api/empleados/9999` | Respuesta `404 Not Found` |

#### 9.1 Alta de cliente

- Objetivo: comprobar que se puede registrar un cliente nuevo.
- Datos usados: nombre, empresa y teléfono.
- Pasos de ejecución: abrir el formulario o lanzar la petición de creación, guardar y revisar el listado.
- Resultado esperado: el cliente queda almacenado correctamente.
- Importancia: valida una operación básica del sistema.

#### 9.2 Consulta de clientes

- Objetivo: comprobar que los clientes ya guardados pueden consultarse.
- Datos usados: listado actual de clientes.
- Pasos de ejecución: acceder a `/clientes` o hacer `GET /api/clientes`.
- Resultado esperado: se muestra la información correctamente.
- Importancia: valida la lectura de datos.

#### 9.3 Modificación de proyecto

- Objetivo: comprobar que un proyecto puede actualizarse.
- Datos usados: nuevo nombre o nuevas fechas.
- Pasos de ejecución: editar proyecto, guardar cambios y revisar resultado.
- Resultado esperado: el proyecto queda actualizado.
- Importancia: valida que la edición funciona.

#### 9.4 Eliminación de empleado

- Objetivo: comprobar que un empleado puede eliminarse.
- Datos usados: identificador del empleado.
- Pasos de ejecución: seleccionar el empleado, eliminarlo y revisar el listado.
- Resultado esperado: el empleado desaparece del sistema.
- Importancia: valida el borrado de registros.

#### 9.5 Consulta de registro inexistente

- Objetivo: comprobar la respuesta ante un identificador no válido.
- Datos usados: `GET /api/empleados/9999`.
- Pasos de ejecución: lanzar la petición con un ID inexistente.
- Resultado esperado: respuesta `404 Not Found`.
- Importancia: valida el tratamiento correcto de errores.

---

### 10. Pruebas unitarias

Una prueba unitaria sirve para comprobar una parte pequeña del código de forma aislada. En este proyecto se prueban los servicios porque es en esa capa donde se encuentra la lógica de negocio principal.

No se usa base de datos real en estas pruebas porque eso las haría más lentas y más dependientes del entorno. En su lugar, se usa **Mockito**, que permite simular los repositorios y comprobar si el servicio hace la llamada correcta.

Configuración usada:

- `JUnit 5`
- `Mockito`
- `@ExtendWith(MockitoExtension.class)`
- `@Mock`
- `@InjectMocks`

Estas pruebas unitarias:

- no usan base de datos real
- no usan MySQL
- no usan Docker
- no usan `@SpringBootTest`

| Clase de test | Métodos comprobados | Qué verifica |
| --- | --- | --- |
| `ClienteServiceTest` | `testCrearCliente`, `testListarClientes`, `testEliminarCliente` | Guardado, listado y borrado de clientes |
| `EmpleadoServiceTest` | `testCrearEmpleado`, `testListarEmpleados`, `testEliminarEmpleado` | Guardado, listado y borrado de empleados |
| `ProyectoServiceTest` | `testCrearProyecto`, `testListarProyectos`, `testActualizarProyecto` | Creación, consulta y actualización de proyectos |

Estas pruebas comprueban la lógica de servicio y la llamada correcta al repositorio simulado.

Esquema de testing unitario:

**Service -> Repository simulado con Mockito**

---

### 11. Prueba de integración

Una prueba de integración comprueba que varias capas del sistema funcionan juntas. No se limita a un único método aislado, sino que revisa el comportamiento del flujo completo entre controlador, servicio, repositorio y base de datos de pruebas.

La clase de integración implementada es:

- `EmpleadoControllerIntegrationTest`

Herramientas y anotaciones usadas:

- `Spring Boot Test`
- `MockMvc`
- `@SpringBootTest`
- `@AutoConfigureMockMvc`
- `@ActiveProfiles("test")`
- `H2 en memoria`

| Prueba | Qué comprueba |
| --- | --- |
| `testListarEmpleadosDevuelveOk` | Que `GET /api/empleados` responde correctamente |
| `testCrearEmpleadoFlujoCompleto` | Que se puede crear un empleado mediante `POST` |
| `testConsultarEmpleadoInexistenteDevuelveNotFound` | Que un ID inexistente devuelve `404` |

Flujo de integración:

**MockMvc -> Controller -> Service -> Repository -> H2 en memoria**

Esta prueba no depende de MySQL real ni de Docker.

---

### 12. Depuración del sistema

Depurar significa ejecutar el programa paso a paso para observar qué está ocurriendo internamente. Esto ayuda a entender mejor el flujo del código y a detectar errores de lógica.

Un **breakpoint** es un punto de parada que hace que la ejecución se detenga en una línea concreta. Desde ahí se pueden observar valores, objetos y cambios durante el proceso.

**Step Over** permite avanzar línea a línea sin entrar en todos los métodos internos.

Inspeccionar variables sirve para comprobar cómo cambian los datos a medida que se ejecuta el código.

Métodos recomendados para depurar:

- `ClienteService.save(...)`
- `ProyectoService.update(...)`
- `EmpleadoService.update(...)`

Pasos básicos:

1. abrir el proyecto en IntelliJ
2. colocar un breakpoint en un método de servicio
3. arrancar la aplicación o un test en modo Debug
4. avanzar con Step Over
5. inspeccionar variables
6. comprobar cómo cambia el flujo

---

### 13. Ejecución de pruebas y resultados

Las pruebas pueden ejecutarse desde terminal o desde IntelliJ IDEA.

Desde terminal:

```bash
mvn test
```

Desde IntelliJ:

1. abrir `src/test/java`
2. elegir una clase de test o toda la carpeta
3. usar `Run` o `Debug`

Resultado validado:

```text
BUILD SUCCESS
Tests run: 18
Failures: 0
Errors: 0
Skipped: 0
```

Esto significa que:

- la compilación ha terminado bien
- no hay fallos de aserciones
- no hay errores de ejecución
- toda la suite de pruebas prevista ha pasado correctamente

Además de la suite automática, se realizó una comprobación funcional completa de la aplicación arrancando SmartFenix con:

```bash
mvn spring-boot:run
```

Durante esta verificación se confirmó que la aplicación arranca en `http://localhost:8099` y que responde correctamente en las rutas principales:

- `/`
- `/dashboard`
- `/clientes`
- `/empleados`
- `/proyectos`
- `/tareas`
- `/api/clientes`
- `/api/empleados`
- `/api/proyectos`
- `/api/tareas`

Todas estas rutas devolvieron respuesta correcta, sin errores 500, una vez aplicada la corrección final de serialización en la API REST.

Resumen de resultados obtenidos:

- las rutas web responden correctamente
- la API REST responde correctamente
- `mvn test` finaliza con `BUILD SUCCESS`
- la eliminación de registros relacionados está controlada
- no se producen errores 500 al eliminar registros con dependencias
- la aplicación queda lista para una demostración funcional

---

### 14. Incidencias encontradas y soluciones

| Incidencia | Causa | Solución |
| --- | --- | --- |
| Documentación antigua contradictoria | Existían referencias antiguas en la memoria y en otros textos del proyecto | Revisar la documentación y dejar una única versión coherente |
| Confusión entre MySQL real y H2 | Podía mezclarse la base de datos de la aplicación con la de pruebas | Aclarar que MySQL es para la app real y H2 para integración |
| Eliminación directa de registros relacionados | Algunas entidades no podían borrarse si tenían relaciones activas por claves foráneas | Comprobar dependencias antes de eliminar y devolver mensajes controlados |
| Error 500 en `/api/proyectos` y `/api/tareas` | Jackson intentaba serializar proxies lazy de Hibernate | Ignorar metadatos internos de Hibernate en las entidades para devolver JSON válido |

#### 14.1 Eliminación controlada de registros relacionados

Durante las pruebas se detectó que algunos registros no podían eliminarse directamente porque estaban relacionados con otras entidades del sistema. Este comportamiento es correcto desde el punto de vista de la base de datos, ya que las claves foráneas protegen la integridad de la información y evitan dejar datos huérfanos.

Ejemplos comprobados:

- un Cliente no puede eliminarse si tiene Proyectos asociados
- un Empleado no puede eliminarse si tiene Tareas asignadas
- un Proyecto no puede eliminarse si tiene Tareas asociadas

La solución aplicada ha sido controlar la eliminación antes de ejecutarla:

- comprobar si existen registros relacionados
- bloquear la eliminación si existen dependencias
- mostrar un mensaje claro en la interfaz web
- devolver una respuesta controlada en la API REST
- evitar errores 500 y páginas de error no controladas

Tabla de eliminación controlada:

| Registro | Dependencia revisada | Resultado si tiene dependencia | Resultado si no tiene dependencia |
| --- | --- | --- | --- |
| Cliente | Proyectos asociados | No se elimina y se muestra mensaje | Se elimina correctamente |
| Empleado | Tareas asignadas | No se elimina y se muestra mensaje | Se elimina correctamente |
| Proyecto | Tareas asociadas | No se elimina y se muestra mensaje | Se elimina correctamente |
| Tarea | Sin dependencias principales | Se elimina normalmente | Se elimina correctamente |

#### 14.2 Comprobación desde la API REST

En la API REST se validó que las operaciones `DELETE` devuelven respuestas coherentes y útiles para el cliente HTTP:

| Situación | Código HTTP | Significado |
| --- | --- | --- |
| Eliminación correcta | `204 No Content` | El registro se ha eliminado |
| Registro inexistente | `404 Not Found` | El ID no existe |
| Registro con dependencias | `409 Conflict` | No se puede eliminar por relaciones existentes |

Pruebas realizadas:

- `DELETE /api/clientes/1` devolvió `409 Conflict` cuando el cliente tenía proyectos asociados y `204 No Content` cuando quedó libre
- `DELETE /api/empleados/1` devolvió `409 Conflict` con tareas asociadas y `204 No Content` después de eliminar la tarea relacionada
- `DELETE /api/proyectos/2` devolvió `409 Conflict` al tener tareas asociadas
- `DELETE /api/tareas/1` devolvió `204 No Content`
- un segundo `DELETE /api/tareas/1` devolvió `404 Not Found`

#### 14.3 Comprobación desde la interfaz web

En la interfaz web se verificó el borrado desde los listados de clientes, empleados, proyectos y tareas:

- si el registro no tiene relaciones, se elimina correctamente
- si el registro tiene relaciones, se muestra un mensaje de error controlado
- la aplicación no muestra `Whitelabel Error Page`
- la aplicación no muestra error 500

Mensajes comprobados en la web:

- "No se puede eliminar el cliente porque tiene proyectos asociados. Elimina o reasigna primero sus proyectos."
- "No se puede eliminar el empleado porque tiene tareas asignadas."
- "No se puede eliminar el proyecto porque tiene tareas asociadas."
- "Tarea eliminada correctamente."
- "Empleado eliminado correctamente."
- "Proyecto eliminado correctamente."
- "Cliente eliminado correctamente."

---

### 15. Conclusión

Con este proyecto se aprende que verificar una aplicación no consiste solo en mirar si arranca, sino en comprobar de forma ordenada que cada parte funciona como debe. A través de los casos de prueba, las pruebas unitarias, la prueba de integración y la depuración, se puede justificar mejor que el sistema responde correctamente.

Las pruebas ayudan a detectar errores antes de la entrega y permiten trabajar con más seguridad. También es importante separar las pruebas unitarias de las de integración, porque cada una revisa una parte distinta del proyecto.

La depuración con IntelliJ también ayuda mucho a entender el flujo interno del programa, porque permite ver paso a paso qué hace cada método y cómo cambian los datos. Además, la verificación final ha servido para detectar y corregir un error real de serialización en la API REST, y para comprobar que la eliminación de datos relacionados se gestiona de manera segura.

Por todo esto, SmartFenix cumple mejor el resultado de aprendizaje **RA3**, ya que el proyecto ha sido verificado mediante pruebas diseñadas y ejecutadas sobre el sistema, se han comprobado tanto casos normales como casos de error, y la aplicación gestiona correctamente las restricciones de base de datos. La eliminación de datos se realiza de forma segura y el sistema queda listo para entrega y demostración.

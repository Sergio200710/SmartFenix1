# SmartFenix

SmartFenix es una aplicación Spring Boot para gestionar clientes, empleados, proyectos y tareas. El proyecto combina una interfaz web con Thymeleaf y una API REST bajo `/api`, siguiendo una arquitectura por capas `Controller -> Service -> Repository -> Domain`.

## 1. Descripción del proyecto

SmartFenix sirve para organizar información básica de una empresa de forma centralizada. El objetivo del proyecto es evitar llevar clientes, empleados, proyectos y tareas de forma desordenada, repartida en papeles o en hojas sueltas, y ofrecer una aplicación sencilla desde la que se puedan consultar y modificar esos datos.

El sistema gestiona principalmente estos datos:

- clientes o empresas que solicitan trabajos
- empleados internos y su rol
- proyectos asociados a un cliente
- tareas concretas asignadas a un proyecto y a un empleado

Se ha usado **Spring Boot 3.2.5** porque permite crear una aplicación web completa de forma rápida, con menos configuración manual y con una estructura clara para separar cada capa del proyecto.

Desde la aplicación, el usuario puede:

- crear registros nuevos
- consultar información ya guardada
- modificar datos existentes
- eliminar registros cuando sea necesario

La diferencia entre la **interfaz web** y la **API REST** es la siguiente:

- la interfaz web está pensada para usar la aplicación desde el navegador de forma visual
- la API REST está pensada para trabajar con peticiones HTTP, por ejemplo desde Postman o desde pruebas automáticas

## 2. Tecnologías utilizadas

Cada tecnología del proyecto tiene una función concreta dentro de la aplicación:

| Tecnología | Versión | Explicación |
| --- | --- | --- |
| Java | 21 | Lenguaje principal con el que está desarrollado todo el proyecto |
| Spring Boot | 3.2.5 | Permite crear la aplicación de forma rápida y organizada |
| Maven | - | Gestiona dependencias, compila el proyecto y ejecuta pruebas |
| Spring Data JPA | - | Facilita la conexión con la base de datos mediante repositorios |
| Spring Validation | - | Ayuda a validar datos de entrada en formularios y peticiones |
| Lombok | 1.18.32 | Reduce código repetitivo como getters, setters o builders |
| MySQL | 8.0 | Base de datos real usada por la aplicación en ejecución normal |
| Docker Compose | - | Levanta MySQL y phpMyAdmin con una sola configuración |
| phpMyAdmin | - | Permite visualizar y revisar la base de datos desde el navegador |
| Thymeleaf | - | Se usa para construir la interfaz web |
| JUnit 5 | - | Framework principal para escribir pruebas automatizadas |
| Mockito | - | Simula repositorios en pruebas unitarias para no usar base real |
| MockMvc | - | Simula peticiones HTTP en la prueba de integración |
| H2 | - | Base de datos en memoria usada en integración para no depender de MySQL |

## 3. Arquitectura del sistema

SmartFenix sigue una arquitectura por capas. Esta organización hace que el proyecto sea más fácil de entender, más sencillo de mantener y más cómodo de probar.

### Domain

La capa **Domain** contiene las entidades JPA del sistema. Estas clases representan la información principal del proyecto y están relacionadas con las tablas de la base de datos.

### Repository

La capa **Repository** contiene interfaces que extienden `JpaRepository`. Gracias a eso, el proyecto puede guardar, buscar, listar y eliminar registros sin tener que escribir manualmente muchas consultas básicas.

### Service

La capa **Service** contiene la lógica de negocio. Sirve para que el controlador no acceda directamente al repositorio y para concentrar en un mismo lugar operaciones como guardar, actualizar o validar comportamiento de las entidades.

### Controller

La capa **Controller** recibe peticiones HTTP. En el proyecto hay dos usos principales:

- controladores REST, que devuelven respuestas JSON
- controladores web, que devuelven vistas Thymeleaf

### Flujo general

El flujo habitual del proyecto puede explicarse así:

`Usuario / Postman -> Controller -> Service -> Repository -> Base de datos`

En las pruebas unitarias este flujo se corta antes de llegar a la base de datos real, porque el `Repository` se simula con Mockito.

## 4. Entidades principales

Las entidades principales del sistema son estas:

| Entidad | Campos principales | Función |
| --- | --- | --- |
| Cliente | nombre, empresa, telefono | Representa una empresa o persona que solicita trabajos |
| Empleado | nombre, email, rol | Representa trabajadores internos de SmartFenix |
| Proyecto | nombre, fechaInicio, fechaFin, cliente | Representa proyectos o trabajos asociados a un cliente |
| Tarea | descripcion, estado, proyecto, empleado | Representa tareas concretas asignadas dentro de un proyecto |

### Explicación breve de cada entidad

- **Cliente:** guarda la información básica de una empresa o persona con la que se trabaja.
- **Empleado:** permite registrar trabajadores internos y el rol que desempeñan.
- **Proyecto:** relaciona un trabajo concreto con un cliente y con unas fechas.
- **Tarea:** divide el trabajo en acciones más pequeñas asociadas a un proyecto y a un empleado responsable.

## 5. Configuración y ejecución

Para trabajar con el proyecto hace falta tener configurado el entorno correcto.

- **Java 21** debe estar disponible para compilar y ejecutar la aplicación.
- **Maven** permite lanzar comandos como `mvn test` y `mvn spring-boot:run`.
- **Docker Compose** levanta los servicios necesarios para la aplicación real.
- **MySQL real** se usa para ejecutar la aplicación en condiciones normales.
- **H2** solo se usa en la prueba de integración.
- **phpMyAdmin** sirve para visualizar la base de datos sin necesidad de usar comandos SQL manuales.

Configuración oficial:

- MySQL: `localhost:3308`
- phpMyAdmin: `http://localhost:8090`
- usuario MySQL: `root`
- contraseña MySQL: `root`
- aplicación: `http://localhost:8099`

### Comandos principales

Levantar Docker:

```bash
docker compose up -d
```

Ver el estado de los contenedores:

```bash
docker compose ps
```

Ejecutar todas las pruebas:

```bash
mvn test
```

Arrancar la aplicación:

```bash
mvn spring-boot:run
```

### Arranque en IntelliJ IDEA

Si quieres ejecutar el proyecto directamente desde IntelliJ, este es el orden recomendado:

1. Abrir la carpeta del proyecto:

```text
/home/sergio/SmartFenix1
```

2. Importar el proyecto como Maven si IntelliJ lo solicita.

3. Comprobar que el SDK del proyecto es `Java 21`.

4. Comprobar que IntelliJ tiene habilitado Lombok:

- plugin `Lombok` instalado
- opción `Build, Execution, Deployment -> Compiler -> Annotation Processors -> Enable annotation processing` activada

5. Levantar primero la base de datos:

```bash
docker compose up -d
```

6. Verificar que los servicios están arriba:

```bash
docker compose ps
```

Debe estar disponible:

- MySQL en `localhost:3308`
- phpMyAdmin en `http://localhost:8090`

7. Ejecutar la configuración de IntelliJ que ya viene preparada en el proyecto:

```text
SmartFenix1 - Correcta
```

Esa configuración arranca la clase principal:

```text
com.smartfenix.SmartFenixApplication
```

8. Cuando arranque correctamente, la aplicación quedará disponible en:

- `http://localhost:8099`
- `http://localhost:8099/dashboard`

#### Si prefieres crear la configuración manualmente

En IntelliJ:

- `Run -> Edit Configurations`
- `+ -> Spring Boot`
- nombre: `SmartFenix1 - Correcta`
- main class: `com.smartfenix.SmartFenixApplication`
- working directory: `$PROJECT_DIR$`
- JDK: `21`

#### Comprobación rápida después del arranque

Comprueba estas rutas:

- `http://localhost:8099/`
- `http://localhost:8099/dashboard`
- `http://localhost:8099/clientes`
- `http://localhost:8099/empleados`
- `http://localhost:8099/proyectos`
- `http://localhost:8099/tareas`

#### Si no arranca en IntelliJ

Revisa estos puntos:

- que Docker esté levantado
- que MySQL esté en `3308`
- que el SDK del proyecto sea `Java 21`
- que Lombok y annotation processing estén activos
- que el puerto `8099` no esté ocupado

## 6. Interfaz web

La interfaz web permite usar la aplicación sin necesidad de Postman. Esto hace que el proyecto sea más cómodo de enseñar y más fácil de entender en una demostración.

Desde el navegador se pueden gestionar clientes, empleados, proyectos y tareas. Cada sección está pensada para trabajar con operaciones básicas de listado, creación, edición y eliminación.

Rutas principales:

- `/`
- `/dashboard`
- `/clientes`
- `/empleados`
- `/proyectos`
- `/tareas`

La ruta principal lleva al dashboard y sirve como punto de entrada a la aplicación. Esta parte es especialmente útil para el vídeo de entrega porque permite enseñar el funcionamiento real del proyecto de forma visual.

## 7. API REST

La API REST permite interactuar con la aplicación mediante peticiones HTTP. Es útil para probar el comportamiento desde Postman, para automatizar pruebas y para comprobar que la aplicación responde correctamente sin depender de la interfaz web.

Cada entidad tiene operaciones CRUD:

- `GET` consulta datos
- `POST` crea datos
- `PUT` actualiza datos
- `DELETE` elimina datos

### Endpoints principales

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

## 8. Casos de prueba

Este apartado cumple la parte del enunciado que pide definir al menos cinco casos de prueba.

| Caso | Qué se prueba | Datos de entrada | Resultado esperado |
| --- | --- | --- | --- |
| Alta de cliente | Crear un cliente | Nombre, empresa y teléfono | El cliente se guarda y aparece en el listado |
| Consulta de clientes | Consultar clientes existentes | GET `/api/clientes` o ruta `/clientes` | Se muestra la lista de clientes |
| Modificación de proyecto | Editar un proyecto | Nuevo nombre o fechas | El proyecto queda actualizado |
| Eliminación de empleado | Eliminar un empleado existente | ID de empleado | El empleado desaparece del sistema |
| Consulta de registro inexistente | Consultar un ID inexistente | GET `/api/empleados/9999` | Respuesta `404 Not Found` |

### Desarrollo de los casos

1. **Alta de cliente**
   Objetivo: comprobar que se puede registrar un cliente nuevo.
   Datos usados: nombre, empresa y teléfono.
   Pasos: abrir formulario o enviar petición de creación, guardar y revisar listado.
   Resultado esperado: el cliente queda almacenado.
   Importancia: valida una operación básica del sistema.

2. **Consulta de clientes**
   Objetivo: comprobar que los clientes existentes pueden consultarse.
   Datos usados: listado actual de clientes.
   Pasos: acceder a `/clientes` o hacer `GET /api/clientes`.
   Resultado esperado: la información se muestra correctamente.
   Importancia: valida lectura de datos ya guardados.

3. **Modificación de proyecto**
   Objetivo: comprobar que un proyecto puede actualizarse.
   Datos usados: nuevo nombre o nuevas fechas.
   Pasos: editar proyecto, guardar cambios y revisar resultado.
   Resultado esperado: el proyecto queda actualizado.
   Importancia: valida que la edición funciona correctamente.

4. **Eliminación de empleado**
   Objetivo: comprobar que un empleado puede eliminarse.
   Datos usados: identificador del empleado.
   Pasos: seleccionar empleado, eliminar y comprobar listado.
   Resultado esperado: el empleado desaparece del sistema.
   Importancia: valida la operación de borrado.

5. **Consulta de registro inexistente**
   Objetivo: comprobar la respuesta ante un ID no válido.
   Datos usados: `GET /api/empleados/9999`.
   Pasos: lanzar la petición con un ID inexistente.
   Resultado esperado: respuesta `404 Not Found`.
   Importancia: valida que el error se gestiona de forma controlada.

## 9. Pruebas unitarias

Una prueba unitaria sirve para comprobar una parte pequeña del código de forma aislada. En este proyecto se prueban los servicios porque ahí se encuentra la lógica de negocio y porque es la capa más adecuada para verificar comportamiento sin arrancar toda la aplicación.

No se usa base de datos real en estas pruebas porque eso las haría más lentas y más dependientes del entorno. En su lugar se usa **Mockito**, que permite simular los repositorios y comprobar si el servicio hace la llamada correcta.

Se utiliza:

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

### ClienteServiceTest

- `testCrearCliente`: comprueba que un cliente puede guardarse correctamente y que el servicio llama al repositorio.
- `testListarClientes`: comprueba que el servicio devuelve la lista esperada.
- `testEliminarCliente`: comprueba que se llama a `deleteById` con el identificador correcto.

### EmpleadoServiceTest

- `testCrearEmpleado`: comprueba el guardado correcto de un empleado y la llamada al repositorio.
- `testListarEmpleados`: verifica que se listan los empleados simulados.
- `testEliminarEmpleado`: comprueba la eliminación por id mediante el repositorio mockeado.

### ProyectoServiceTest

- `testCrearProyecto`: valida la creación de un proyecto nuevo.
- `testListarProyectos`: comprueba la consulta de varios proyectos.
- `testActualizarProyecto`: valida que se actualizan correctamente los datos del proyecto y que se guarda el objeto esperado.

En conjunto, estas pruebas comprueban la lógica de servicio y la interacción correcta con el repositorio simulado.

## 10. Prueba de integración

Una prueba de integración comprueba que varias partes del sistema funcionan juntas. En lugar de revisar solo un método aislado, verifica el comportamiento del flujo completo entre varias capas.

En SmartFenix, la prueba de integración principal está en la clase:

- `EmpleadoControllerIntegrationTest`

### Tecnologías usadas

- `Spring Boot Test`: arranca el contexto de Spring para ejecutar la prueba en un entorno parecido al real.
- `MockMvc`: permite simular peticiones HTTP sin abrir manualmente el servidor en un navegador.
- `H2`: se usa como base de datos en memoria para no depender de MySQL real.
- `@SpringBootTest`: carga la aplicación para la prueba.
- `@AutoConfigureMockMvc`: prepara `MockMvc`.
- `@ActiveProfiles("test")`: activa el perfil de pruebas.

### Qué comprueba cada prueba

- `testListarEmpleadosDevuelveOk`: comprueba que `GET /api/empleados` responde correctamente.
- `testCrearEmpleadoFlujoCompleto`: comprueba que se puede crear un empleado mediante `POST`.
- `testConsultarEmpleadoInexistenteDevuelveNotFound`: comprueba que un ID inexistente devuelve `404`.

### Flujo de integración

`Controller -> Service -> Repository -> H2 en memoria`

Esta prueba no depende de MySQL real ni de Docker, lo que permite ejecutarla en cualquier entorno con Maven y Java 21.

## 11. Ejecución de pruebas

Las pruebas pueden ejecutarse desde terminal o desde IntelliJ IDEA.

### Desde terminal

```bash
mvn test
```

### Desde IntelliJ

1. abrir la carpeta `src/test/java`
2. elegir una clase de test o toda la carpeta
3. usar la opción `Run` o `Debug`

### Resultado validado

```text
BUILD SUCCESS
Tests run: 12
Failures: 0
Errors: 0
Skipped: 0
```

Esto significa que:

- la compilación ha terminado correctamente
- no hay fallos de aserciones
- no hay errores de ejecución
- todas las pruebas previstas han terminado bien

## 12. Depuración

Depurar significa ejecutar el programa paso a paso para observar qué está ocurriendo internamente. Es útil para entender mejor el flujo del código y detectar errores de lógica.

Un **breakpoint** es un punto de parada que hace que la ejecución se detenga en una línea concreta. A partir de ahí se pueden observar variables, objetos y resultados parciales.

**Step Over** permite avanzar línea a línea sin entrar en todos los métodos internos.

Inspeccionar variables sirve para comprobar cómo cambian los datos durante la ejecución.

### Métodos recomendados para depurar

- `ClienteService.save(...)`
- `ProyectoService.update(...)`
- `EmpleadoService.update(...)`

### Pasos útiles para el vídeo

1. abrir el proyecto en IntelliJ
2. colocar un breakpoint en un método de servicio
3. arrancar la aplicación o un test en modo Debug
4. avanzar con Step Over
5. inspeccionar variables
6. explicar cómo cambia el flujo

## 13. Incidencias y soluciones

### 1. Documentación antigua contradictoria

Problema: existían documentos antiguos con puertos y contraseñas diferentes, además de explicaciones erróneas sobre las pruebas.

Solución: se revisó la documentación y se dejó una única versión coherente y actualizada.

### 2. Puerto 8099 ocupado

Problema: podía quedar una instancia antigua de Java usando el puerto `8099`, lo que impedía arrancar la aplicación.

Solución: comprobar el proceso con:

```bash
lsof -i :8099
```

y cerrar la instancia anterior.

### 3. Diferencia entre MySQL real y H2

Problema: podía confundirse la base de datos de la aplicación con la base de datos de pruebas.

Solución: dejar claro que MySQL se usa para la aplicación real y H2 para la prueba de integración.

### 4. Evitar Docker en tests

Problema: si los tests dependieran de Docker, podrían fallar en otro ordenador aunque el código estuviera bien.

Solución: usar Mockito para unitarias y H2 para integración, evitando dependencia de Docker en pruebas automatizadas.

## 14. Conclusión

Con este proyecto he aprendido a separar mejor la parte de aplicación real y la parte de pruebas. También he visto que verificar un programa no consiste solo en comprobar si arranca, sino en diseñar casos de prueba, probar la lógica de negocio y revisar cómo responde el sistema en situaciones normales y en errores controlados.

Las pruebas ayudan a detectar errores antes de entregar el proyecto y hacen que el funcionamiento quede mejor justificado. También he comprobado que es importante diferenciar entre pruebas unitarias e integración, porque cada una revisa una parte distinta del sistema.

La depuración con IntelliJ también ha sido útil para entender el flujo interno del programa y ver cómo cambian los valores paso a paso.

Por todo esto, SmartFenix cumple el resultado de aprendizaje **RA3**, ya que el proyecto ha sido verificado mediante casos de prueba, pruebas unitarias, prueba de integración y depuración.

## 15. Anexo del vídeo demo

Guion recomendado para grabar el vídeo:

1. presentar el proyecto y la actividad
2. enseñar la estructura en IntelliJ
3. enseñar las entidades principales
4. enseñar servicios y controladores
5. enseñar la interfaz web
6. crear un registro desde la web
7. enseñar la API REST
8. ejecutar `mvn test`
9. mostrar `BUILD SUCCESS`
10. enseñar un test unitario
11. enseñar la prueba de integración
12. poner un breakpoint
13. ejecutar Debug
14. inspeccionar variables
15. cerrar con una conclusión final sobre la verificación

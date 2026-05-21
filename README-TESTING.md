# Mi Documentación de Pruebas Unitarias e Integración - SmartFenix (1º DAM)

En este documento explico detalladamente el plan de pruebas automatizadas que he diseñado e implementado para mi proyecto **SmartFenix**. He realizado estas pruebas para asegurar que toda la lógica de negocio funcione sin errores antes de la entrega del proyecto.

---

## 1. Tecnologías de Testing que he Utilizado

Para desarrollar las pruebas, he integrado las siguientes herramientas en el entorno de desarrollo:

*   **JUnit 5 (Jupiter):** Lo he usado como el framework principal para escribir los tests. Con él, he utilizado anotaciones como `@Test` y aserciones básicas (`assertEquals`, `assertNotNull`, `assertTrue`) para comprobar que los datos devueltos coincidan exactamente con lo esperado.
*   **Spring Boot Test:** He utilizado la anotación `@SpringBootTest` para arrancar el contexto de Spring de forma real. Esto me permite probar el comportamiento del código real conectándolo con la base de datos MySQL configurada en Docker.
*   **MockMvc:** He empleado esta herramienta en las pruebas de integración para simular peticiones HTTP (GET y POST) directamente a los controladores de mi API, sin necesidad de levantar el servidor web manualmente. Con MockMvc he podido verificar los estados HTTP de respuesta y los objetos JSON devueltos.

---

## 2. Estructura de Paquetes de mis Tests

He organizado mis clases de prueba dentro del directorio estándar de desarrollo de Maven (`src/test/java`), replicando la estructura de paquetes de mi código de producción para que todo esté ordenado:

```text
src/test/java/
└── com/
    └── smartfenix/
        ├── controller/
        │   └── EmpleadoControllerIntegrationTest.java  <-- Aquí he colocado las pruebas de integración y de validación
        └── service/
            ├── ClienteServiceTest.java                <-- Aquí he guardado las pruebas unitarias del servicio de Clientes
            └── ProyectoServiceTest.java               <-- Aquí he guardado las pruebas unitarias del servicio de Proyectos
```

---

## 3. Explicación de los Tests que he Creado

He dividido mi trabajo en tres bloques principales de pruebas para cubrir todos los requisitos de calidad exigidos:

### A. Pruebas Unitarias en la Capa Service
Con estos tests, he verificado de manera individual la lógica de negocio de mis servicios principales utilizando la base de datos real:

*   **En `ClienteServiceTest.java` he comprobado:**
    *   **Alta de cliente (`testCrearCliente`):** He verificado que al dar de alta un cliente como "Amazon", el sistema le asigne un ID automáticamente y guarde sus datos de nombre y empresa de forma correcta.
    *   **Búsqueda por ID (`testBuscarCliente`):** He guardado un cliente ("Sony") y he comprobado que el método `findById` lo recupere correctamente usando su ID.
    *   **Actualización de datos (`testActualizarCliente`):** He verificado que si modifico los datos de un cliente (de "Google" a "Alphabet"), el método de actualización guarde los nuevos valores sin perder el ID original.
*   **En `ProyectoServiceTest.java` he comprobado:**
    *   **Alta de proyecto (`testCrearProyecto`):** He verificado que un proyecto nuevo ("Proyecto Migración AWS") se asocie correctamente a un cliente de la base de datos y se almacene con éxito.
    *   **Búsqueda de proyecto (`testBuscarProyecto`):** He comprobado que al buscar un proyecto por ID, no solo recupere el proyecto, sino también todos los datos del cliente asociado mediante la relación JPA.
    *   **Actualización de proyecto (`testActualizarProyecto`):** He verificado que al cambiar el nombre de un proyecto de "Proyecto Inicial" a "Proyecto Actualizado", la base de datos refleje ese cambio.

### B. Pruebas de Integración con MockMvc
*   **En `EmpleadoControllerIntegrationTest.java` (`testCrearEmpleadoFlujoCompleto`):**
    *   Aquí he verificado el flujo completo de la aplicación: **Controller $\rightarrow$ Service $\rightarrow$ Repository $\rightarrow$ BD (MySQL)**.
    *   He usado `MockMvc` para enviar una petición HTTP `POST` a `/api/empleados` con los datos de "Carlos Gomez". He verificado que el controlador devuelva un HTTP `201 Created` y los datos del empleado creado en JSON.
    *   Para terminar el flujo de integración, he tomado el ID autogenerado que me devolvió la base de datos y he realizado una petición HTTP `GET` a `/api/empleados/{id}`, comprobando que responde un HTTP `200 OK` y devuelve al empleado correcto.

### C. Pruebas de Validación de Errores
*   **En `EmpleadoControllerIntegrationTest.java` he comprobado:**
    *   **Validación de Email (`testCrearEmpleadoConEmailInvalidoDevuelveBadRequest`):** He intentado enviar un empleado con un email incorrecto (`email-invalido`). He verificado que la API valide el formato y rechace la petición devolviendo un estado HTTP **400 Bad Request**.
    *   **Validación de Nombre (`testCrearEmpleadoConNombreVacioDevuelveBadRequest`):** He intentado crear un empleado con el nombre vacío. Gracias a la anotación `@NotBlank`, he verificado que el controlador intercepte el error y responda con un estado HTTP **400 Bad Request**.

---

## 4. Cómo Ejecuto los Tests

Para ejecutar mis pruebas he documentado dos métodos muy sencillos:

### Opción A: Desde la Consola (Terminal)
Me sitúo en la raíz del proyecto (donde está el archivo `pom.xml`) y ejecuto el comando de Maven:

```bash
mvn test
```
Este comando compila mi código de forma automática, levanta el entorno de Spring Boot con MySQL y ejecuta todos los tests de mi suite, mostrando un reporte detallado en la terminal al terminar.

### Opción B: Desde el Entorno de Desarrollo (IntelliJ IDEA)
1.  Busco en el árbol del proyecto la carpeta `src/test/java`.
2.  Hago clic derecho sobre ella y selecciono la opción **Run 'All Tests'** (o utilizo el atajo de teclado `Ctrl + Shift + F10`).
3.  IntelliJ abre una ventana en la parte inferior mostrando una barra de progreso que se pone en verde conforme pasan las pruebas.

---

## 5. Resultados Obtenidos y Mejoras Realizadas

Tras ejecutar toda mi suite de pruebas, he obtenido resultados muy positivos y he verificado el correcto funcionamiento del sistema:

*   **Funcionamiento exitoso:** Todos los tests implementados han funcionado correctamente y han pasado en verde (`BUILD SUCCESS`).
*   **Errores corregidos:** Al principio, durante mis primeras ejecuciones de prueba, detecté que si la base de datos MySQL en mi Docker no estaba activa, las pruebas unitarias fallaban con un error de conexión (`Connection Refused`). He corregido este flujo asegurándome de iniciar mi contenedor de Docker (`docker compose up -d`) antes de ejecutar las pruebas.
*   **Estado final:** He verificado que el sistema gestiona de forma robusta las entidades y responde con los códigos de error HTTP correctos ante entradas incorrectas. La aplicación se encuentra en un estado funcional y listo para entrega.

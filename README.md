
# TP Integrador · DAW1 — Biblioteca

**Alumno/a:** Martinez Quiles, Romina Antonella  
**Legajo:** 510035

**Enunciado elegido:** Biblioteca

## Descripción

Sistema de gestión de biblioteca desarrollado con Java 21 y Spring Boot.

La aplicación permite administrar libros, socios y préstamos mediante una API REST. Permite registrar, consultar, modificar y eliminar libros y socios, crear préstamos y registrar devoluciones.

Utiliza Spring Data JPA para la persistencia de datos y una base de datos H2.

## Cómo ejecutarlo

Requisitos: Java 21 (Maven viene incluido con `mvnw`).

En Windows:

```bash
mvnw.cmd spring-boot:run
```

La API queda en `http://localhost:8080/api`.

Base de datos H2:
`jdbc:h2:mem:biblioteca`

Consola H2:
`http://localhost:8080/h2-console`

## Modelo

| Entidad | Atributos principales | Relaciones |
|---|---|---|
| Libro | id, titulo, autor, isbn | Relacionado con préstamos |
| Socio | id, nombre, email | Relacionado con préstamos |
| Prestamo | id, fechaDevolucion, estado | N a 1 con Libro y Socio |

Estados de Prestamo:

`ACTIVO → DEVUELTO`

## Endpoints

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| GET | /api/libros | Lista todos los libros | 200 |
| GET | /api/libros/{id} | Busca libro por ID | 200, 404 |
| POST | /api/libros | Crea un libro | 201, 400, 409 |
| PUT | /api/libros/{id} | Modifica un libro | 200, 400, 404, 409 |
| DELETE | /api/libros/{id} | Elimina un libro | 204, 404, 409 |
| GET | /api/socios | Lista todos los socios | 200 |
| GET | /api/socios/{id} | Busca socio por ID | 200, 404 |
| POST | /api/socios | Crea un socio | 201, 400, 409 |
| PUT | /api/socios/{id} | Modifica un socio | 200, 400, 404, 409 |
| DELETE | /api/socios/{id} | Elimina un socio | 204, 404, 409 |
| POST | /api/prestamos | Crea un préstamo | 201, 400, 404, 409 |
| GET | /api/prestamos | Lista todos los préstamos | 200 |
| GET | /api/prestamos?estado=ACTIVO | Filtra préstamos por estado | 200 |
| PUT | /api/prestamos/{id}/devolver | Registra la devolución | 200, 404, 409 |

## Reglas de negocio

1. La fecha de devolución debe ser futura. Si no se cumple, se rechaza la solicitud.
2. Un libro no puede tener más de un préstamo activo.
3. Un socio puede tener como máximo tres préstamos sin devolver.
4. No se permite registrar dos veces la devolución de un mismo préstamo (409 Conflict).
5. Para crear un préstamo, el libro y el socio deben existir (404 Not Found).
6. El ISBN de los libros debe ser único.
7. El email de los socios debe ser único.
8. Los campos obligatorios no pueden estar vacíos.

## Pruebas

En el archivo `pruebas.http` se encuentran las peticiones de ejemplo para probar los endpoints desde IntelliJ IDEA.

Se realizaron pruebas de:

- Creación y listado de libros.
- Creación y listado de socios.
- Creación de préstamos.
- Registro de devolución.
- Rechazo de una segunda devolución (409 Conflict).

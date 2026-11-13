
package ar.edu.daw1.biblioteca.controller;

import ar.edu.daw1.biblioteca.dto.PrestamoRequest;
import ar.edu.daw1.biblioteca.entity.Prestamo;
import ar.edu.daw1.biblioteca.entity.EstadoPrestamo;
import ar.edu.daw1.biblioteca.service.PrestamoService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    // Crear un préstamo
    @PostMapping
    public ResponseEntity<Prestamo> crear(
            @Valid @RequestBody PrestamoRequest request) {

        Prestamo prestamo = prestamoService.crear(
                request.getLibroId(),
                request.getSocioId(),
                request.getFechaDevolucion()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(prestamo);
    }

    // Listar todos los préstamos o filtrar por estado
    @GetMapping
    public ResponseEntity<List<Prestamo>> listar(
            @RequestParam(required = false) EstadoPrestamo estado) {

        if (estado != null) {
            return ResponseEntity.ok(
                    prestamoService.listarPorEstado(estado)
            );
        }

        return ResponseEntity.ok(prestamoService.listar());
    }

    // Registrar devolución de un préstamo
    @PutMapping("/{id}/devolver")
    public ResponseEntity<Prestamo> devolver(
            @PathVariable Long id) {

        Prestamo prestamo = prestamoService.devolver(id);

        return ResponseEntity.ok(prestamo);
    }
}


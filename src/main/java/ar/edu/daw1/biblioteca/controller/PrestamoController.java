
package ar.edu.daw1.biblioteca.controller;

import ar.edu.daw1.biblioteca.dto.PrestamoRequest;
import ar.edu.daw1.biblioteca.entity.Prestamo;
import ar.edu.daw1.biblioteca.entity.EstadoPrestamo;
import ar.edu.daw1.biblioteca.service.PrestamoService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

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

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(prestamo.getId())
                .toUri();

        return ResponseEntity
                .created(location)
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

    // Buscar préstamo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                prestamoService.buscarPorId(id)
        );
    }

    // Cambiar estado del préstamo
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Prestamo> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String estadoTexto = request.get("estado");

        if (estadoTexto == null || estadoTexto.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe indicar un estado"
            );
        }

        EstadoPrestamo nuevoEstado;

        try {
            nuevoEstado = EstadoPrestamo.valueOf(estadoTexto);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Estado inválido"
            );
        }

        Prestamo prestamo =
                prestamoService.cambiarEstado(id, nuevoEstado);

        return ResponseEntity.ok(prestamo);
    }

    // Registrar devolución (se conserva la ruta anterior)
    @PutMapping("/{id}/devolver")
    public ResponseEntity<Prestamo> devolver(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                prestamoService.devolver(id)
        );
    }
}

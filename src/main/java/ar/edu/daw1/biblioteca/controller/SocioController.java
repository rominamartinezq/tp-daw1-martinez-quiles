
package ar.edu.daw1.biblioteca.controller;

import ar.edu.daw1.biblioteca.entity.Socio;
import ar.edu.daw1.biblioteca.entity.Prestamo;
import ar.edu.daw1.biblioteca.service.SocioService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/socios")
public class SocioController {

    private final SocioService socioService;

    public SocioController(SocioService socioService) {
        this.socioService = socioService;
    }

    // Listar todos los socios
    @GetMapping
    public List<Socio> listarTodos() {
        return socioService.listarTodos();
    }

    // Buscar socio por ID
    @GetMapping("/{id}")
    public Socio buscarPorId(@PathVariable Long id) {
        return socioService.buscarPorId(id);
    }

    // Crear socio
    @PostMapping
    public ResponseEntity<Socio> crear(
            @Valid @RequestBody Socio socio) {

        Socio nuevo = socioService.crear(socio);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevo);
    }

    // Actualizar socio
    @PutMapping("/{id}")
    public Socio actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Socio datos) {

        return socioService.actualizar(id, datos);
    }

    // Eliminar socio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        socioService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    // Listar préstamos de un socio
    @GetMapping("/{id}/prestamos")
    public List<Prestamo> listarPrestamos(
            @PathVariable Long id) {

        return socioService.listarPrestamos(id);
    }
}

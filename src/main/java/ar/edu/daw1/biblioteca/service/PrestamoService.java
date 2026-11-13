
package ar.edu.daw1.biblioteca.service;

import ar.edu.daw1.biblioteca.entity.*;
import ar.edu.daw1.biblioteca.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final SocioRepository socioRepository;

    public PrestamoService(
            PrestamoRepository prestamoRepository,
            LibroRepository libroRepository,
            SocioRepository socioRepository) {

        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.socioRepository = socioRepository;
    }

    public Prestamo crear(Long libroId, Long socioId, LocalDate fechaDevolucion) {

        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Libro no encontrado"));

        Socio socio = socioRepository.findById(socioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Socio no encontrado"));

        if (fechaDevolucion == null ||
                !fechaDevolucion.isAfter(LocalDate.now())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha debe ser futura");
        }

        List<Prestamo> prestamosLibro =
                prestamoRepository.findByLibro(libro);

        boolean libroOcupado = prestamosLibro.stream()
                .anyMatch(p -> p.getEstado() != EstadoPrestamo.DEVUELTO);

        if (libroOcupado) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El libro ya está prestado");
        }

        List<Prestamo> prestamosSocio =
                prestamoRepository.findBySocio(socio);

        long prestamosActivos = prestamosSocio.stream()
                .filter(p -> p.getEstado() != EstadoPrestamo.DEVUELTO)
                .count();

        if (prestamosActivos >= 3) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El socio ya tiene 3 préstamos sin devolver");
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setLibro(libro);
        prestamo.setSocio(socio);
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamo.setEstado(EstadoPrestamo.ACTIVO);

        return prestamoRepository.save(prestamo);
    }

    public List<Prestamo> listar() {
        return prestamoRepository.findAll();
    }

    public List<Prestamo> listarPorEstado(EstadoPrestamo estado) {
        return prestamoRepository.findByEstado(estado);
    }

    public List<Prestamo> listarPorSocio(Socio socio) {
        return prestamoRepository.findBySocio(socio);
    }

    // Registrar devolución de un préstamo
    public Prestamo devolver(Long prestamoId) {

        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Préstamo no encontrado"));

        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Este préstamo ya fue devuelto");
        }

        prestamo.setEstado(EstadoPrestamo.DEVUELTO);

        return prestamoRepository.save(prestamo);
    }
}


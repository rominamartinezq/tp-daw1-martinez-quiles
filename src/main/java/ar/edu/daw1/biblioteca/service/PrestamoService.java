
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

    // Crear préstamo
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

    // Listar todos los préstamos
    public List<Prestamo> listar() {
        return prestamoRepository.findAll();
    }

    // Buscar préstamo por ID
    public Prestamo buscarPorId(Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Préstamo no encontrado"));
    }

    // Listar préstamos por estado
    public List<Prestamo> listarPorEstado(EstadoPrestamo estado) {
        return prestamoRepository.findByEstado(estado);
    }

    // Listar préstamos de un socio
    public List<Prestamo> listarPorSocio(Socio socio) {
        return prestamoRepository.findBySocio(socio);
    }

    // Cambiar estado del préstamo
    public Prestamo cambiarEstado(Long prestamoId, EstadoPrestamo nuevoEstado) {

        Prestamo prestamo = buscarPorId(prestamoId);

        if (nuevoEstado == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe indicar un estado");
        }

        EstadoPrestamo estadoActual = prestamo.getEstado();

        boolean transicionPermitida =
                (estadoActual == EstadoPrestamo.ACTIVO &&
                        (nuevoEstado == EstadoPrestamo.DEVUELTO ||
                                nuevoEstado == EstadoPrestamo.VENCIDO))
                        ||
                        (estadoActual == EstadoPrestamo.VENCIDO &&
                                nuevoEstado == EstadoPrestamo.DEVUELTO);

        if (!transicionPermitida) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Transición de estado no permitida");
        }

        prestamo.setEstado(nuevoEstado);

        return prestamoRepository.save(prestamo);
    }

    // Registrar devolución
    public Prestamo devolver(Long prestamoId) {
        return cambiarEstado(prestamoId, EstadoPrestamo.DEVUELTO);
    }
}

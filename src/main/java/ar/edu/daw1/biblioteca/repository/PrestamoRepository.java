
package ar.edu.daw1.biblioteca.repository;

import ar.edu.daw1.biblioteca.entity.Prestamo;
import ar.edu.daw1.biblioteca.entity.EstadoPrestamo;
import ar.edu.daw1.biblioteca.entity.Libro;
import ar.edu.daw1.biblioteca.entity.Socio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findBySocio(Socio socio);

    List<Prestamo> findByLibro(Libro libro);

    List<Prestamo> findByEstado(EstadoPrestamo estado);

}

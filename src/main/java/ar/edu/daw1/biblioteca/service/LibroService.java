
package ar.edu.daw1.biblioteca.service;

import ar.edu.daw1.biblioteca.entity.Libro;
import ar.edu.daw1.biblioteca.repository.LibroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }

    public Libro buscarPorId(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Libro no encontrado"));
    }

    public Libro crear(Libro libro) {

        if (libroRepository.existsByIsbn(libro.getIsbn())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un libro con ese ISBN");
        }

        return libroRepository.save(libro);
    }

    public Libro actualizar(Long id, Libro datos) {
        Libro libro = buscarPorId(id);

        if (libroRepository.existsByIsbnAndIdNot(
                datos.getIsbn(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe otro libro con ese ISBN");
        }

        libro.setTitulo(datos.getTitulo());
        libro.setAutor(datos.getAutor());
        libro.setIsbn(datos.getIsbn());

        return libroRepository.save(libro);
    }

    public void eliminar(Long id) {
        Libro libro = buscarPorId(id);
        libroRepository.delete(libro);
    }
}

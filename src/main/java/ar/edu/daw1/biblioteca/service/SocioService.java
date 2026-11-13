
package ar.edu.daw1.biblioteca.service;

import ar.edu.daw1.biblioteca.entity.Socio;
import ar.edu.daw1.biblioteca.repository.SocioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SocioService {

    private final SocioRepository socioRepository;

    public SocioService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    public List<Socio> listarTodos() {
        return socioRepository.findAll();
    }

    public Socio buscarPorId(Long id) {
        return socioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Socio no encontrado"));
    }

    public Socio crear(Socio socio) {

        if (socioRepository.existsByEmail(socio.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya existe un socio con ese email");
        }

        return socioRepository.save(socio);
    }

    public Socio actualizar(Long id, Socio datos) {

        Socio socio = buscarPorId(id);

        if (socioRepository.existsByEmailAndIdNot(
                datos.getEmail(), id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya existe otro socio con ese email");
        }

        socio.setNombre(datos.getNombre());
        socio.setEmail(datos.getEmail());

        return socioRepository.save(socio);
    }

    public void eliminar(Long id) {
        Socio socio = buscarPorId(id);
        socioRepository.delete(socio);
    }
}

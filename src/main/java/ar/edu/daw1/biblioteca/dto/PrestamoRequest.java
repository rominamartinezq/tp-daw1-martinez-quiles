
package ar.edu.daw1.biblioteca.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class PrestamoRequest {

    @NotNull(message = "El ID del libro es obligatorio")
    private Long libroId;

    @NotNull(message = "El ID del socio es obligatorio")
    private Long socioId;

    @NotNull(message = "La fecha de devolución es obligatoria")
    @Future(message = "La fecha de devolución debe ser futura")
    private LocalDate fechaDevolucion;

    public Long getLibroId() {
        return libroId;
    }

    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    public Long getSocioId() {
        return socioId;
    }

    public void setSocioId(Long socioId) {
        this.socioId = socioId;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
}

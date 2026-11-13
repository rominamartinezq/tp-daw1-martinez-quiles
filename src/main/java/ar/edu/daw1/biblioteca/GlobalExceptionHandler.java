
package ar.edu.daw1.biblioteca;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail manejarValidaciones(
            MethodArgumentNotValidException ex) {

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "La petición tiene errores");

        problema.setTitle("Datos inválidos");

        Map<String, String> errores = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage()));

        problema.setProperty("errores", errores);

        return problema;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail manejarJsonInvalido(
            HttpMessageNotReadableException ex) {

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "El JSON enviado no es válido");

        problema.setTitle("Datos inválidos");

        return problema;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail manejarEstado(
            ResponseStatusException ex) {

        HttpStatus status = HttpStatus.valueOf(
                ex.getStatusCode().value());

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                status,
                ex.getReason() != null
                        ? ex.getReason()
                        : "La solicitud no pudo procesarse");

        problema.setTitle(status.getReasonPhrase());

        return problema;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail manejarRestricciones(
            ConstraintViolationException ex) {

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "La petición tiene errores");

        problema.setTitle("Datos inválidos");

        Map<String, String> errores = new LinkedHashMap<>();

        ex.getConstraintViolations().forEach(error ->
                errores.put(error.getPropertyPath().toString(),
                        error.getMessage()));

        problema.setProperty("errores", errores);

        return problema;
    }
}




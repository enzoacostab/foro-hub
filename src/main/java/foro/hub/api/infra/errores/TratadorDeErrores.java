package foro.hub.api.infra.errores;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class TratadorDeErrores {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarError404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarError400(MethodArgumentNotValidException e) {
        var errores = e.getFieldErrors().stream().map(DatosError400::new).toList();
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity tratarErrorEntradaDuplicada(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().body(new DatosErrorValidacion(e.getMostSpecificCause().getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity tratarErrorEntradaInvalida(HttpMessageNotReadableException e) {
        String[] err = e.getLocalizedMessage().split("\\[|\"");
        return ResponseEntity.badRequest().body(new DatosErrorValidacion("'" +
                                                                         err[1] +
                                                                         "' no esta permitido. valores permitidos: [" +
                                                                         err[err.length - 1]));
    }

    private record DatosError400(String campo, String error) {
        DatosError400(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

    private record DatosErrorValidacion(String error) {}
}

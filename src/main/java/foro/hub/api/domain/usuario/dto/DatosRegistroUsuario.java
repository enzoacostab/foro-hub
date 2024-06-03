package foro.hub.api.domain.usuario.dto;

import jakarta.validation.constraints.NotBlank;

public record DatosRegistroUsuario(
        @NotBlank
        String username,
        @NotBlank
        String clave
) {
}

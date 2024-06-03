package foro.hub.api.domain.topico.dto;


import foro.hub.api.domain.topico.model.Topico;

import java.time.LocalDate;

public record DatosRespuestaTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDate fechaDeCreacion,
        Status status,
        String autor,
        String curso
) {
    public DatosRespuestaTopico(Topico topico) {
        this(topico.getId(),
            topico.getTitulo(),
            topico.getMensaje(),
            topico.getFechaDeCreacion(),
            topico.getStatus(),
            topico.getAutor(),
            topico.getCurso()
        );
    }
}

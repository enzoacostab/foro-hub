package foro.hub.api.domain.topico.model;

import foro.hub.api.domain.topico.dto.DatosActualizarTopico;
import foro.hub.api.domain.topico.dto.DatosRegistroTopico;
import foro.hub.api.domain.topico.dto.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String mensaje;
    @Column(name = "fecha_de_creacion")
    private LocalDate fechaDeCreacion;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String autor;
    private String curso;

    public Topico(DatosRegistroTopico datos) {
        titulo = datos.titulo();
        mensaje = datos.mensaje();
        fechaDeCreacion = LocalDate.now();
        status = Status.SIN_RESOLVER;
        autor = datos.autor();
        curso = datos.curso();
    }

    public void actualizarTopico(DatosActualizarTopico datos) {
        titulo = datos.titulo();
        mensaje = datos.mensaje();
        status = datos.status();
        autor = datos.autor();
        curso = datos.curso();
    }
}
package foro.hub.api.domain.topico.service;

import foro.hub.api.domain.topico.dto.DatosActualizarTopico;
import foro.hub.api.domain.topico.dto.DatosRegistroTopico;
import foro.hub.api.domain.topico.dto.DatosRespuestaTopico;
import foro.hub.api.domain.topico.model.Topico;
import foro.hub.api.domain.topico.repository.TopicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopicoService {
    @Autowired
    private TopicoRepository repository;

    public DatosRespuestaTopico registrar(DatosRegistroTopico datos) {
        var topico = repository.save(new Topico(datos));
        return new DatosRespuestaTopico(topico);
    }

    public Page<DatosRespuestaTopico> obtenerTodos(Pageable paginacion) {
        return repository.findAll(paginacion).map(DatosRespuestaTopico::new);
    }

    public DatosRespuestaTopico actualizar(DatosActualizarTopico datos, Long id) {
        var topico = repository.getReferenceById(id);
        topico.actualizarTopico(datos);
        return new DatosRespuestaTopico(topico);
    }

    public Topico eliminar(Long id) {
        Optional<Topico> topico = repository.findById(id);
        System.out.println(topico);
        if (topico.isPresent()) {
            repository.delete(topico.get());
            return topico.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    public DatosRespuestaTopico obtener(Long id) {
        var topico = repository.getReferenceById(id);
        return new DatosRespuestaTopico(topico);
    }
}

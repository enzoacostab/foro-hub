package foro.hub.api.domain.topico.repository;

import foro.hub.api.domain.topico.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TopicoRepository extends JpaRepository<Topico, Long> {
}

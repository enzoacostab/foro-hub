package foro.hub.api.controller;

import foro.hub.api.domain.topico.service.TopicoService;
import foro.hub.api.domain.topico.dto.DatosActualizarTopico;
import foro.hub.api.domain.topico.dto.DatosRegistroTopico;
import foro.hub.api.domain.topico.dto.DatosRespuestaTopico;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {
    @Autowired
    private TopicoService service;

    @PostMapping
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(@RequestBody @Valid DatosRegistroTopico datos,
                                                                UriComponentsBuilder uriComponentsBuilder) {
        var topico = service.registrar(datos);
        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.id()).toUri();
        return ResponseEntity.created(url).body(topico);
    }

    @GetMapping
    public ResponseEntity<Page<DatosRespuestaTopico>> listarTopicos(@PageableDefault(size = 2) Pageable paginacion) {
        Page<DatosRespuestaTopico> topicos = service.obtenerTodos(paginacion);
        return ResponseEntity.ok(topicos);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(@RequestBody @Valid DatosActualizarTopico datos,
                                                                 @PathVariable Long id) {
        var topico = service.actualizar(datos, id);
        return ResponseEntity.ok(topico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarTopico(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> retornarTopico(@PathVariable Long id) {
        var topico = service.obtener(id);
        return ResponseEntity.ok(topico);
    }
}

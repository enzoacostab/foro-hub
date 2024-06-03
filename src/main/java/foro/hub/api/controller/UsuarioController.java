package foro.hub.api.controller;

import foro.hub.api.domain.topico.dto.DatosRespuestaTopico;
import foro.hub.api.domain.topico.model.Topico;
import foro.hub.api.domain.usuario.dto.DatosRegistroUsuario;
import foro.hub.api.domain.usuario.dto.DatosRespuestaUsuario;
import foro.hub.api.domain.usuario.model.Usuario;
import foro.hub.api.domain.usuario.repository.UsuarioRepository;
import foro.hub.api.domain.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService service;

    @PostMapping
    public ResponseEntity<DatosRespuestaUsuario> registrarUsuario(@RequestBody @Valid DatosRegistroUsuario datos,
                                            UriComponentsBuilder uriComponentsBuilder) {
        var usuario = service.registrar(datos);
        var url = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.id()).toUri();
        return ResponseEntity.created(url).body(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaUsuario> retornarUsuario(@PathVariable Long id) {
        var usuario = service.obtener(id);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarUsuario(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

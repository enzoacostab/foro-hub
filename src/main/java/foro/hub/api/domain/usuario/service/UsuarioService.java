package foro.hub.api.domain.usuario.service;

import foro.hub.api.domain.usuario.dto.DatosRegistroUsuario;
import foro.hub.api.domain.usuario.dto.DatosRespuestaUsuario;
import foro.hub.api.domain.usuario.model.Usuario;
import foro.hub.api.domain.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    public DatosRespuestaUsuario registrar(DatosRegistroUsuario datos) {
        var encoder = new BCryptPasswordEncoder();
        var clave = encoder.encode(datos.clave());
        var usuario = new Usuario(null, datos.username(), clave);
        repository.save(usuario);
        return new DatosRespuestaUsuario(usuario.getId(), usuario.getUsername());

    }

    public Usuario eliminar(Long id) {
        Optional<Usuario> usuario = repository.findById(id);
        System.out.println(usuario);
        if (usuario.isPresent()) {
            repository.delete(usuario.get());
            return usuario.get();
        } else {
            throw new EntityNotFoundException();
        }
    }

    public DatosRespuestaUsuario obtener(Long id) {
        var usuario = repository.getReferenceById(id);
        return new DatosRespuestaUsuario(usuario.getId(), usuario.getUsername());
    }
}

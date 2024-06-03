package foro.hub.api.infra.security.service;

import foro.hub.api.domain.usuario.model.Usuario;
import foro.hub.api.infra.security.dto.DatosAutenticacionUsuario;
import foro.hub.api.infra.security.dto.DatosJwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public DatosJwtToken autenticar(DatosAutenticacionUsuario datos) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(datos.username(), datos.clave());
        var usuarioAutenticado = authenticationManager.authenticate(authToken);
        var jwtToken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());
        return new DatosJwtToken(jwtToken);
    }
}

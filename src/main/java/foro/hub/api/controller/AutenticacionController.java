package foro.hub.api.controller;

import foro.hub.api.infra.security.dto.DatosAutenticacionUsuario;
import foro.hub.api.infra.security.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacionController {
    @Autowired
    AuthService service;

    @PostMapping
    public ResponseEntity autenticarUsuario(@RequestBody @Valid DatosAutenticacionUsuario datos) {
        var jwtToken = service.autenticar(datos);
        return ResponseEntity.ok(jwtToken);
    }
}

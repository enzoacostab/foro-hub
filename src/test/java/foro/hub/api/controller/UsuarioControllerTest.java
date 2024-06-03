package foro.hub.api.controller;

import foro.hub.api.domain.usuario.dto.DatosRegistroUsuario;
import foro.hub.api.domain.usuario.dto.DatosRespuestaUsuario;
import foro.hub.api.domain.usuario.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class UsuarioControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<DatosRegistroUsuario> registroUsuarioJacksonTester;
    @Autowired
    private JacksonTester<DatosRespuestaUsuario> respuestaUsuarioJacksonTester;
    @MockBean
    private UsuarioService usuarioService;
    private DatosRespuestaUsuario datos = new DatosRespuestaUsuario(1L, "enzo");

    @Test
    @DisplayName("deberia retornar estado http 400 cuando los datos ingresados sean invalidos - registrar")
    @WithMockUser
    void registrarUsuarioEscenario1() throws Exception {
        var response = mvc.perform(post("/usuarios")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("deberia retornar estado http 200 cuando los datos ingresados sean validos - registrar")
    @WithMockUser
    void registrarUsuarioEscenario2() throws Exception {
        when(usuarioService.registrar(any())).thenReturn(datos);

        var response = mvc
                .perform(post("/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(registroUsuarioJacksonTester
                            .write(new DatosRegistroUsuario("enzo", "123456"))
                            .getJson()
                    ))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var jsonEsperado = respuestaUsuarioJacksonTester
                .write(datos)
                .getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("deberia retornar estado http 404 cuando no exista un usuario con el id proporcionado - eliminar")
    @WithMockUser
    void eliminarUsuarioEscenario1() throws Exception {
        when(usuarioService.eliminar(any())).thenThrow(new EntityNotFoundException());

        var response = mvc
                .perform(delete("/usuarios/1"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("deberia retornar estado http 204 cuando exista un usuario con el id proporcionado - eliminar")
    @WithMockUser
    void eliminarUsuarioEscenario2() throws Exception {
        when(usuarioService.eliminar(any())).thenReturn(any());

        var response = mvc
                .perform(delete("/usuarios/1"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("deberia retornar estado http 404 cuando no exista un usuario con el id proporcionado - retornar")
    @WithMockUser
    void retornarUsuario1() throws Exception {
        when(usuarioService.obtener(any())).thenThrow(new EntityNotFoundException());

        var response = mvc
                .perform(get("/usuarios/1"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("deberia retornar estado http 200 cuando exista un usuario con el id proporcionado - retornar")
    @WithMockUser
    void retornarUsuarioEscenario2() throws Exception {
        when(usuarioService.obtener(any())).thenReturn(datos);

        var response = mvc
                .perform(get("/usuarios/1"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = respuestaUsuarioJacksonTester
                .write(datos)
                .getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
}
package foro.hub.api.controller;

import foro.hub.api.infra.security.dto.DatosAutenticacionUsuario;
import foro.hub.api.infra.security.dto.DatosJwtToken;
import foro.hub.api.infra.security.service.AutenticacionService;
import foro.hub.api.infra.security.service.AuthService;
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
class AutenticacionControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<DatosAutenticacionUsuario> autenticacionUsuarioJacksonTester;
    @Autowired
    private JacksonTester<DatosJwtToken> jwtTokenJacksonTester;
    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("deberia retornar estado http 400 cuando los datos ingresados sean invalidos - autenticar")
    @WithMockUser
    void autenticarUsuarioEscenario1() throws Exception {
        var response = mvc.perform(post("/login")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("deberia retornar estado http 200 cuando los datos ingresados sean validos - autenticar")
    @WithMockUser
    void autenticarUsuarioEscenario2() throws Exception {
        var datos = new DatosJwtToken(".");

        when(authService.autenticar(any())).thenReturn(datos);

        var response = mvc
                .perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(autenticacionUsuarioJacksonTester
                            .write(new DatosAutenticacionUsuario("enzo", "123456"))
                            .getJson()
                    ))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = jwtTokenJacksonTester
                .write(datos)
                .getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
}
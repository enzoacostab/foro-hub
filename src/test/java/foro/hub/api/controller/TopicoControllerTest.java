package foro.hub.api.controller;

import foro.hub.api.domain.topico.dto.DatosActualizarTopico;
import foro.hub.api.domain.topico.service.TopicoService;
import foro.hub.api.domain.topico.dto.DatosRegistroTopico;
import foro.hub.api.domain.topico.dto.DatosRespuestaTopico;
import foro.hub.api.domain.topico.dto.Status;
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

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TopicoControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<DatosRegistroTopico> registroTopicoJacksonTester;
    @Autowired
    private JacksonTester<DatosRespuestaTopico> respuestaTopicoJacksonTester;
    @Autowired
    private JacksonTester<DatosActualizarTopico> actualizarTopicoJacksonTester;
    @MockBean
    private TopicoService topicoService;
    private final DatosRespuestaTopico datos = new DatosRespuestaTopico(null,".",".",
                                             LocalDate.now(), Status.SIN_RESOLVER, ".", ".");

    @Test
    @DisplayName("deberia retornar estado http 400 cuando los datos ingresados sean invalidos - registrar")
    @WithMockUser
    void registrarTopicoEscenario1() throws Exception {
        var response = mvc.perform(post("/topicos")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("deberia retornar estado http 200 cuando los datos ingresados sean validos - registrar")
    @WithMockUser
    void registrarTopicoEscenario2() throws Exception {
        when(topicoService.registrar(any())).thenReturn(datos);

        var response = mvc
                .perform(post("/topicos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(registroTopicoJacksonTester
                            .write(new DatosRegistroTopico(".", ".", ".", "."))
                            .getJson()
                    ))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());

        var jsonEsperado = respuestaTopicoJacksonTester
                .write(datos)
                .getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("deberia retornar estado http 200 - retornar todos")
    @WithMockUser
    void listarTopicos() throws Exception {
        var response = mvc
                .perform(get("/topicos"))
                .andReturn()
                .getResponse();

        System.out.println(response.getContentAsString());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("deberia retornar estado http 400 cuando los datos ingresados sean invalidos - actualizar")
    @WithMockUser
    void actualizarTopicoEscenario1() throws Exception {
        var response = mvc.perform(put("/topicos/1")).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("deberia retornar estado http 404 cuando no exista un topico con el id proporcionado - actualizar")
    @WithMockUser
    void actualizarTopicoEscenario2() throws Exception {
        when(topicoService.actualizar(any(), any())).thenThrow(new EntityNotFoundException());

        var response = mvc
                .perform(put("/topicos/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(actualizarTopicoJacksonTester
                                .write(new DatosActualizarTopico("..", "..",
                                        "..", Status.RESUELTO, ".."))
                                .getJson()
                        ))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("deberia retornar estado http 200 cuando los datos ingresados sean validos y el topico exista - actualizar")
    @WithMockUser
    void actualizarTopicoEscenario3() throws Exception {
        DatosRespuestaTopico datosActualizados = new DatosRespuestaTopico(null,"..","..",
                LocalDate.now(), Status.RESUELTO, "..", "..");

        when(topicoService.actualizar(any(), any())).thenReturn(datosActualizados);

        var response = mvc
                .perform(put("/topicos/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(actualizarTopicoJacksonTester
                                .write(new DatosActualizarTopico("..", "..", "..", Status.RESUELTO, ".."))
                                .getJson()
                        ))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = respuestaTopicoJacksonTester
                .write(datosActualizados)
                .getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }

    @Test
    @DisplayName("deberia retornar estado http 404 cuando no exista un topico con el id proporcionado - eliminar")
    @WithMockUser
    void eliminarTopicoEscenario1() throws Exception {
        when(topicoService.eliminar(any())).thenThrow(new EntityNotFoundException());

        var response = mvc
                .perform(delete("/topicos/1"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("deberia retornar estado http 204 cuando exista un topico con el id proporcionado - eliminar")
    @WithMockUser
    void eliminarTopicoEscenario2() throws Exception {
        when(topicoService.eliminar(any())).thenReturn(any());

        var response = mvc
                .perform(delete("/topicos/1"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("deberia retornar estado http 404 cuando no exista un topico con el id proporcionado - retornar")
    @WithMockUser
    void retornarTopico1() throws Exception {
        when(topicoService.obtener(any())).thenThrow(new EntityNotFoundException());

        var response = mvc
                .perform(get("/topicos/1"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("deberia retornar estado http 200 cuando exista un topico con el id proporcionado - retornar")
    @WithMockUser
    void retornarTopicoEscenario2() throws Exception {
        when(topicoService.obtener(any())).thenReturn(datos);

        var response = mvc
                .perform(get("/topicos/1"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        var jsonEsperado = respuestaTopicoJacksonTester
                .write(datos)
                .getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
}
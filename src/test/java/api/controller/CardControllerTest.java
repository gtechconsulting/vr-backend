package api.controller;

import api.ApplicationTests;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CardControllerTest extends ApplicationTests {

    private static final String URL_API = "/cartoes";

    private MockMvc mockMvc;

    @Autowired
    private CardController cardController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
    }

    @AfterAll
    public void tearDown() throws Exception {
        this.testDeleteCardById();
    }

    @Test
    @DisplayName("Create Card")
    public void testCreateCard() throws Exception {
        String data = "{\"cardNumber\": \"1010101010\", \"password\": \"789567345\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("GetAllCards")
    public void testGetAllCards() throws Exception {
        String data = "{\"cardNumber\": \"123123456456\", \"password\": \"123456654\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_API))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Get Card By Card Number")
    public void testGetCardByNumberCard() throws Exception {
        String data = "{\"cardNumber\": \"3030303030\", \"password\": \"5656565\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_API+"/{numeroCartao}", "3030303030"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Get Card by Id")
    public void testGetCardById() throws Exception {
        String data = "{\"cardNumber\": \"9030405060\", \"password\": \"453456467\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_API+"/id/{id}", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Update Card by Id")
    public void testUpdateCardById() throws Exception {
        String data = "{\"cardNumber\": \"5050505050\", \"password\": \"77777777777\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String updated = "{\"cardNumber\": \"7777777777\", \"password\": \"88888888888\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.put(URL_API+"/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updated)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Delete Card by Id")
    public void testDeleteCardById() throws Exception {
        String data = "{\"cardNumber\": \"6060606060\", \"password\": \"9999999999999\", \"status\": \"ATIVO\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_API+"/{id}", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}

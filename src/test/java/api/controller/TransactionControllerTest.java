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
public class TransactionControllerTest extends ApplicationTests {

    private static final String URL_API = "/transacoes";

    private MockMvc mockMvc;

    @Autowired
    private TransactionController transactionController;

    @Autowired
    private CardController cardController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @AfterAll
    public void tearDown() throws Exception {
        this.testDeleteTransactionById();
    }

    @Test
    @DisplayName("Create transaction")
    public void testCreateTransaction() throws Exception {
        String card = "{\"cardNumber\": \"123123456456\", \"password\": \"123123456\", \"status\": \"ATIVO\"}";

        this.mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(card) 
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String transaction = "{\"cardNumber\": \"123123456456\", \"password\": \"123123456\", \"valor\": \"10.20\"}";

        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transaction)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @DisplayName("Get all transactions")
    public void testGetAllTransactions() throws Exception {
        String card = "{\"cardNumber\": \"123456123456\", \"password\": \"123456123\", \"status\": \"ATIVO\"}";

        this.mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(card) 
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String transaction = "{\"cardNumber\": \"123456123456\", \"password\": \"123456123\", \"valor\": \"10.20\"}";

        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transaction)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_API))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Get transaction by ID")
    public void testGetTransactionById() throws Exception {
        String card = "{\"cardNumber\": \"123412341234\", \"password\": \"1234567890\", \"status\": \"ATIVO\"}";

        this.mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(card) 
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String transaction = "{\"cardNumber\": \"123412341234\", \"password\": \"1234567890\", \"valor\": \"10.20\"}";

        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transaction)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_API+"/{id}", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Delete transaction by ID")
    public void testDeleteTransactionById() throws Exception {
        String card = "{\"carNumber\": \"123123123123\", \"password\": \"1234567890\", \"status\": \"ATIVO\"}";

        this.mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/cartoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(card) 
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String transaction = "{\"cardNumber\": \"123123123123\", \"password\": \"1234567890\", \"valor\": \"10.20\"}";

        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transaction)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        this.mockMvc.perform(MockMvcRequestBuilders.delete(URL_API+"/{id}", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}

package nl.inholland.codegeneration.controllers;

import nl.inholland.codegeneration.models.DTO.request.TransactionRequestDTO;
import nl.inholland.codegeneration.models.DTO.response.TransactionResponseDTO;
import nl.inholland.codegeneration.models.QueryParams;
import nl.inholland.codegeneration.models.Transaction;
import nl.inholland.codegeneration.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

public class TransactionControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEE"})
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/transactions").param("filter", "filterQuery")
                                            .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEE"})
    public void testGetById() throws Exception {
        mockMvc.perform(get("/transactions/{id}", 1).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"EMPLOYEE", "CUSTOMER"})
    public void testAdd() throws Exception {
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO("NL01INHO0000000001", "NL01INHO0000000002", new BigDecimal(100), "description");
        // Fill the object according to your needs
        mockMvc.perform(post("/transactions")
                .content(asJsonString(transactionRequestDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

// package nl.inholland.codegeneration.controllers;

// import nl.inholland.codegeneration.configuration.apiTestConfiguration;
// import nl.inholland.codegeneration.controllers.TransactionController;
// import nl.inholland.codegeneration.models.DTO.request.TransactionRequestDTO;
// import nl.inholland.codegeneration.services.TransactionService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.mockito.MockitoAnnotations;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.context.annotation.Import;
// import org.springframework.http.MediaType;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import com.fasterxml.jackson.databind.ObjectMapper;

// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import java.math.BigDecimal;

// @SpringBootTest
// @AutoConfigureMockMvc
// @Import(apiTestConfiguration.class)
// public class TransactionControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private TransactionService transactionService;

//     @MockBean
//     private TransactionController transactionController;

//     // private TransactionRequestDTO transactionRequestDTO;

//     @BeforeEach
//     public void setup() {
//         // setup before each test
//         MockitoAnnotations.initMocks(this);
//         mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
//     }

//     @MockBean
//     TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(
//             "accountFromIban",
//             "accountToIban",
//             new BigDecimal("1000"),
//             "description");

//     @Test
//     @WithMockUser(username = "user", roles = {"EMPLOYEE"})
//     public void getAllTransactions_Returns200() throws Exception {
//         // Setup
//         // Assuming you would setup a list of TransactionResponseDTO objects here

//         // Execute & Verify
//         mockMvc.perform(get("/transactions")
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     @WithMockUser(username = "user", roles = {"EMPLOYEE"})
//     public void getTransactionById_Returns200() throws Exception {
//         // Setup
//         // Assuming you would setup a TransactionResponseDTO object here

//         // Execute & Verify
//         mockMvc.perform(get("/transactions/1")
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     @WithMockUser(username = "user", roles = {"EMPLOYEE"})
//     public void addTransaction_Returns201() throws Exception {
//         // Setup
//         TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(
//                 "accountFromIban",
//                 "accountToIban",
//                 new BigDecimal("1000"),
//                 "description");
    
//         ObjectMapper objectMapper = new ObjectMapper();
//         String transactionRequestJson = objectMapper.writeValueAsString(transactionRequestDTO);
    
//         // Execute & Verify
//         mockMvc.perform(post("/transactions")
//                 .content(transactionRequestJson)
//                 .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isCreated());
//     }
// }
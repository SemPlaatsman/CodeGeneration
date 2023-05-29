import nl.inholland.codegeneration.controllers.TransactionController;
import nl.inholland.codegeneration.models.DTO.request.TransactionRequestDTO;
import nl.inholland.codegeneration.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private TransactionRequestDTO transactionRequestDTO;

    @BeforeEach
    public void setup() {
        // setup before each test
    }

    @Test
    public void getAllTransactions_Returns200() throws Exception {
        // Setup
        // Assuming you would setup a list of TransactionResponseDTO objects here

        // Execute & Verify
        mockMvc.perform(get("/transactions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getTransactionById_Returns200() throws Exception {
        // Setup
        // Assuming you would setup a TransactionResponseDTO object here

        // Execute & Verify
        mockMvc.perform(get("/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void addTransaction_Returns201() throws Exception {
        // Setup
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(null, null, null, null);
        // Assuming you would setup the properties of the DTO here

        // Execute & Verify
        mockMvc.perform(post("/transactions")
                .content(/* You would need to serialize the TransactionRequestDTO to JSON */)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
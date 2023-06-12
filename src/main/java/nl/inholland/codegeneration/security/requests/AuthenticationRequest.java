package nl.inholland.codegeneration.security.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @Schema(example = "testUser")
    @NotNull(message = "Please fill the username field!")
    @NotEmpty(message = "Please fill the username field!")
    private String username;

    @Schema(example = "test123")
    @NotNull(message = "Please fill the password field!")
    @NotEmpty(message = "Please fill the password field!")
    private String password;
}

package RomanoPietro.u5d11.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewDipendenteDTO(
        @NotEmpty(message = "L'username é obbligatorio")
        @Size(min = 2, max = 20, message = "L'username deve essere compreso tra 2 e 20 caratteri!")
        String username,

        @NotEmpty(message = "Il nome proprio é obbligatorio!")
        @Size(min = 2, max = 30, message = "Il nome proprio deve essere compreso tra 2 e 30 caratteri!")
        String nome,

        @NotEmpty(message = "Il cognome proprio é obbligatorio")
        @Size(min = 2, max = 30, message = "Il cognome proprio deve essere compreso tra 2 e 30 caratteri!")
        String cognome,

        @NotEmpty(message = "L'email è un campo obbligatorio!")
        @Size(message = "L'email inserita non è un'email valida!")
        String email,

        @NotEmpty(message = "La password è un campo obbligatorio!")
        @Size(min = 4, message = "La password deve avere almeno 4 caratteri!")
        String password) {
}

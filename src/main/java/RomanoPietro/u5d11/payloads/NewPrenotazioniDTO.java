package RomanoPietro.u5d11.payloads;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewPrenotazioniDTO(

long dipendenteId,

long viaggioId,

@NotEmpty(message = "La data di richiesta non può essere vuota.")
String dataRichiesta,

@NotEmpty(message = "Le preferenze non possono essere vuote.")
@Size(min = 2, max = 255, message = "Le preferenze non possono superare i 255 caratteri.")
String preferenze
) {
}

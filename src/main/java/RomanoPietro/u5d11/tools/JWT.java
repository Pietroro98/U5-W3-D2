package RomanoPietro.u5d11.tools;

import RomanoPietro.u5d11.entities.Dipendente;
import RomanoPietro.u5d11.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWT {
    @Value("${jwt.secret}") // Il segreto sarà salvato in env.properties e letto da application.properties
    private String secret;
    public String createToken(Dipendente dipendente) {
        // La classe Jwts ha principalmente due metodi: builder() e parser(), rispettivamente serviranno per creare e verificare i token
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis())) // Data di emissione del Token (IAT - Issued At), va messa in millisecondi
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // Data di scadenza del Token (Expiration Date), anche questa in millisecondi
                .subject(String.valueOf(dipendente.getId())) // Subject, ovvero a chi appartiene il token <-- N.B. NON METTERE DATI SENSIBILI QUA DENTRO!!
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())) // Firmo il token, per poterlo fare devo utilizzare un algoritmo specifico HMAC e un segreto
                .compact(); // Assemblo il tutto nella stringa finale che sarà il mio token
    }
    public void verifyToken(String accessToken) {
        // Jwts.parser()
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build().parse(accessToken);
            // .parse() ci lancerà diversi tipi di eccezioni a seconda che il token sia stato o manipolato, o sia scaduto o sia malformato
        } catch (Exception ex) {
            throw new UnauthorizedException("Problemi con il token! Per favore effettua di nuovo il login!");
        }
    }

    public String getIdFromToken(String accessToken) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(accessToken)
                .getPayload().getSubject(); // All'interno del Payload, nel campo Subject avevamo inserito l'id dell'utente
    }
}
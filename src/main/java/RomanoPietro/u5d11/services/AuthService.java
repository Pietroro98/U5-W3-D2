package RomanoPietro.u5d11.services;

import RomanoPietro.u5d11.entities.Dipendente;
import RomanoPietro.u5d11.exceptions.UnauthorizedException;
import RomanoPietro.u5d11.payloads.UserLoginDTO;
import RomanoPietro.u5d11.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private DipendenteService dipendenteService;

    @Autowired
    private JWT jwt;

    @Autowired
    private PasswordEncoder bcrypt;


    public String checkCredentialsAndGenerateToken(UserLoginDTO body) {

        Dipendente found = this.dipendenteService.findByEmail(body.email());

        if (bcrypt.matches(body.password(), found.getPassword())) {

            String accessToken = jwt.createToken(found);
            return accessToken;

        } else {
            // 4. Se le credenziali sono errate --> 401 (Unauthorized)
            throw new UnauthorizedException("Credenziali errate!");
        }
    }
}
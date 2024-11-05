package RomanoPietro.u5d11.security;


import RomanoPietro.u5d11.entities.Dipendente;
import RomanoPietro.u5d11.exceptions.UnauthorizedException;
import RomanoPietro.u5d11.services.DipendenteService;
import RomanoPietro.u5d11.tools.JWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTCheckerFilter extends OncePerRequestFilter {
    @Autowired
    private JWT jwt;
    @Autowired
    private DipendenteService dipendenteService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire token nell'Authorization Header nel formato corretto!");
        String accessToken = authHeader.substring(7);
        jwt.verifyToken(accessToken);

        //*********************************** AUTORIZZAZIONE ****************************************************************

        // 1. Cerco l'utente tramite id (l'id l'abbiamo messo nel token!)
        String dipendenteId = jwt.getIdFromToken(accessToken);
        Dipendente currentDipendente = this.dipendenteService.findById(Long.parseLong(dipendenteId));

        // 2. Trovato l'utente posso associarlo al cosiddetto Security Context, questa è la maniera per Spring Security di associare l'utente alla
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(currentDipendente,null, currentDipendente.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
package concept.com.example.club.core.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import concept.com.example.club.common.exception.TokenExpiradoException;
import concept.com.example.club.core.user.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String chaveSecreta;
    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(chaveSecreta);
            return JWT.create()
                    .withClaim("role", user.getPlan().name())
                    .withIssuer("Rom Society")
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new IllegalStateException("Erro ao gerar token JWT", exception);
        }
    }

    public String validationToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(chaveSecreta);
            return JWT.require(algorithm)
                    .withIssuer("Rom Society")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException exception){
            throw new TokenExpiradoException("Token expirado");
        }
    }

    private Instant genExpirationDate() {
        return Instant.now().plusSeconds(2 * 60 * 60);

    }

}

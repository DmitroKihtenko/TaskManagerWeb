package ua.edu.sumdu.j2se.kikhtenkoDmytro.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.Token;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.Identity;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.VerifiedDuration;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.security.UserAuthentication;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtProcessor {
    private static final Logger logger =
            LoggerFactory.getLogger(JwtProcessor.class);
    private final VerifiedDuration leaveTime;
    private final Identity type;
    private ObjectMapper objectMapper;
    private SignatureAlgorithm algorithm;
    private JwtSecretKeeper secretKeeper;

    public JwtProcessor() {
        leaveTime = new VerifiedDuration("JWT token leave time",
                Duration.ofMinutes(30), true);
        algorithm = SignatureAlgorithm.HS512;
        secretKeeper = new JwtSecretKeeper();
        type = new Identity("JWT token type", "Bearer", true);
        objectMapper = new ObjectMapper();
    }

    @NonNull
    public Duration getLeaveTime() {
        return leaveTime.getValue();
    }

    public void setLeaveTime(@NonNull Duration leaveTime) {
        this.leaveTime.setValue(leaveTime);
    }

    @NonNull
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Autowired
    public void setObjectMapper(@NonNull ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @NonNull
    public SignatureAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(@NonNull SignatureAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @NonNull
    public JwtSecretKeeper getSecretKeeper() {
        return secretKeeper;
    }

    @Autowired
    public void setSecretKeeper(JwtSecretKeeper secretKeeper) {
        this.secretKeeper = secretKeeper;
    }

    @NonNull
    public String getType() {
        return type.getValue();
    }

    public void setType(@NonNull String type) {
        this.type.setValue(type);
    }

    public Token createToken(UserAuthentication auth)
            throws IOException {
        String secret = secretKeeper.getSecret(auth);
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("authentication", objectMapper.
                writeValueAsString(auth));
        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiration = issuedAt.plus(leaveTime.getValue());

        Token result = new Token();
        result.setType("Bearer");
        result.setJwtToken(Jwts.builder()
                .compressWith(CompressionCodecs.GZIP)
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiration))
                .setClaims(payload)
                .signWith(algorithm, secret.getBytes(
                        StandardCharsets.UTF_8))
                .compact());
        return result;
    }

    public UserAuthentication parseToken(
            @NonNull Token token) throws IOException {
        try {
            if(!token.getType().equals(getType())) {
                throw new IOException("Token type '" +
                        token.getType() + "' is not supported");
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String value = token.getJwtToken();
            Claims payload = Jwts.parser().
                    setCompressionCodecResolver(
                            new DefaultCompressionCodecResolver()).
                    parseClaimsJwt(value.substring(
                            0,
                            value.lastIndexOf('.') + 1)
                    ).getBody();
            UserAuthentication auth = objectMapper.readValue(
                    payload.get("authentication", String.class),
                    UserAuthentication.class
            );
            String secret = secretKeeper.getSecret(auth);
            Jwts.parser().
                    setCompressionCodecResolver(
                            new DefaultCompressionCodecResolver()).
                    setSigningKey(secret.getBytes(
                            StandardCharsets.UTF_8)).
                    parseClaimsJws(value).getBody();
            return auth;
        } catch (ExpiredJwtException e) {
            logger.debug(e.getMessage());

            throw new IOException("Authentication expired");
        } catch (JwtException | JsonProcessingException e) {
            logger.debug(e.getMessage());

            throw new IOException(
                    "Invalid authentication token"
            );
        }
    }
}

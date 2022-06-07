package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.lang.NonNull;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.Identity;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.JwtToken;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.VerifiedCustomType;

import java.util.Objects;

public class Token implements Replicable {
    private final JwtToken jwtToken;
    private final Identity type;

    public Token() {
        this.type = new Identity("Token type", "Bearer", true);
        this.jwtToken = new JwtToken("Jwt token", null,true);
    }

    public String getJwtToken() {
        return jwtToken.getValue();
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken.setValue(jwtToken);
        this.jwtToken.setAdjusted(true);
    }

    public String getType() {
        return type.getValue();
    }

    public void setType(String type) {
        this.type.setValue(type);
        this.type.setAdjusted(true);
    }

    @JsonIgnore
    @NonNull
    public Identity getTypeCover() {
        return type;
    }

    @JsonIgnore
    @NonNull
    public VerifiedCustomType<String> getJwtTokenCover() {
        return jwtToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Token)) {
            return false;
        }
        Token token = (Token) o;
        return jwtToken.equals(token.jwtToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jwtToken, type);
    }

    @Override
    public String toString() {
        return "Token{" +
                "jwtToken=" + jwtToken +
                "type=" + type +
                '}';
    }

    @Override
    public Token replicate() {
        Token value = new Token();
        value.update(this);
        return value;
    }

    @Override
    public void update(Object object) {
        if(object instanceof Token) {
            Token value = (Token) object;
            if(value.getJwtTokenCover().isAdjusted()) {
                setJwtToken(value.getJwtToken());
            }
            if(value.getTypeCover().isAdjusted()) {
                setType(value.getType());
            }
        }
    }
}

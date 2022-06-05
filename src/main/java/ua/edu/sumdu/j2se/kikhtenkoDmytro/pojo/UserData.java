package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo;

import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.BCryptHash;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.VerifiedCustomType;

public class UserData extends Authorization {
    @Override
    protected VerifiedCustomType<String> createPasswordCover() {
        return new BCryptHash("User password hash", null, false);
    }

    @Override
    public String toString() {
        return "UserData{" + super.toString() + "}";
    }
}

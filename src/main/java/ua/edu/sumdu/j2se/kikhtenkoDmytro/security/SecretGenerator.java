package ua.edu.sumdu.j2se.kikhtenkoDmytro.security;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.NonZeroAmount;

import java.util.HashSet;
import java.util.LinkedList;

@Component
public class SecretGenerator {
    private final PasswordGenerator generator;
    private final NonZeroAmount length;

    private HashSet<CharacterData> characters;

    public SecretGenerator() {
        generator = new PasswordGenerator();
        length = new NonZeroAmount(
                "Jwt token secret length", 16, true);
        characters = new HashSet<>();
        characters.add(EnglishCharacterData.Digit);
        characters.add(EnglishCharacterData.Alphabetical);
        characters.add(EnglishCharacterData.Special);
    }

    public Integer getLength() {
        return length.getValue();
    }

    public void setLength(@NonNull Integer length) {
        this.length.setValue(length);
    }

    @NonNull
    public HashSet<CharacterData> getCharacters() {
        return characters;
    }

    public void setCharacters(
            @NonNull HashSet<CharacterData> characters) {
        this.characters = characters;
    }

    @NonNull
    protected LinkedList<CharacterRule> createRules(
            @NonNull Iterable<CharacterData> characters) {
        LinkedList<CharacterRule> rules = new LinkedList<>();
        for(CharacterData characterData : characters) {
            rules.add(new CharacterRule(characterData));
        }
        return rules;
    }

    public String generate() {
        return generator.generatePassword(
                getLength(),
                createRules(characters)
        );
    }
}

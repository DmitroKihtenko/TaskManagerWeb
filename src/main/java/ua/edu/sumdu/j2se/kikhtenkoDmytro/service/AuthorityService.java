package ua.edu.sumdu.j2se.kikhtenkoDmytro.service;

import org.springframework.stereotype.Service;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.security.Authority;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.AuthorityData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

@Service
public class AuthorityService {
    public AuthorityData get(int id) {
        return new AuthorityData(id);
    }

    public ArrayList<AuthorityData> get() {
        HashSet<Authority> allAuthorities =
                Authority.getAllAuthorities();
        ArrayList<AuthorityData> authorities = new ArrayList<>(
                allAuthorities.size());
        for(Authority authority : allAuthorities) {
            authorities.add(new AuthorityData(authority));
        }
        authorities.sort(Comparator.comparingInt(
                AuthorityData::getId));
        return authorities;
    }
}

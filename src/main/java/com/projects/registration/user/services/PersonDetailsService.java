package com.projects.registration.user.services;

import com.projects.registration.security.security.PersonDetails;
import com.projects.registration.user.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {

    public static final String USER_NOT_FOUND_MSG = "user with email - %s not found";
    public final PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println(personRepository.findByEmail(email).get());
        return new PersonDetails(personRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(USER_NOT_FOUND_MSG.formatted(email))));
    }
}

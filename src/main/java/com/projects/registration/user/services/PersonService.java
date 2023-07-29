package com.projects.registration.user.services;

import com.projects.registration.user.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {
    public final PersonRepository personRepository;

    public void enablePerson(String email) {
        personRepository.enablePerson(email);
    }
}

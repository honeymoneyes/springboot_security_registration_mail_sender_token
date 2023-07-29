package com.projects.registration.registration.services;

import com.projects.registration.email.interfaces.EmailSender;
import com.projects.registration.email.utils.Email;
import com.projects.registration.registration.dto.RegistrationRequest;
import com.projects.registration.registration.token.entity.ConfirmationToken;
import com.projects.registration.registration.token.service.ConfirmationTokenService;
import com.projects.registration.registration.utils.EmailValidator;
import com.projects.registration.security.utils.PasswordEncoder;
import com.projects.registration.user.entities.Person;
import com.projects.registration.user.repositories.PersonRepository;
import com.projects.registration.user.roles.PersonRole;
import com.projects.registration.user.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private static final String CONFIRM_TOKEN = "http://localhost:8080/api/v1/registration/confirm?token=";
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final PersonService personService;
    private final EmailSender emailSender;

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator
                .test(request.email());

        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }
        String token = signUpPerson(Person.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(request.password())
                .personRole(PersonRole.USER)
                .enabled(false)
                .locked(false)
                .build());

        emailSender.send(request.email(), Email.buildEmail(request.firstName(), CONFIRM_TOKEN + token));

        return token;
    }

    public String signUpPerson(Person person) {
        boolean userExists = personRepository.findByEmail(person.getEmail()).isPresent();

        if (userExists) {
            // TODO If token expired but person is in person_db, then need change this step and send token again
            throw new IllegalStateException("email already taken");
        }

        String encodePassword = passwordEncoder.bCryptPasswordEncoder().encode(person.getPassword());

        person.setPassword(encodePassword);

        personRepository.save(person);

        ConfirmationToken confirmationToken = createToken(person);

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return confirmationToken.getToken();

    }

    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        personService.enablePerson(confirmationToken.getPerson().getEmail());

        return "confirmed";
    }

    private static ConfirmationToken createToken(Person person) {

        return ConfirmationToken.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .person(person)
                .build();
    }
}

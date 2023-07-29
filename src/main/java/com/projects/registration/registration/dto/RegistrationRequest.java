package com.projects.registration.registration.dto;

public record RegistrationRequest(String firstName,
                                  String lastName,
                                  String email,
                                  String password) {
}

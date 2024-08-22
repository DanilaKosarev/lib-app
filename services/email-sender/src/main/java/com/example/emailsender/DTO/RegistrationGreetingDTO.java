package com.example.emailsender.DTO;

public class RegistrationGreetingDTO {
    private String email;
    private String name;

    public RegistrationGreetingDTO() {
    }

    public RegistrationGreetingDTO(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

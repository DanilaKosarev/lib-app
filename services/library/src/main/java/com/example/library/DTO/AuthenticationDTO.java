package com.example.library.DTO;

import jakarta.validation.constraints.*;

public class AuthenticationDTO {
    @Email(message = "Must be in email format")
    @NotBlank(message = "Email should not be null or empty")
    @Size(max = 100, message = "Email address should be less than 100 characters")
    private String email;

    @NotBlank(message = "Password should not be null or empty")
    @Size(min = 6, max = 100, message = "Password should be in range of 6 to 100 characters")
    private String password;

    public AuthenticationDTO() {
    }

    public AuthenticationDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

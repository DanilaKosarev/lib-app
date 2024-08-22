package com.example.library.DTO;

import com.example.library.validation.ValidateEmailUniqueness;
import jakarta.validation.constraints.*;

public class RegistrationDTO {

    @NotBlank(message = "Name should not be null or empty")
    @Size(min = 2, max = 100, message = "Name should be in range of 2 to 100 characters")
    private String name;

    @Min(value = 16, message = "Age should be 16 or greater")
    private int age;

    @ValidateEmailUniqueness
    @Email(message = "Must be in email format")
    @NotBlank(message = "Email should not be null or empty")
    @Size(max = 100, message = "Email address should be less than 100 characters")
    private String email;

    @NotBlank(message = "Password should not be null or empty")
    @Size(min = 6, max = 100, message = "Password should be in range of 6 to 100 characters")
    private String password;

    public RegistrationDTO() {
    }

    public RegistrationDTO(String name, int age, String email, String password) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

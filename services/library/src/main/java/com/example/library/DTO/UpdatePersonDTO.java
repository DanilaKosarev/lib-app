package com.example.library.DTO;

import jakarta.validation.constraints.*;

public class UpdatePersonDTO {

    @NotBlank(message = "Name should not be null or empty")
    @Size(min=2, max = 100, message = "Name should be in range of 2 to 100 characters")
    private String name;

    @Min(value = 16, message = "Age should be greater than 0")
    private int age;

    public UpdatePersonDTO() {
    }

    public UpdatePersonDTO(String name, int age) {
        this.name = name;
        this.age = age;
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

}

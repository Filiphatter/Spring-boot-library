package com.example.librarysystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationRequestDTO {
    @NotBlank(message = "Förnamn är obligatoriskt")
    @Size(min = 2, max = 50, message = "Förnamn måste vara mellan 2 och 50 tecken")
    private String firstName;

    @NotBlank(message = "Efternamn är obligatoriskt")
    @Size(min = 2, max = 50, message = "Efternamn måste vara mellan 2 och 50 tecken")
    private String lastName;

    @NotBlank(message = "E-post är obligatoriskt")
    @Email(message = "E-postformatet är ogiltigt")
    private String email;

    @NotBlank(message = "Lösenord är obligatoriskt")
    @Size(min = 8, max = 64, message = "Lösenord måste vara mellan 8 och 64 tecken")
    private String password;
    //construktor
    public RegistrationRequestDTO() {}

    //getter n setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

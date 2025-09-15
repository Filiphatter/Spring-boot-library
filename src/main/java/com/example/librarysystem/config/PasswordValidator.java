package com.example.librarysystem.config;

import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {
    public boolean isValid(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");

        return hasLetter && hasDigit;
    }
    public String getPasswordRequirements() {
        return "Lösenord måste vara minst 8 tecken långt och innehålla både bokstäver och siffror";
    }
}

package com.example.librarysystem.service;

import com.example.librarysystem.dto.UserDTO;
import com.example.librarysystem.dto.UserRequestDTO;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {

        private final UserRepository userRepository;

        public UserService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        public Optional<UserDTO> getUserByEmail(String email) {
            return userRepository.findByEmail(email)
                    .map(this::mapToUserDTO);
        }

    public UserDTO createUser(UserRequestDTO dto) {
        validateUserInput(dto);

        // Kontrollera om e-post redan finns
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-postadressen är redan registrerad.");
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRegistrationDate(LocalDate.now());

        User saved = userRepository.save(user);
        return mapToUserDTO(saved);
    }

    private void validateUserInput(UserRequestDTO dto) {
        if (dto.getFirstName() == null || dto.getFirstName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Förnamn är obligatoriskt.");
        }
        if (dto.getLastName() == null || dto.getLastName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Efternamn är obligatoriskt.");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-postadress är obligatorisk.");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lösenord är obligatoriskt.");
        }
    }

        //mapper
        private UserDTO mapToUserDTO(User user) {
            UserDTO dto = new UserDTO();
            dto.setUserId(user.getUserID());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setRegistrationDate(user.getRegistrationDate());
            return dto;
        }
    }

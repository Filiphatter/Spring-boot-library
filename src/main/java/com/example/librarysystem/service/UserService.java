package com.example.librarysystem.service;

import com.example.librarysystem.config.PasswordValidator;
import com.example.librarysystem.dto.RegistrationRequestDTO;
import com.example.librarysystem.dto.UserDTO;
import com.example.librarysystem.dto.UserRequestDTO;
import com.example.librarysystem.entity.Role;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.repository.RoleRepository;
import com.example.librarysystem.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

        private final UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordValidator passwordValidator;

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

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "USER-roll finns inte"));

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // krypterar den gammla metoden också
        user.setRegistrationDate(LocalDate.now());
        user.getRoles().add(userRole); //också updaterat för nya uppgiften

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
        if (!passwordValidator.isValid(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, passwordValidator.getPasswordRequirements());
        } //updaterar validering
    }

    //nya metoder för security uppgifter
    public UserDTO registerUser(RegistrationRequestDTO dto) throws Exception {
            //kontroller email
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new Exception("En användare finns redan med denna epost.");
        }

        //Validera lösenord
        if (!passwordValidator.isValid(dto.getPassword())) {
            throw new Exception(passwordValidator.getPasswordRequirements());
        }
        //get USER-rollen
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new Exception("USER-roll finns inte i systemet"));

        //skapa användare
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); //själva krypteringen
        user.getRoles().add(userRole);

        // spara användare
        User savedUser = userRepository.save(user);
        //retunera användare utan lösenord
        return mapToUserDTO(savedUser);
    }


    @Transactional // migrera lösenord till BC (ai)
    public String migrateExistingPasswords() {
        List<User> users = userRepository.findAll();
        int updated = 0;

        for (User user : users) {
            String currentPassword = user.getPassword();
            // Kolla om lösenordet redan är BCrypt-krypterat (börjar med $2a$)
            if (!currentPassword.startsWith("$2a$")) {
                String encryptedPassword = passwordEncoder.encode(currentPassword);
                user.setPassword(encryptedPassword);
                userRepository.save(user);
                updated++;
                System.out.println("Migrerade lösenord för: " + user.getEmail());
            }
        }

        return "Migrerade " + updated + " lösenord från klartext till BCrypt";
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

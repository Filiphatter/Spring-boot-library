package com.example.librarysystem.service;

import com.example.librarysystem.config.PasswordValidator;
import com.example.librarysystem.dto.RegistrationRequestDTO;
import com.example.librarysystem.dto.UserDTO;
import com.example.librarysystem.dto.UserRequestDTO;
import com.example.librarysystem.entity.Role;
import com.example.librarysystem.entity.User;
import com.example.librarysystem.repository.RoleRepository;
import com.example.librarysystem.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

        private final UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordValidator passwordValidator;

    @Autowired
    private Validator validator; //spring validation

        public UserService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        public Optional<UserDTO> getUserByEmail(String email) {
            return userRepository.findByEmail(email)
                    .map(this::mapToUserDTO);
        }

    public UserDTO createUser(UserRequestDTO dto) {
        //Validera DTO-reglerna manuellt
        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder(); //Nytt (kanske inte) som jag fick hjälp av ai. Stringbuilder är str1 + str2 som en string. alltså addera ihop strings.
            for (ConstraintViolation<UserRequestDTO> violation : violations) {
                sb.append(violation.getMessage()).append("; "); //append konverterar andra datatyper til strängar (kanske inte nytt utan jag bara glömt)
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, sb.toString());
        }

        //Extra lösenordsvalidering med din PasswordValidator
        if (!passwordValidator.isValid(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, passwordValidator.getPasswordRequirements());
        }

        //Kolla e-post unikhet
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-postadressen är redan registrerad.");
        }

        //Skapa användare
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "USER-roll finns inte"));

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRegistrationDate(LocalDate.now());
        user.getRoles().add(userRole);

        User saved = userRepository.save(user);
        return mapToUserDTO(saved);
    }


    public UserDTO registerUser(RegistrationRequestDTO dto) {
        // Validera DTO-reglerna (NotBlank, Size, Email)
        Set<ConstraintViolation<RegistrationRequestDTO>> violations = validator.validate(dto); //kör userrequestdto genom validator
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder(); //skapa stringbuilder
            for (ConstraintViolation<RegistrationRequestDTO> violation : violations) {
                sb.append(violation.getMessage()).append("; "); //loopar igenom objektet med fel
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, sb.toString()); //här samlas fel
            //en vanlig exception är "allmän" aka spring vet inte vad de betyder. Response... kan man skriva vilken http statuskod och meddelande
            //detta fallet är det bad request = 400 alltså en dålig request.
        }

        // Validera lösenordspolicyn
        if (!passwordValidator.isValid(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, passwordValidator.getPasswordRequirements());
        }

        // Kontrollera att e-post inte redan finns
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-postadressen är redan registrerad.");
            //Conflict är intressant iom man skicka en bra request jämfört med en 400, men systemet accepterar inte det detta fall eg unique constraint i DB.
        }

        // Hämta USER-roll
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "USER-roll finns inte i systemet"));

        // Skapa användare
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // kryptera lösenord
        user.getRoles().add(userRole);

        // Spara och returnera DTO
        User savedUser = userRepository.save(user);
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

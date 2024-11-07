package com.securityModel.controllers;

import com.securityModel.modele.User;
import com.securityModel.payload.request.LoginRequest;
import com.securityModel.payload.request.SignupRequest;
import com.securityModel.payload.request.TokenRefreshRequest;
import com.securityModel.payload.response.JwtResponse;
import com.securityModel.payload.response.MessageResponse;
import com.securityModel.payload.response.TokenRefreshResponse;
import com.securityModel.repository.UserDao;
import com.securityModel.security.services.UserDetailsImpl;
import com.securityModel.security.services.AuthService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.UUID;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;



    @Autowired
    private UserDao userRepository;

    @Autowired
    private JavaMailSender emailSender;


    @PostMapping("/signin")
    // La méthode prend en paramètre un objet LoginRequest qui est validé avec @Valid
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // Cette méthode renvoie un objet JwtResponse contenant le jeton JWT et éventuellement d'autres informations utiles
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        // Renvoie une réponse HTTP 200 (OK) contenant le JwtResponse en tant que corps de la réponse
        // La méthode ResponseEntity.ok() crée une réponse avec le statut HTTP 200 et le contenu passé en paramètre
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws MessagingException {
        MessageResponse messageResponse = authService.registerUser(signUpRequest);
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        TokenRefreshResponse tokenRefreshResponse = authService.refreshToken(request);
        return ResponseEntity.ok(tokenRefreshResponse);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MessageResponse messageResponse = authService.logoutUser(userDetails);
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping("/signoutProvider")
    public ResponseEntity<?> logoutProvider() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MessageResponse messageResponse = authService.logoutProvider(userDetails);
        return ResponseEntity.ok(messageResponse);
    }
    @PostMapping(value = "/signupProvider", consumes = { "multipart/form-data" })
    public ResponseEntity<?> signupProvider(SignupRequest signUpRequest, MultipartFile file) throws MessagingException {
        MessageResponse messageResponse = authService.registerProvider(signUpRequest, file);
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping("/signinProvider")
    public ResponseEntity<?> authenticateProvider(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateProvider(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }
    @GetMapping("/confirmProvider")
    public ResponseEntity<?> confirmProvider(@RequestParam String email) {
        MessageResponse messageResponse = authService.confirmProvider(email);
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping("/signupCustomer")
    public ResponseEntity<?> signupCustomer(@Valid @RequestBody SignupRequest signUpRequest) throws MessagingException {
        MessageResponse messageResponse = authService.registerCustomer(signUpRequest);
        return ResponseEntity.ok(messageResponse);
    }
    @GetMapping("/confirmCustomer")
    public ResponseEntity<?> confirmCustomer(@RequestParam String email) {
        MessageResponse messageResponse = authService.confirmCustomer(email);
        return ResponseEntity.ok(messageResponse);
    }
    @PostMapping("/signinCustomer")
    public ResponseEntity<?> authenticateCustomer(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateCustomer(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }
    @PostMapping("/signoutCustomer")
    public ResponseEntity<?> logoutCustomer() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MessageResponse messageResponse = authService.logoutCustomer(userDetails);
        return ResponseEntity.ok(messageResponse);
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmUser(@RequestParam String email) {
        MessageResponse messageResponse = authService.confirmUser(email);
        return ResponseEntity.ok(messageResponse);
    }


    @PostMapping("/forgotpassword")
        public HashMap<String,String> resetPassword(String email) throws MessagingException {
            HashMap<String, String> response = new HashMap<>();
            // Rechercher l'utilisateur par son email
            User user = userRepository.findByEmail(email);
            if (user == null) {
                response.put("message", "Aucun utilisateur trouvé avec cet email");
                return response;
            }
            UUID forget = UUID.randomUUID();
            user.setPasswordResetToken(forget.toString());
            user.setId(user.getId());
            String from="admin@gmail.com";
            String to=user.getEmail();
            MimeMessage message=emailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message);
            helper.setSubject("forget password");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setText("votre code est : " +user.getPasswordResetToken(), true);
            emailSender.send(message);
            userRepository.saveAndFlush(user);
            response.put("user", "user found, check your email");
            return response;
        }

        @PostMapping("/resetpassword/{passwordResetToken}")
        public HashMap<String,String> savePassword(@PathVariable String passwordResetToken, String newPassword){
          User user = userRepository.findByPasswordResetToken(passwordResetToken);
          HashMap message = new HashMap();

            if (user != null) {
                user.setId(user.getId());
                user.setPassword(new BCryptPasswordEncoder().encode(newPassword)); // Assuming you're hashing the password in the setter
                userRepository.save(user); // Save the updated user

                message.put("resetpassword", "proccesed");
                return message;
            } else {
                message.put("resetpassword", "failed");
                return message;
            }
        }
    @PostMapping("/changepassword")
    public HashMap<String, String> changePassword(
            @RequestParam String token,
            @RequestParam String currentPassword,
            @RequestParam String newPassword
    ) {
        HashMap<String, String> response = new HashMap<>();

        // Retrieve the user by token
        User user = userRepository.findByPasswordResetToken(token);
        if (user == null) {
            response.put("message", "Invalid token");
            return response;
        }

        // Verify the current password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            response.put("message", "Current password is incorrect");
            return response;
        }

        // Update the password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordResetToken(null); // Clear the reset token after changing password
        userRepository.save(user);

        response.put("message", "Password changed successfully");
        return response;
    }
}

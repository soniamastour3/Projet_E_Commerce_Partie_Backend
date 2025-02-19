package com.securityModel.security.services;
import com.securityModel.Utils.StoresService;
import com.securityModel.exception.TokenRefreshException;
import com.securityModel.modele.Customer;
import com.securityModel.modele.Provider;
import com.securityModel.modele.User;
import com.securityModel.models.*;
import com.securityModel.payload.request.LoginRequest;
import com.securityModel.payload.request.SignupRequest;
import com.securityModel.payload.request.TokenRefreshRequest;
import com.securityModel.payload.response.JwtResponse;
import com.securityModel.payload.response.MessageResponse;
import com.securityModel.payload.response.TokenRefreshResponse;
import com.securityModel.repository.ClientDao;
import com.securityModel.repository.ProviderDao;
import com.securityModel.repository.RoleRepository;
import com.securityModel.repository.UserDao;
import com.securityModel.security.jwt.JwtUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDao userRepository;

    @Autowired
    ProviderDao providerRepository;

    @Autowired
    ClientDao customerRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private  StoresService storesService;



    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        Optional<User> u = userRepository.findByUsername(loginRequest.getUsername());
        if (u.get().isConfirm() == true) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwt = jwtUtils.generateJwtToken(userDetails);

            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            return new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(),
                    userDetails.getEmail(), roles);
        } else {
            throw new RuntimeException("user not confirm");
        }
    }

    public JwtResponse authenticateProvider(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        Optional<User> u = userRepository.findByUsername(loginRequest.getUsername());
        if (u.isPresent() && u.get() instanceof Provider provider) {

            // Debugging: Log the confirmation status
            System.out.println("Provider confirmation status: " + provider.isConfirm());

            if (provider.isConfirm() == true) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwt = jwtUtils.generateJwtToken(userDetails);

            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            return new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(),
                    userDetails.getEmail(), roles);
        } else {
            throw new RuntimeException("Provider not confirm");
        }} else {
            throw new RuntimeException("Provider not found.");
        }
    }

    public JwtResponse authenticateCustomer(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        Optional<User> u = userRepository.findByUsername(loginRequest.getUsername());
        if (u.isPresent() && u.get() instanceof Customer customer) {

            // Debugging: Log the confirmation status
            System.out.println("Customer confirmation status: " + customer.isConfirm());

            if (customer.isConfirm() == true) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                String jwt = jwtUtils.generateJwtToken(userDetails);

                List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                        .collect(Collectors.toList());

                RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

                return new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(),
                        userDetails.getEmail(), roles);
            } else {
                throw new RuntimeException("Customer not confirm");
            }} else {
            throw new RuntimeException("Customer not found.");
        }
    }
    public MessageResponse registerUser(SignupRequest signUpRequest) throws MessagingException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new MessageResponse("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!");
        }

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        User user;

        // Assign roles and create Provider or Customer
        if (strRoles != null && strRoles.contains("ROLE_PROVIDER")) {
            user = new Provider(signUpRequest.getUsername(), signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()), signUpRequest.getCompany());
            roles.add(roleRepository.findByName(ERole.ROLE_PROVIDER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        } else if (strRoles != null && strRoles.contains("ROLE_CUSTOMER")) {
            user = new Customer(signUpRequest.getUsername(), signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()), signUpRequest.getLocation());
            roles.add(roleRepository.findByName(ERole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        } else {
            user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()));
            roles.add(roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
        }

        user.setConfirm(false); // Ensure user is not confirmed initially
        user.setRoles(roles);
        userRepository.save(user);

        sendConfirmationEmail(signUpRequest);

        return new MessageResponse("User registered successfully!");
    }

    public  MessageResponse registerProvider(SignupRequest signUpRequest) throws MessagingException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new MessageResponse("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!");
        }

        // Create Provider
        Provider provider = new Provider(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getCompany()
        );


        // Set roles and save the provider
        Set<Role> roles = new HashSet<>();
        Role providerRole = roleRepository.findByName(ERole.ROLE_PROVIDER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        provider.setConfirm(false);
        roles.add(providerRole);
        provider.setRoles(roles);

        providerRepository.save(provider);
        sendConfirmationEmailProvider(signUpRequest);
        // Send confirmation email or handle additional logic

        return new MessageResponse("Provider registered successfully!");
    }

    public  MessageResponse registerCustomer(SignupRequest signUpRequest) throws MessagingException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new MessageResponse("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!");
        }

        // Create Provider
        Customer customer = new Customer(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getLocation()
        );

        // Set roles and save the provider
        Set<Role> roles = new HashSet<>();
        Role customerRole = roleRepository.findByName(ERole.ROLE_PROVIDER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        customer.setConfirm(false);
        roles.add(customerRole);
        customer.setRoles(roles);

        customerRepository.save(customer);
        sendConfirmationEmailCustomer(signUpRequest);
        // Send confirmation email or handle additional logic

        return new MessageResponse("Client registered successfully!");
    }

    private void sendConfirmationEmail(SignupRequest SignupRequest) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Complete Registration");
        String from="admin@gmail.com";
        String to= SignupRequest.getEmail();
        helper.setFrom(from);
        helper.setTo(to);
        String emailContent= ("<HTML><body><a href=\"http://localhost:8085/api/auth/confirm?email="+SignupRequest.getEmail()
                +"\">Confirm</a></body></HTML>");
        helper.setText(emailContent, true);
        emailSender.send(message);}
       /* helper.setFrom("soniamastour.sm@gmail.com");
        helper.setTo(user.getEmail());
        String confirmationUrl = "http://localhost:8088/api/auth/confirm?email=" + user.getEmail();
        String emailContent = "<html><body><p>Click the link below to complete your registration:</p>" +
                "<a href=\"" + confirmationUrl + "\">Confirm</a></body></html>";

        helper.setText(emailContent, true);
        emailSender.send(message);*/

    private void sendConfirmationEmailProvider(SignupRequest SignupRequest) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setSubject("Complete Registration");
        String from="admin@gmail.com";
        String to= SignupRequest.getEmail();
        helper.setFrom(from);
        helper.setTo(to);
        String emailContent= ("<HTML><body><a href=\"http://localhost:8085/api/auth/confirmProvider?email="+SignupRequest.getEmail()
                +"\">Confirm</a></body></HTML>");
        helper.setText(emailContent, true);
        emailSender.send(message);}

    private void sendConfirmationEmailCustomer(SignupRequest SignupRequest) throws MessagingException {
        MimeMessage messageP = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(messageP);
        helper.setSubject("Complete Registration");
        String from="admin@gmail.com";
        String to= SignupRequest.getEmail();
        helper.setFrom(from);
        helper.setTo(to);
        String emailContent= ("<HTML><body><a href=\"http://localhost:8085/api/auth/confirmCustomer?email="+SignupRequest.getEmail()
                +"\">Confirm</a></body></HTML>");
        helper.setText(emailContent, true);
        emailSender.send(messageP);}

    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return new TokenRefreshResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }

    public MessageResponse confirmUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user.isConfirm()) {
            return new MessageResponse("User is already confirmed.");
        }
        user.setConfirm(true);
        userRepository.save(user);
        return new MessageResponse("User confirmed successfully!");
    }
    public MessageResponse confirmProvider(String email) {
        Provider provider = providerRepository.findByEmail(email);
        if (provider.isConfirm()) {
            return new MessageResponse("provider is already confirmed.");
        }
        provider.setConfirm(true);
        providerRepository.save(provider);
        return new MessageResponse("provider confirmed successfully!");
    }

    public MessageResponse confirmCustomer(String email) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer.isConfirm()) {
            return new MessageResponse("client is already confirmed.");
        }
        customer.setConfirm(true);
        customerRepository.save(customer);
        return new MessageResponse("client confirmed successfully!");
    }



    public MessageResponse logoutUser(UserDetailsImpl userDetails) {
        Long userId = userDetails.getId();
        refreshTokenService.deleteByUserId(userId);
        return new MessageResponse("Log out successful!");
    }

    public MessageResponse logoutProvider(UserDetailsImpl userDetails) {
        Long providerId = userDetails.getId(); // Get the provider's ID from the UserDetailsImpl
        refreshTokenService.deleteByUserId(providerId); // Delete the refresh token for this provider
        return new MessageResponse("Provider log out successful!"); // Return a success message
    }

    public MessageResponse logoutCustomer(UserDetailsImpl userDetails) {
        Long CustomerId = userDetails.getId(); // Get the customer's ID from the UserDetailsImpl
        refreshTokenService.deleteByUserId(CustomerId); // Delete the refresh token for this customer
        return new MessageResponse("Customer log out successful!"); // Return a success message
    }

}

package com.a00n.springsecurityjwtexample.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.a00n.springsecurityjwtexample.dto.AuthenticationRequest;
import com.a00n.springsecurityjwtexample.entities.Token;
import com.a00n.springsecurityjwtexample.entities.User;
import com.a00n.springsecurityjwtexample.repositories.RoleRepository;
import com.a00n.springsecurityjwtexample.repositories.TokenRepository;
import com.a00n.springsecurityjwtexample.repositories.UserRepository;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    public String register(User user) {
        if (userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail()).orElse(null) != null) {
            return null;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var userRole = roleRepository.findByName("ROLE_USER").orElse(null);
        user.setRoles(Set.of(userRole));
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return jwtToken;
    }

    private void saveUserToken(User savedUser, String jwtToken) {
        var token = Token.builder()
                .user(savedUser)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public String authenticate(AuthenticationRequest request) {
        try {
            System.out.println(request);
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            var user = userRepository.findByUsernameOrEmail(request.getEmail(), request.getEmail()).orElse(null);
            // System.out.println("a00na00na00na00na00na00na00na00na00na00na00na00na00na00na00n");
            System.out.println(user);
            var jwtToken = jwtService.generateToken(user);
            // revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return jwtToken;
        } catch (Exception e) {
            System.out
                    .println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + e.getMessage());
            return null;
        }
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}

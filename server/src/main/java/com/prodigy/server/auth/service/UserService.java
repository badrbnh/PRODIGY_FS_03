package com.prodigy.server.auth.service;

import com.prodigy.server.auth.exception.AuthenticationException;
import com.prodigy.server.auth.model.TokenResponse;
import com.prodigy.server.auth.model.User;
import com.prodigy.server.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author badreddine
 **/
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private ApplicationContext context;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public User register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public TokenResponse verify(User user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateJWT(user.getEmail());
        } else {
            throw  new AuthenticationException("User is not authenticated");
        }
    }

    public TokenResponse refresh(String refreshToken) {
        String username = jwtService.extractUserName(refreshToken);
        UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);

        if (jwtService.validateToken(refreshToken, userDetails)) {
            String newAccessToken = jwtService.generateToken(username, TokenType.ACCESS_TOKEN);
            return new TokenResponse(newAccessToken, refreshToken);
        } else {
            throw new AuthenticationException("Token is not valid");
        }
    }
}

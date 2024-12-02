package com.prodigy.server.auth.service;

import com.prodigy.server.auth.model.User;
import com.prodigy.server.auth.model.UserPrincipals;
import com.prodigy.server.auth.repository.UserRepository;
import com.sun.security.auth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author badreddine
 **/
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        if (user==null){
            throw new UsernameNotFoundException("User 404");
        }
        return new UserPrincipals(user);
    }

}

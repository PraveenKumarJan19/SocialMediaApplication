package com.javatechie.config;

import com.javatechie.entity.request.User;
import com.javatechie.repository.UserRepository;
import org.hibernate.validator.internal.util.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.logging.Logger;

@Component
public class UserInfoUserDetailsService implements UserDetailsService {

    private final Logger logger = Logger.getLogger(UserInfoUserDetailsService.class.getSimpleName());

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userInfo = repository.findByActualUserName(username);
        try {
            return new UserInfoUserDetails(userInfo.orElseThrow());
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            throw new UsernameNotFoundException("user not found " + username);
        }
    }
}

package com.web.security;

import com.web.domain.User;
import com.web.domain.UserRole;
import com.web.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRepository.findUserByUserId(username);
        CustomUser customUser = new CustomUser(user);
        return customUser;
    }

    public User save(User user) {
        user.setUserPw(passwordEncoder.encode(user.getUserPw()));
        UserRole role=new UserRole();
        role.setRoleName("BASIC");
        user.setRoles(Arrays.asList(role));
        return usersRepository.save(user);
    }
}

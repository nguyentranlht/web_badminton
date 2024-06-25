package com.example.webBadminton.service;

import com.example.webBadminton.model.Role;
import com.example.webBadminton.model.User;
import com.example.webBadminton.model.UserPrincipal;
import com.example.webBadminton.repository.IRoleRepository;
import com.example.webBadminton.repository.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        return processUser(user);
    }

    private OAuth2User processUser(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(oAuth2User.getAttribute("name"));
            newUser.setPassword(new BCryptPasswordEncoder().encode(email));
            newUser.setUsername(email);
            newUser.setRole(roleRepository.getReferenceById(roleRepository.getRoleIdByName("User")));
            // Set additional necessary attributes, perhaps fetch them from oAuth2User
            userRepository.save(newUser);
            return newUser;
        });
        return new DefaultOAuth2User(oAuth2User.getAuthorities(), oAuth2User.getAttributes(), "email");
    }

}
package com.example.webBadminton.service;

import com.example.webBadminton.model.Role;
import com.example.webBadminton.model.User;
import com.example.webBadminton.model.UserPrincipal;
import com.example.webBadminton.repository.IRoleRepository;
import com.example.webBadminton.repository.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
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
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        Map<String, Object> attributes = user.getAttributes();
        String email = (String) attributes.get("email");  // Correctly fetch the email

        // Fetch additional details like roles from the database
        Long id = userRepository.getUserIdByEmail(email);
        User appUser = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Email not found: " + email)
        );
        appUser.setRole(roleRepository.getReferenceById(userRepository.getRoleId(id)));

        System.out.println(appUser.getUsername() + " " + appUser.getPassword() + " " + appUser.getRole().getName());
        return new UserPrincipal(appUser, attributes);
    }
}
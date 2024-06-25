package com.example.webBadminton.service;

import com.example.webBadminton.model.Role;
import com.example.webBadminton.model.User;
import com.example.webBadminton.repository.IRoleRepository;
import com.example.webBadminton.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    public void save(User user) {
        try {
            userRepository.save(user);
            Long userId = userRepository.getUserIdByUsername(user.getUsername());
            Long role = roleRepository.getRoleIdByName("USER");
            if (role != 0 && userId != 0)
                userRepository.addRoleToUser(userId, role);
        } catch (Exception e) {
            System.err.println("An error occurred while saving the user: " + e.getMessage());
            throw e;
        }
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            Long userId = user.getId();
            String[] roleNames = userRepository.getRolesOfUser(userId);
            for (String roleName : roleNames) {
                Role role = roleRepository.findRoleById(roleRepository.getRoleIdByName(roleName));
                user.getRoles().add(role);
            }
        }
        return users;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            Long userId = user.getId();
            String[] roleNames = userRepository.getRolesOfUser(userId);
            for (String roleName : roleNames) {
                Role role = roleRepository.findRoleById(roleRepository.getRoleIdByName(roleName));
                user.getRoles().add(role);
            }
        }
        return user;
    }

    public void updateUserRoles(Long id, List<Long> roleIds) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            Set<Role> roles = roleIds.stream()
                    .map(roleRepository::findRoleById)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
            userRepository.save(user);
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


}

package com.example.webBadminton.service;

import com.example.webBadminton.model.Role;
import com.example.webBadminton.model.User;
import com.example.webBadminton.repository.IRoleRepository;
import com.example.webBadminton.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void save(User user) {
        try {
            user.passwordEncryption(user.getPassword());
            userRepository.save(user);
            Long userId = userRepository.getUserIdByUsername(user.getUsername());
            Long role = roleRepository.getRoleIdByName("Super Admin");
            if (role != 0 && userId != 0)
                userRepository.addRoleToUser(userId, role);
        } catch (Exception e) {
            System.err.println("An error occurred while saving the user: " + e.getMessage());
            throw e;
        }
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateUser(@NotNull User user) {
        User existedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalStateException("User with ID " + user.getId() + " does not exist."));
        existedUser.setName(user.getName());
        existedUser.setUsername(user.getUsername());
        existedUser.passwordEncryption(user.getPassword());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(existedUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User with ID " + id + " does not exist."));
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public void updateUserRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with ID " + userId + " does not exist."));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalStateException("Role with ID " + roleId + " does not exist."));
        user.setRole(role);
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

package com.example.webBadminton.service;

import com.example.webBadminton.model.CustomUserDetail;
import com.example.webBadminton.model.User;
import com.example.webBadminton.repository.IRoleRepository;
import com.example.webBadminton.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    public void save(User user) {
        try {
            userRepository.save(user);
            Long userId = userRepository.getUserIdByUsername(user.getUsername());
            Long role = roleRepository.getRoleIdByName("Super Admin");
            if (role != 0 && userId != 0)
                userRepository.addRoleToUser(userId, role);
        } catch (Exception e) {
            // Log the exception details (you can use any logging framework)
            System.err.println("An error occurred while saving the user: " + e.getMessage());
            // Re-throw if necessary or handle accordingly
            throw e;
        }
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateUser(@NotNull User user) {
        try {
            User existedUser = userRepository.findById(user.getId())
                    .orElseThrow(() -> new IllegalStateException("User with ID " + user.getId() + " does not exist."));
            existedUser.setName(user.getName());
            existedUser.setUsername(user.getUsername());
            existedUser.setPassword(user.getPassword());
        } catch (Exception e) {
            // Log the exception details (you can use any logging framework)
            System.err.println("An error occurred while updating the user: " + e.getMessage());
            // Re-throw if necessary or handle accordingly
            throw e;
        }
    }

    public Optional<User> getUserById(Long userId)
    {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByCurentId(){
        return userRepository.findById(getCurrentUserId());
    }

    public List<User> findOwners() {
        return userRepository.findByRolesName("Owner");
    }


    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetail) {
            CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
            return userDetails.getId();
        }
        return null; // or handle differently if user is not authenticated
    }
}

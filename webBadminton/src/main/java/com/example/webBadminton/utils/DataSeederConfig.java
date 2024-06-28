package com.example.webBadminton.utils;

import com.example.webBadminton.model.Role;
import com.example.webBadminton.model.User;
import com.example.webBadminton.model.location.Province;
import com.example.webBadminton.repository.IRoleRepository;
import com.example.webBadminton.repository.IUserRepository;
import com.example.webBadminton.repository.location.IDistrictRepository;
import com.example.webBadminton.repository.location.IProvinceRepository;
import com.example.webBadminton.repository.location.IWardRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Component
public class DataSeederConfig implements CommandLineRunner {
    private static final String FILE_PATH = "classpath:static/data/geographical_administrative_levels.json";
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IProvinceRepository provinceRepository;
    @Autowired
    private IDistrictRepository districtRepository;
    @Autowired
    private IWardRepository wardRepository;
    @Autowired
    private ResourceLoader resourceLoader;

    public void loadJsonDataFromFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Resource resource = resourceLoader.getResource(FILE_PATH);
        if (resource.exists()) {
            // Read the resource as an InputStream and convert to List of Provinces
            List<Province> provinces = mapper.readValue(resource.getInputStream(), new TypeReference<List<Province>>() {
            });

            for (Province province : provinces) {
                // Check and set the relationship for districts and wards
                province.getDistricts().forEach(district -> {
                    district.setProvince(province);
                    if (district.getWards() != null) {
                        district.getWards().forEach(ward ->
                        {
                            if (ward.getId() != null) {
                                ward.setDistrict(district);
                            } else {
                                System.out.println("\nDistrict " + district.getName() + " have null Ward");
                                district.setWards(null);
                            }
                        });
                    }
                });
                provinceRepository.save(province);  // Save the province, cascading the saves to districts and wards
            }
        } else {
            throw new IOException("File not found: " + FILE_PATH);
        }
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Create roles if they don't exist
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("Super Admin"));
            roleRepository.save(new Role("Admin"));
            roleRepository.save(new Role("User"));
            roleRepository.save(new Role("Owner"));
        }

        // Create super admin if not exists
        if (userRepository.count() == 0) {
            try {
                User superAdmin = new User("superadmin", passwordEncoder.encode("superadmin"), "Super Admin");
                userRepository.save(superAdmin);
                Long userId = userRepository.getUserIdByUsername(superAdmin.getUsername());
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

        if (provinceRepository.count() == 0) {
            try {
                loadJsonDataFromFile();
            } catch (Exception e) {
                // Log the exception details (you can use any logging framework)
                System.err.println("An error occurred while saving the user: " + e.getMessage());
                // Re-throw if necessary or handle accordingly
                throw e;
            }
        }
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}

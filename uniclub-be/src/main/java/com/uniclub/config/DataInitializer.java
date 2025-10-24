package com.uniclub.config;

import com.uniclub.entity.Role;
import com.uniclub.entity.User;
import com.uniclub.repository.RoleRepository;
import com.uniclub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create roles if they don't exist
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole.setDescription("Administrator role");
            adminRole.setStatus((byte) 1);
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("CUSTOMER").isEmpty()) {
            Role customerRole = new Role();
            customerRole.setName("CUSTOMER");
            customerRole.setDescription("Customer role");
            customerRole.setStatus((byte) 1);
            roleRepository.save(customerRole);
        }

        // Create admin user if it doesn't exist
        if (userRepository.findByEmail("admin@uniclub.vn").isEmpty()) {
            User adminUser = new User();
            adminUser.setEmail("admin@uniclub.vn");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setFullname("Admin User");
            adminUser.setRole(roleRepository.findByName("ADMIN").orElse(null));
            adminUser.setStatus((byte) 1);
            userRepository.save(adminUser);
        }

        // Create customer user if it doesn't exist
        if (userRepository.findByEmail("user@uniclub.vn").isEmpty()) {
            User customerUser = new User();
            customerUser.setEmail("user@uniclub.vn");
            customerUser.setPassword(passwordEncoder.encode("user123"));
            customerUser.setFullname("Customer User");
            customerUser.setRole(roleRepository.findByName("CUSTOMER").orElse(null));
            customerUser.setStatus((byte) 1);
            userRepository.save(customerUser);
        }
    }
}

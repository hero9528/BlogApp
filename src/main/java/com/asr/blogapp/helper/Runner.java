package com.asr.blogapp.helper;

import com.asr.blogapp.entity.MyUserEntity;
import com.asr.blogapp.entity.Role;
import com.asr.blogapp.repositry.RoleRepositry;
import com.asr.blogapp.repositry.UserRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private RoleRepositry roleRepositry;

    @Autowired
    private UserRepositry  userRepositry;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        createRoleNotExists("ADMIN");
        createRoleNotExists("GUEST");
        createAdminUserIfNotExists();

    }

    private void createRoleNotExists(String roleName) {
      Role role = roleRepositry.findByName(roleName);

      if (role == null) {
          Role newRole = new Role();
          newRole.setName(roleName);
          roleRepositry.save(newRole);

      }
    }

    private void createAdminUserIfNotExists() {

        String adminEmail = "deep@gmail.com";


        MyUserEntity existingAdminUser = userRepositry.findByEmail(adminEmail);
        if (existingAdminUser == null) {
            MyUserEntity newAdminUser = new MyUserEntity();
            newAdminUser.setFirstName("myadmin");
            newAdminUser.setLastName("user");
            newAdminUser.setEmail(adminEmail);
            newAdminUser.setPassword(passwordEncoder.encode("asr123"));

            Role role = roleRepositry.findByName("ADMIN");
            List<Role> roleList = Arrays.asList(role);
            newAdminUser.setRoles(roleList);
            userRepositry.save(newAdminUser);
        }

    }
}

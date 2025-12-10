package com.runcible.abbot.application;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.runcible.abbot.model.User;
import com.runcible.abbot.service.UserService;

@Component
public class PasswordMigrationRunner implements ApplicationRunner 
{
    Logger logger = Logger.getLogger(PasswordMigrationRunner.class.getName());

    @Autowired
    private UserService userService; // Assuming you have a UserRepository

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception 
    {
        List<User> users = userService.findAll();
        for (User user : users) 
        {
            if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) 
            {
                logger.info("Encoding password for user: " + user.getEmail());
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                userService.updateUser(user);
            }
        }
    }
}

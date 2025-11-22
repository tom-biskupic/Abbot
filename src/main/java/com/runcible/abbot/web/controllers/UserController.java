package com.runcible.abbot.web.controllers;

import java.security.Principal;
import java.util.logging.Logger;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.runcible.abbot.model.User;
import com.runcible.abbot.service.UserService;
import com.runcible.abbot.service.exceptions.DuplicateUserException;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.web.model.ValidationResponse;

@RestController
public class UserController
{
    Logger logger = Logger.getLogger(UserController.class.getName());

	@GetMapping(value="/user")
	public Principal user(Principal user) 
	{
	    return user;
	}
	
    @GetMapping(value="/userlist")
    public ModelAndView showPage()
    {
        return new ModelAndView("userlist");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN'")
    @GetMapping(value="/userlist.json")
    public @ResponseBody Page<User> showList(Pageable p)
    {
        return userService.findAll(p);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN'") 
    @GetMapping(value="/user.json/{id}")
    public @ResponseBody User showUser(@PathVariable("id") Integer id) throws NoSuchUser
    {
        return userService.findByID(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN'")
    @PostMapping(value="/user.json")
    public @ResponseBody ValidationResponse editUser(
            @Valid @RequestBody User user,
            BindingResult       result)
    {
        ValidationResponse response = new ValidationResponse();
        
        if ( result.hasErrors() )
        {
            response.setErrorMessageList(result.getAllErrors());
            response.setStatus("FAIL");
        }
        else
        {
            try
            {
                if ( user.getId() != null )
                {
                    userService.updateUser(user);
                }
                else
                {
                    userService.addUser(user);
                }

                response.setStatus("SUCCESS");
            }
            catch (DuplicateUserException e)
            {
                result.addError(new FieldError(
                        result.getObjectName(),
                        "email",
                        user.getEmail(),
                        false,
                        null,
                        null,
                        "The email address specified is already registered"));
                response.setErrorMessageList(result.getAllErrors());
                response.setStatus("FAIL");
            }
        }
        return response;
    }

    @GetMapping(value="/register")
    public ModelAndView showForm()
    {
        ModelAndView mav = new ModelAndView("registerform");
        mav.addObject("User",new User());
        return mav;
    }
        
    @PostMapping(value="/register")
    public @ResponseBody ValidationResponse registerUser(
            @Valid @RequestBody User 	user,
            BindingResult           	result )
    {
        ValidationResponse response = new ValidationResponse();

        if ( result.hasErrors() )
        {
            response.setErrorMessageList(result.getAllErrors());
            response.setStatus("FAIL");
        }
        else
        {
            try
            {
                userService.addUser(user);
                response.setStatus("SUCCESS");
            }
            catch (DuplicateUserException e)
            {
                result.addError(new FieldError(
                        result.getObjectName(),
                        "email",
                        user.getEmail(),
                        false,
                        null,
                        null,
                        "The email address specified is already registered"));
                response.setErrorMessageList(result.getAllErrors());
                response.setStatus("FAIL");
            }
        }
        
        return response;
    }
    
    @Autowired
    private UserService userService;
}

package com.runcible.abbot.web.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.security.Principal;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.runcible.abbot.model.User;
import com.runcible.abbot.service.UserService;
import com.runcible.abbot.service.exceptions.DuplicateUserException;
import com.runcible.abbot.service.exceptions.NoSuchUser;
import com.runcible.abbot.web.model.ValidationResponse;

@Controller
@RestController
public class UserController
{
	@RequestMapping(value="/user",method=GET)
	public Principal user(Principal user) 
	{
	    return user;
	}
	
    @RequestMapping(value="/userlist",method=GET)
    public ModelAndView showPage()
    {
        return new ModelAndView("userlist");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN'")
    @RequestMapping(value="/userlist.json",method=GET)
    public @ResponseBody Page<User> showList(Pageable p)
    {
        return userService.findAll(p);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN'") 
    @RequestMapping(value="/user.json/{id}",method=GET)
    public @ResponseBody User showUser(@PathVariable("id") Integer id) throws NoSuchUser
    {
        return userService.findByID(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN'")
    @RequestMapping(value="/user.json",method=POST)
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

    @RequestMapping(value="/register",method=GET)
    public ModelAndView showForm()
    {
        ModelAndView mav = new ModelAndView("registerform");
        mav.addObject("User",new User());
        return mav;
    }
        
    @RequestMapping(value="/register",method=POST)
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

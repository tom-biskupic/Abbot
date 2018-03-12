package com.runcible.abbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.runcible.abbot.model.User;
import com.runcible.abbot.repository.UserRepository;
import com.runcible.abbot.service.exceptions.DuplicateUserException;
import com.runcible.abbot.service.exceptions.NoSuchUser;

@Component
@Transactional
public class UserServiceImpl implements UserService
{

    @Override
    public User validateLogon(String email, String password)
    {
        User user = userRepo.findByEmailAndPassword(email.trim(), password.trim());
        return user;
    }

    @Override
    @Transactional(readOnly=false)
    public void addUser(User user) throws DuplicateUserException
    {
        if (userRepo.findByEmail(user.getEmail()) != null )
        {
            throw new DuplicateUserException();
        }

        //
        //  if this is the first user created, make them an administrator
        //
        
        if ( userRepo.count() == 0 )
        {
            user.setAdministrator(true);
        }
        
        userRepo.save(user);
    }

    @Override
    public void updateUser(User user) throws DuplicateUserException
    {
        User sameEmailUser = userRepo.findByEmail(user.getEmail());
        
        //
        //  If this email address is the same as that of a different user
        //  then we throw
        //
        if ( sameEmailUser != null && sameEmailUser.getId() != user.getId())
        {
            throw new DuplicateUserException();            
        }
        
        userRepo.save(user);
    }


    @Override
    public Page<User> findAll(Pageable page)
    {
        return userRepo.findAll(page);
    }

    @Override
    public User findByID(Integer id) throws NoSuchUser
    {
        User user = userRepo.findOne(id);
        
        if (user == null)
        {
            throw new NoSuchUser();
        }
        
        return user;
    }

    @Override
    public User findByEmail(String name) throws NoSuchUser
    {
        User user = userRepo.findByEmail(name);
        
        if ( user == null )
        {
            throw new NoSuchUser();
        }
        
        return user;
    }


    @Autowired
    UserRepository userRepo;

}

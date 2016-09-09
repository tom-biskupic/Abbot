package com.runcible.abbot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.runcible.abbot.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>
{
    public User findByEmailAndPassword(
            String email,
            String password );
    
    public User findByEmail(String email);
    
    public Page<User> findAll(Pageable pageable);
}

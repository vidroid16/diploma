package com.shalya.diploma.repositories;

import com.shalya.diploma.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByLogin(String login);

    Optional<User> findById(Long id);

    Optional<User> findByLoginAndPassword(String login, String password);

}
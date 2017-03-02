package com.timeout.chatbot.repository;

import com.timeout.chatbot.domain.UserJpa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJpaRepository extends CrudRepository<UserJpa, Long> {

    List<UserJpa> findByLastName(String lastName);

}
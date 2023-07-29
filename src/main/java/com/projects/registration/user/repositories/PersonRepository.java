package com.projects.registration.user.repositories;

import com.projects.registration.user.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Person p SET p.enabled = TRUE WHERE p.email = ?1")
    void enablePerson(String email);
}

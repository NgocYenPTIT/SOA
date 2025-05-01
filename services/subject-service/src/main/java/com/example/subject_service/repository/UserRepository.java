package com.example.subject_service.repository;

import com.example.subject_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    // Spring Data JPA sẽ tự động tạo các phương thức cơ bản
}
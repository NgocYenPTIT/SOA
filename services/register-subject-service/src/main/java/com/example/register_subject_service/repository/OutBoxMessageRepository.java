package com.example.register_subject_service.repository;

import com.example.register_subject_service.model.OutBoxMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutBoxMessageRepository extends JpaRepository<OutBoxMessage, Long> {

    List<OutBoxMessage> findAllByDeletedAtIsNull( Pageable pageable);
}
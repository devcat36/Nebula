package com.albireo.nebula.repository;

import com.albireo.nebula.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, String> {
    Optional<Session> findBySessionIdAndExpireAfter(String sessionId, Date expire);
}

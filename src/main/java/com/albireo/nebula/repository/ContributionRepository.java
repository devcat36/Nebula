package com.albireo.nebula.repository;

import com.albireo.nebula.model.Contribution;
import com.albireo.nebula.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContributionRepository extends JpaRepository<Contribution, Long> {
    List<Contribution> findByUser(User user);
    Optional<Contribution> findByContributionId(Long contributionId);
}

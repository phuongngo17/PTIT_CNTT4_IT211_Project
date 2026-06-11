package org.example.badminton.repository;

import org.example.badminton.model.entity.BadmintonCluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadmintonClusterRepository extends JpaRepository<BadmintonCluster, Long> {
}

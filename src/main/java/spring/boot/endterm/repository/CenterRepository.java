package spring.boot.endterm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.boot.endterm.entity.Center;

import java.util.Optional;
import java.util.UUID;

public interface CenterRepository extends JpaRepository<Center , UUID> {
    Optional<Center> findById(Long id);
}

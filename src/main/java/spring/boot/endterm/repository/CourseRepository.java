package spring.boot.endterm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.boot.endterm.entity.Course;

import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    Optional<Course> findById(Long id);
}

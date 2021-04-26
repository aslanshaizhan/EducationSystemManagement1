package spring.boot.endterm.service.course;

import org.springframework.http.ResponseEntity;
import spring.boot.endterm.dto.request.CourseEditRequest;
import spring.boot.endterm.dto.request.CourseRequest;
import spring.boot.endterm.entity.Course;

public interface CourseService {
    ResponseEntity<?> newCourse(CourseRequest request);
    Course findById(Long id);
    void save(Course course);
    ResponseEntity<?> editCourse(CourseEditRequest request);
    ResponseEntity<?> allCourses();
    ResponseEntity<?> deleteCourse(Long id);
}

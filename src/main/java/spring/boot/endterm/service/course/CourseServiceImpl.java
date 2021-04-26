package spring.boot.endterm.service.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import spring.boot.endterm.dto.request.CourseEditRequest;
import spring.boot.endterm.dto.request.CourseRequest;
import spring.boot.endterm.entity.Center;
import spring.boot.endterm.entity.Course;
import spring.boot.endterm.exceptions.CustomNotFoundException;
import spring.boot.endterm.repository.CourseRepository;
import spring.boot.endterm.service.center.CenterService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseServiceImpl implements CourseService{

    private final CourseRepository repository;
    private final CenterService centerService;

    @Autowired
    public CourseServiceImpl(CourseRepository repository, CenterService centerService) {
        this.repository = repository;
        this.centerService = centerService;
    }

    @Override
    public ResponseEntity<?> newCourse(CourseRequest request) {
        Center center = centerService.findById(request.getCenterId());
        Course course = new Course(request , center);
        save(course);
        Map<String  , Object> result = new HashMap<>();
        result.put("message" , "Успешно добавлен");
        result.put("status" , HttpStatus.OK);
        return new ResponseEntity<>(result , HttpStatus.OK);
    }

    @Override
    public Course findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new CustomNotFoundException(String.format("" +
                "Course with id : %s does not exist" , id)));
    }

    @Override
    public void save(Course course) {
        repository.save(course);
    }

    @Override
    public ResponseEntity<?> editCourse(CourseEditRequest request) {
        Course course = findById(request.getId());
        course.setName(request.getName());
        course.setPrice(request.getPrice());
        Map<String , Object> result = new HashMap<>();
        result.put("message" , "Successfully edited");
        result.put("status" , HttpStatus.OK);
        return new ResponseEntity<>(result , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> allCourses() {
        List<Course> courses = repository.findAll();
        return new ResponseEntity<>(courses , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteCourse(Long id) {
        Course course = findById(id);
        course.setUsers(null);
        course.setCenter(null);
        save(course);
        repository.delete(course);
        return new ResponseEntity<>("Deleted course with id : " + id , HttpStatus.OK);
    }
}

package spring.boot.endterm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.boot.endterm.dto.request.CourseEditRequest;
import spring.boot.endterm.dto.request.CourseRequest;
import spring.boot.endterm.service.course.CourseService;
import spring.boot.endterm.service.user.UserService;

@RestController
@RequestMapping("/api/v1/admin-system")
public class AdminController {

    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public AdminController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @PostMapping(value = "/new/course")
    public ResponseEntity<?> newCourse(@RequestBody CourseRequest request){
        return courseService.newCourse(request);
    }

    @GetMapping(value = "/courses")
    public ResponseEntity<?> courses(){
        return courseService.allCourses();
    }

    @PutMapping(value = "/edit/course")
    public ResponseEntity<?> editCourse(@RequestBody CourseEditRequest request){
        return courseService.editCourse(request);
    }

    @DeleteMapping(value = "/delete/course/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id){
        return courseService.deleteCourse(id);
    }

    @PutMapping(value = "/block/user/{id}")
    public ResponseEntity<?> blockUser(@PathVariable Long id){
        return userService.blockUser(id);
    }
}

package spring.boot.endterm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import spring.boot.endterm.dto.request.ChangePasswordRequest;
import spring.boot.endterm.dto.request.EditUserRequest;
import spring.boot.endterm.service.user.UserService;

@RestController
@RequestMapping("/api/v1/system")
@PreAuthorize("isAuthenticated()")
public class SystemController {

    private final UserService userService;

    @Autowired
    public SystemController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/test")
    public String test(){
        return userService.getAuthentication().getLogin();
    }

    @PutMapping(value = "/add/balance/{balance}")
    public ResponseEntity<?> addBalance(@PathVariable int balance){
        return userService.addBalance(balance);
    }

    @PutMapping(value = "/buy/course/{id}")
    public ResponseEntity<?> buyCourse(@PathVariable Long id){
        return userService.buyCourse(id);
    }

    @PutMapping(value = "/unsubscribe/course")
    public ResponseEntity<?> unsubscribeCourse(){
        return userService.unsubscribeCourse();
    }

    @PutMapping(value = "/edited/profile")
    public ResponseEntity<?> editProfile(@RequestBody EditUserRequest request){
        return userService.editProfile(request);
    }

    @PutMapping(value = "/change/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request){
        return userService.changePassword(request);
    }

}

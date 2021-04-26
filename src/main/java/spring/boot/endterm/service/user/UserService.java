package spring.boot.endterm.service.user;

import org.springframework.http.ResponseEntity;
import spring.boot.endterm.dto.request.ChangePasswordRequest;
import spring.boot.endterm.dto.request.EditUserRequest;
import spring.boot.endterm.dto.request.LoginRequest;
import spring.boot.endterm.dto.request.RegisterRequest;
import spring.boot.endterm.dto.response.TokenResponse;
import spring.boot.endterm.entity.User;

public interface UserService {
    void save(User user);
    ResponseEntity<?> register(RegisterRequest request);
    ResponseEntity<?> login(LoginRequest request);
    User findByLogin(String login);
    User getAuthentication();
    ResponseEntity<TokenResponse> refreshToken(String token);
    ResponseEntity<?> blockUser(Long id);
    User findById(Long id);
    ResponseEntity<?> buyCourse(Long id);
    ResponseEntity<?> addBalance(int balance);
    ResponseEntity<?> unsubscribeCourse();
    ResponseEntity<?> editProfile(EditUserRequest request);
    ResponseEntity<?> changePassword(ChangePasswordRequest request);
}

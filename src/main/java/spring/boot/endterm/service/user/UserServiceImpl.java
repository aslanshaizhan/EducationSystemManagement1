package spring.boot.endterm.service.user;

import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import spring.boot.endterm.constants.ClaimKeysConstants;
import spring.boot.endterm.dto.request.ChangePasswordRequest;
import spring.boot.endterm.dto.request.EditUserRequest;
import spring.boot.endterm.dto.request.LoginRequest;
import spring.boot.endterm.dto.request.RegisterRequest;
import spring.boot.endterm.dto.response.TokenResponse;
import spring.boot.endterm.entity.Course;
import spring.boot.endterm.entity.Role;
import spring.boot.endterm.entity.User;
import spring.boot.endterm.exceptions.BadRequestException;
import spring.boot.endterm.exceptions.CustomBadRequestException;
import spring.boot.endterm.exceptions.CustomConflictException;
import spring.boot.endterm.exceptions.CustomNotFoundException;
import spring.boot.endterm.repository.UserRepository;
import spring.boot.endterm.service.course.CourseService;
import spring.boot.endterm.service.role.RoleService;
import spring.boot.endterm.service.token.TokenService;
import spring.boot.endterm.service.userDetails.UserDetailsImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final TokenService tokenService;
    private final CourseService courseService;

    @Autowired
    public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder passwordEncoder, RoleService roleService, TokenService tokenService, CourseService courseService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.tokenService = tokenService;
        this.courseService = courseService;
    }

    @Override
    public void save(User user) {
        repository.save(user);
    }

    @Override
    public ResponseEntity<?> register(RegisterRequest request) {
        boolean existsByLogin = repository.existsByLogin(request.getLogin());
        if (existsByLogin){
            throw new CustomConflictException(String.format("User with login : %s already exists" , request.getLogin()));
        }
        List<Role> userRole = new ArrayList<>();
        userRole.add(roleService.findByName("ROLE_USER"));
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User newUser = new User(request , userRole);
        save(newUser);
        Map<String , Object> result = new HashMap<>();
        result.put("message" , "Successfully registered");
        result.put("status" , HttpStatus.OK);
        return new ResponseEntity<>(result , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TokenResponse> refreshToken(String token) {
        if (!tokenService.tokenValidation(token))
            throw new BadRequestException("It's not valid refresh token");

        DefaultClaims claims = tokenService.getClaimsFromToken(token);
        Map<String, Object> newClaims = new HashMap<>();
        if (!claims.get(ClaimKeysConstants.IS_REFRESH_TOKEN, Boolean.class))
            throw new BadRequestException("It's not valid refresh token");

        String login = claims.get(ClaimKeysConstants.USERNAME, String.class);

        TokenResponse tokensResponse;

        User userLogin = findByLogin(login);
        List<String> roles = new ArrayList<>();
        userLogin.getRoles().stream().forEach(x -> roles.add(x.getName()));
        newClaims.put(ClaimKeysConstants.USERNAME, userLogin.getLogin());
        tokensResponse = tokenService.generateTokensResponse(claims);

        // tokensResponse.setType(getAccountType(user.getRoles()));
        return new ResponseEntity<>(tokensResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> blockUser(Long id) {
        User user = findById(id);
        user.setBlocked(true);
        save(user);
        return new ResponseEntity<>("User with id : "+id+" blocked" , HttpStatus.OK);
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(()->new CustomNotFoundException(String.format("" +
                "User with id : %s not found" , id)));
    }

    @Override
    public ResponseEntity<?> buyCourse(Long id) {
        User user = getAuthentication();
        Course course = courseService.findById(id);
        List<Course> userCourses = new ArrayList<>();
        if (user.getBalance()>=course.getPrice()) {
            userCourses.add(course);
            user.setCourses(userCourses);
            return new ResponseEntity<>("Bought course", HttpStatus.OK);
        }else {
            throw new CustomConflictException("Your balance is not enough");
        }
    }

    @Override
    public ResponseEntity<?> addBalance(int balance) {
        User user = getAuthentication();
        user.setBalance(balance);
        return new ResponseEntity<>("Added balance" , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> unsubscribeCourse() {
        User user = getAuthentication();
        user.setCourses(null);
        save(user);
        return new ResponseEntity<>("unsubscribed"  ,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> editProfile(EditUserRequest request) {
        User user = getAuthentication();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        save(user);
        return new ResponseEntity<>("edited" , HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> changePassword(ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.getOldPasswd() , getAuthentication().getPassword())){
            throw new CustomConflictException("Confirm old password");
        }
        if (!request.getNewPasswd().equals(request.getConfPasswd())){
            throw new CustomConflictException("confirm new password");
        }
        User user = getAuthentication();
        user.setPassword(passwordEncoder.encode(request.getNewPasswd()));
        save(user);
        return new ResponseEntity<>("changed" , HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> login(LoginRequest request) {
        User user =findByLogin(request.getLogin());
        if (!passwordEncoder.matches(request.getPassword() , user.getPassword()))
            throw new CustomBadRequestException("Bad credentials");
        if (user.isBlocked())
            throw new CustomConflictException("Your account is blocked");
        Map<String, Object> claims = new HashMap<>();
        claims.put(ClaimKeysConstants.USERNAME, user.getLogin());
        List<String> roles = new ArrayList<>();
        user.getRoles().forEach(role -> roles.add(role.getName()));
        claims.put(ClaimKeysConstants.ROLES, roles);
        TokenResponse tokensResponse = tokenService.generateTokensResponse(claims);
        String role = "";
        for (Role r : user.getRoles()){
            role = r.getName();
        }
        tokensResponse.setRole(role);
        return new ResponseEntity<>(tokensResponse, HttpStatus.OK);

    }

    @Override
    public User findByLogin(String login) {
        return repository.findByLogin(login).orElseThrow(() -> new CustomNotFoundException(
                String.format("User with login : %s does not exist" , login)
        ));
    }

    @Override
    public User getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            UserDetailsImpl securityUser = (UserDetailsImpl) authentication.getPrincipal();
            User user = findByLogin(securityUser.getUsername());
            return user;
        }
        return null;

    }
}

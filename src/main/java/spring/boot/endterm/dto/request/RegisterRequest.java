package spring.boot.endterm.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String login;
    private String password;

}

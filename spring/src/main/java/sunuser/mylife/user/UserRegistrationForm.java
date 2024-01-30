package sunuser.mylife.user;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

@Data
public class UserRegistrationForm{
    private String username;
    private String password;

    public User toUser(PasswordEncoder encoder) {
        return new User(username, encoder.encode(password));
    }
}

package sunuser.mylife.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import sunuser.mylife.property.StorageProperties;
import sunuser.mylife.user.UserRegistrationForm;
import sunuser.mylife.user.UserRepository;

@RestController
public class AuthController {

    private final UserRepository userRep;
    private final PasswordEncoder passwordEncoder;
	private final StorageProperties storageProperties;

	public AuthController(StorageProperties storageProperties, PasswordEncoder passwordEncoder,
			UserRepository userRep) {
		this.storageProperties = storageProperties;
		this.passwordEncoder = passwordEncoder;
		this.userRep = userRep;
	}

	@GetMapping("/login")
	public String login() {
		return "logged in";
	}

    @PostMapping("/register")
    public String register(@RequestBody UserRegistrationForm form) {
        userRep.save(form.toUser(passwordEncoder));
		
		return storageProperties.getLocation();
    }

}

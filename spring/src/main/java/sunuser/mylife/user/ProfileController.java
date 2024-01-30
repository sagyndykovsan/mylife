package sunuser.mylife.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfileController {

	private final ProfileService profileService;

	@GetMapping("/search")
	public List<UserDTO> searchPeople(@RequestParam(required = false) String u) {
		System.out.println(u);
		return profileService.searchPeople(u);
	}

	@GetMapping("/profile-image/{filename}")
	public ResponseEntity<Resource> getProfileImage(@PathVariable String filename, HttpServletRequest request) {
		return profileService.getProfileImage(filename, request);
	}

	@GetMapping("/{username}")
	public UserDTO getUser(@PathVariable String username) {
		return profileService.getProfile(username);
	}

	@GetMapping("/{username}/following")
	public List<UserDTO> getUserFollowing(@PathVariable String username) {
		return profileService.getUserFollowing(username);
	}

	@GetMapping("/{username}/followers")
	public List<UserDTO> getUserFollowers(@PathVariable String username) {
		return profileService.getUserFollowers(username);
	}

	@PostMapping("/{username}/follow")
	public String followUser(@PathVariable String username, @AuthenticationPrincipal User user) {
		return profileService.followUser(username, user);
	}


	@PatchMapping("/profile")
	public User patchUser(@RequestParam("image") MultipartFile image,
						  @AuthenticationPrincipal User user,
						  HttpServletRequest request) {

		return profileService.patchUser(image, user, request);
	}
}

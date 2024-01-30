package sunuser.mylife.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sunuser.mylife.service.FileStorageService;
import sunuser.mylife.service.JwtService;
import sunuser.mylife.service.UrlUtil;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

	private final UserRepository userRepository;
	private final FileStorageService storage;
	private final UrlUtil urlUtil;
	private final JwtService jwtService;
	private final UserDTOMapper userDTOMapper;



	public ResponseEntity<Resource> getProfileImage(String filename, HttpServletRequest request) {
		Resource resource = storage.loadAsResource(filename);

		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			throw new RuntimeException();
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.body(resource);
	}


	public UserDTO getProfile(String username) {
		return userRepository.getUserByUsername(username);
	}

	public User patchUser(MultipartFile image, User user, HttpServletRequest request) {
		String filename = storage.store(image);
		String baseUrl = urlUtil.getBaseUrl(request);

		user.setImageUrl(baseUrl + "/profile-image/" + filename);

		return userRepository.save(user);
	}

    public List<UserDTO> getUserFollowing(String username) {
		return userRepository.getUserFollowing(username);
    }

	public List<UserDTO> getUserFollowers(String username) {
		return userRepository.getUserFollowers(username);
	}

	@Transactional
	public String followUser(String username, User user) {
		User userToFollow = userRepository.findByUsername(username);
		userToFollow.follow(user);

		userRepository.save(userToFollow);
		userRepository.save(user);

		return "followed";
	}

	public List<UserDTO> searchPeople(String username) {
		return userRepository.findByUsernameLike(username);
	}
}

package sunuser.mylife.user;

import java.util.function.Function;

import org.springframework.stereotype.Service;

@Service
public class UserDTOMapper {
	public UserDTO apply(User user, String imageUrl) {
		return new UserDTO(
				user.getId(),
				user.getUsername(),
				imageUrl,
				3L,
				3L,
				3L
				);
	}
}

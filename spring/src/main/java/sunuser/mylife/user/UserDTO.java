package sunuser.mylife.user;

public record UserDTO (
		Long id,
		String username,
		String imageUrl,
		Long postAmount,
		Long followingAmount,
		Long followersAmount
		){
}
package sunuser.mylife.post;

import org.springframework.data.repository.CrudRepository;

import sunuser.mylife.user.User;

public interface PostLikeRepository extends CrudRepository<PostLike, PostLikeId> {
	public boolean existsByUserAndPost(User user, Post post);
	public void deleteByUserAndPost(User user, Post post);
}

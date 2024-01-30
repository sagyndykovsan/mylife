package sunuser.mylife.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {

//	@Query("select new sunuser.mylife.post.PostDTO(p.id post_id," +
//				" p.description," +
//				" p.mediaUrl," +
//				" p.createdAt," +
//				" p.user.id," +
//				" p.user.username," +
//				" p.user.imageUrl," +
//				" count(distinct l), " +
//				" count(distinct c.id)," +
//				" CASE WHEN l.user.id = :userId THEN true ELSE false END)" +
////				" coalesce(l.user.id = :userId, false))" +
//			" from Post p" +
//			" left join p.likes l" +
//			" left join p.comments c" +
//			" group by p.id, p.description, l.user.id, p.mediaUrl, p.createdAt, p.user.id, p.user.username, p.user.imageUrl")
////			" group by p.id, p.description, p.mediaUrl, p.createdAt, p.user.id, p.user.username, p.user.imageUrl")
@Query("SELECT DISTINCT new sunuser.mylife.post.PostDTO(" +
		" p.id, p.description, p.mediaUrl, p.createdAt, p.user.id, " +
		" p.user.username, p.user.imageUrl, " +
		" count(distinct l.user.id), count(distinct c.id), " +
		" CASE WHEN MAX(CASE WHEN l.user.id = :userId THEN 1 ELSE 0 END) = 1 THEN true ELSE false END)" +
		" FROM Post p " +
		" LEFT JOIN p.likes l " +
		" LEFT JOIN p.comments c " +
		" GROUP BY p.id, p.description, p.mediaUrl, p.createdAt, p.user.id, p.user.username, p.user.imageUrl")
	Page<PostDTO> getAllPosts(@Param("userId") Long userId, Pageable pageable);
	// current

	@Query("SELECT DISTINCT new sunuser.mylife.post.PostDTO(" +
				" p.id, p.description, p.mediaUrl, p.createdAt, p.user.id, " +
				" p.user.username, p.user.imageUrl, " +
				" count(distinct l.user.id), count(distinct c.id), " +
				" CASE WHEN MAX(CASE WHEN l.user.id = :userId THEN 1 ELSE 0 END) = 1 THEN true ELSE false END)" +
			" FROM Post p " +
			" LEFT JOIN p.likes l " +
			" LEFT JOIN p.comments c " +
			" WHERE p.id = :postId" +
			" GROUP BY p.id, p.description, p.mediaUrl, p.createdAt, p.user.id, p.user.username, p.user.imageUrl")
	PostDTO getPostById(@Param("postId") Long postId, @Param("userId") Long userId);

	@Query("SELECT DISTINCT new sunuser.mylife.post.PostDTO(" +
				" p.id, p.description, p.mediaUrl, p.createdAt, p.user.id, " +
				" p.user.username, p.user.imageUrl, " +
				" count(distinct l.user.id), count(distinct c.id), " +
				" CASE WHEN MAX(CASE WHEN l.user.id = :userId THEN 1 ELSE 0 END) = 1 THEN true ELSE false END)" +
			" FROM Post p " +
			" LEFT JOIN p.likes l " +
			" LEFT JOIN p.comments c " +
			" WHERE p.user.username = :username" +
			" GROUP BY p.id, p.description, p.mediaUrl, p.createdAt, p.user.id, p.user.username, p.user.imageUrl")
	List<PostDTO> getAllPostByUsername(@Param("username") String username, @Param("userId") Long userId);
}

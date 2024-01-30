package sunuser.mylife.post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sunuser.mylife.user.User;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    Long countByPost(Post post);
    List<PostComment> findAllByPost(Post post);
}

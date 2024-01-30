package sunuser.mylife.post;

import java.util.Date;

public record CommentDTO(Long id, String text, Date createdAt, Long userId, String username, String userImageUrl) {
}

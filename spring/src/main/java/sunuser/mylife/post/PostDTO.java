package sunuser.mylife.post;

import java.util.Date;

public record PostDTO(Long id,
                      String description,
                      String mediaUrl,
                      Date createdAt,
                      Long userId,
                      String username,
                      String userImageUrl,
                      Long likesAmount,
                      Long commentsAmount,
                      Boolean isLiked) {
}
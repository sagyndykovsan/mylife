package sunuser.mylife.post;

import org.springframework.stereotype.Service;
import sunuser.mylife.user.UserDTO;

import java.util.Date;
import java.util.function.Function;

@Service
public class PostCommentDTOMapper implements Function<PostComment, CommentDTO> {
    @Override
    public CommentDTO apply(PostComment comment) {
        return new CommentDTO(comment.getId(),
                comment.getText(),
                comment.getCreatedAt(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getUser().getImageUrl());
    }
}

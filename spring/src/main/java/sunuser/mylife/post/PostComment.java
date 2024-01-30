package sunuser.mylife.post;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sunuser.mylife.user.User;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Post post;

    private String text;
    private Date createdAt = new Date();

    public PostComment(User user, Post post, String text) {
        this.user = user;
        this.post = post;
        this.text = text;
    }
}

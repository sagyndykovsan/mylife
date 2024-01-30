package sunuser.mylife.post;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import sunuser.mylife.user.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String mediaUrl;
	private String description;
	private Date createdAt = new Date();
	@ManyToOne
	private User user;

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
	private List<PostLike> likes = new ArrayList<>();

	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
	private List<PostComment> comments = new ArrayList<>();

	public void setUser(User user) {
		this.user = user;
	}

	public Post(String mediaUrl, String description, User user) {
		this.mediaUrl = mediaUrl;
		this.description = description;
		this.user = user;
	}
}

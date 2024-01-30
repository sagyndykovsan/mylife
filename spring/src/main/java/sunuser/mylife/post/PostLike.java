package sunuser.mylife.post;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import sunuser.mylife.user.User;

@Entity
@Data
@NoArgsConstructor
@IdClass(PostLikeId.class)
public class PostLike {

	@Id
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "user_id")
	private User user;


	@Id
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "post_id")
	private Post post;


	public PostLike(User user, Post post) {
		this.user = user;
		this.post = post;
	}
}

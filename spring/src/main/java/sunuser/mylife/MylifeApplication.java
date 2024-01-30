package sunuser.mylife;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import sunuser.mylife.post.Post;
import sunuser.mylife.post.PostComment;
import sunuser.mylife.post.PostCommentRepository;
import sunuser.mylife.post.PostRepository;
import sunuser.mylife.user.User;
import sunuser.mylife.user.UserRepository;

@SpringBootApplication
public class MylifeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MylifeApplication.class, args);
	}


	@Bean
	public CommandLineRunner dataLoader(UserRepository repo, PasswordEncoder encoder, PostRepository Prepo, PostCommentRepository crepo) {
		return args -> {
			User user = new User("cdawg", encoder.encode("123"));
			repo.save(user);
			Post post = new Post("https://as2.ftcdn.net/v2/jpg/05/79/31/57/1000_F_579315701_sXazttRoHiBEe6L4Z9sqCnxb8R0xlX2v.jpg", "some mock data of post", user);
			Prepo.save(post);
			PostComment comment = new PostComment(user, post, "as always great");
			PostComment comment2 = new PostComment(user, post, "great day");
			crepo.save(comment);
			crepo.save(comment2);
		};
	}
}

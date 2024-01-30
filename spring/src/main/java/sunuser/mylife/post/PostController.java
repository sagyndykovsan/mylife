package sunuser.mylife.post;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import sunuser.mylife.user.User;

@RestController
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;

	// current
	@GetMapping("/posts")
	public Page<PostDTO> getAllPosts(@AuthenticationPrincipal User user,
			@RequestParam(defaultValue = "0") Integer page) {
		return postService.getAllPosts(user, page);
	}

	@GetMapping("/{username}/posts")
	public List<PostDTO> getUsersPost(@PathVariable String username) {
		return postService.getUsersPost(username);
	}

	@GetMapping("/posts/{postId}")
	public PostDTO getPostById(@PathVariable Long postId, @AuthenticationPrincipal User user) {
		return postService.getPostById(postId, user);
	}

	@PostMapping("/posts/{postId}/like")
	public void likePost(@PathVariable Long postId, @AuthenticationPrincipal User user) {
		postService.likeOrUnlike(postId, user);
	}

	@PostMapping("/posts/create")
	public void createPost(@RequestParam String description,
						   @RequestParam MultipartFile media,
						   HttpServletRequest request,
						   @AuthenticationPrincipal User user) {
		postService.createPost(description, media, request, user);

	}

	@GetMapping("/posts/media/{filename}")
	public ResponseEntity<Resource> getPostMedia(@PathVariable String filename, HttpServletRequest request) {
		return postService.getPostMedia(filename, request);
	}

	@GetMapping("/posts/{postId}/comments")
	public List<CommentDTO> getAllComments(@PathVariable Long postId) {
		return postService.getAllComments(postId);
	}


	@PostMapping("/posts/{postId}/comments/create")
	public ResponseEntity<String> createComment(@RequestBody PostComment comment, @AuthenticationPrincipal User user) {
		return postService.createComment(comment, user);
	}
}

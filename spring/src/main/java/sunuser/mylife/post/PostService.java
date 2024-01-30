package sunuser.mylife.post;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import sunuser.mylife.service.FileStorageService;
import sunuser.mylife.service.JwtService;
import sunuser.mylife.service.UrlUtil;
import sunuser.mylife.user.ProfileService;
import sunuser.mylife.user.User;
import sunuser.mylife.user.UserDTOMapper;
import sunuser.mylife.user.UserRepository;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepo;
	private final JwtService jwtService;
	private final PostCommentDTOMapper commentDTOMapper;
	private final PostLikeRepository postLikeRepo;
	private final PostCommentRepository postCommentRepo;
	private final FileStorageService storage;
	private final UserDTOMapper userDTOMapper;
	private final UrlUtil urlUtil;
	private final ProfileService profileService;
	private final UserRepository userRepo;

	@Transactional
	public void likeOrUnlike(Long postId, User user) {
		Post post = postRepo.findById(postId).get();

		if (postLikeRepo.existsByUserAndPost(user, post)) {
			postLikeRepo.deleteByUserAndPost(user, post);
		} else {
			PostLike postLike = new PostLike(user, post);
			postLikeRepo.save(postLike);
		}
	}

	// current
	public Page<PostDTO> getAllPosts(User user, int page) {
		Pageable pageable = PageRequest.of(page, 10);
		return postRepo.getAllPosts(user.getId(), pageable);
	}

	public List<PostDTO> getUsersPost(String username) {
		User user = userRepo.findByUsername(username);

		return postRepo.getAllPostByUsername(username, user.getId());
	}

	public PostDTO getPostById(Long postId, User user) {
		return postRepo.getPostById(postId, user.getId());
	}

	public List<CommentDTO> getAllComments(Long postId) {
		Post post = postRepo.findById(postId).orElseThrow(() -> new RuntimeException("post not found"));
		List<PostComment> comments = postCommentRepo.findAllByPost(post);

		return comments.stream()
				.map(commentDTOMapper)
				.collect(Collectors.toList());
	}

    public ResponseEntity<String> createComment(PostComment comment, User user) {
		comment.setUser(user);
		postCommentRepo.save(comment);

		return ResponseEntity.ok("all good");
    }

	public void createPost(String description, MultipartFile media, HttpServletRequest request, User user) {
		String filename = storage.store(media);
		String baseUrl = urlUtil.getBaseUrl(request);

		Post post = new Post(baseUrl+"/posts/media/"+filename, description, user);
		postRepo.save(post);
	}

	public ResponseEntity<Resource> getPostMedia(String filename, HttpServletRequest request) {

		Resource resource = storage.loadAsResource(filename);

		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			throw new RuntimeException();
		}

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.body(resource);

	}
}

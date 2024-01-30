package sunuser.mylife.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PostByIdConverter implements Converter<Long, Post> {
    @Autowired
    private PostRepository repo;

    public Post convert(Long id) {
        return repo.findById(id).orElse(null);
    }
}

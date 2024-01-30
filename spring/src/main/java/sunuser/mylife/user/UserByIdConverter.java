package sunuser.mylife.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class UserByIdConverter implements Converter<Long, User> {
    @Autowired
    private UserRepository repo;

    public User convert(@NonNull Long id) {
        return repo.findById(id).orElse(null);
    }
}

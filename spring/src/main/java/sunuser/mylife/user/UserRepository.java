package sunuser.mylife.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT new sunuser.mylife.user.UserDTO(u.id, u.username, u.imageUrl, count(distinct p), count(distinct fg), count(distinct fs)) FROM User u" +
            " LEFT JOIN u.following fg" +
            " LEFT JOIN u.followers fs" +
            " LEFT JOIN u.posts p" +
            " WHERE u.username = :username" +
            " GROUP BY u.id, u.username, u.imageUrl")
    UserDTO getUserByUsername(@Param("username") String username);

    @Query("SELECT new sunuser.mylife.user.UserDTO(u.id, u.username, u.imageUrl, count(distinct p), count(distinct fg), count(distinct fs))" +
            " FROM User u" +
            " LEFT JOIN u.following fg" +
            " LEFT JOIN u.followers fs" +
            " LEFT JOIN u.posts p" +
            " WHERE fs.username = :username" +
            " GROUP BY u.id, u.username, u.imageUrl")
    List<UserDTO> getUserFollowing(@Param("username") String username);


    @Query("SELECT new sunuser.mylife.user.UserDTO(u.id, u.username, u.imageUrl, count(distinct p), count(distinct fg), count(distinct fs))" +
            " FROM User u" +
            " LEFT JOIN u.following fg" +
            " LEFT JOIN u.followers fs" +
            " LEFT JOIN u.posts p" +
            " WHERE fg.username = :username" +
            " GROUP BY u.id, u.username, u.imageUrl")
    List<UserDTO> getUserFollowers(@Param("username") String username);

    @Query("SELECT new sunuser.mylife.user.UserDTO(u.id, u.username, u.imageUrl, count(distinct p), count(distinct fg), count(distinct fs))" +
            " FROM User u" +
            " LEFT JOIN u.following fg" +
            " LEFT JOIN u.followers fs" +
            " LEFT JOIN u.posts p" +
            " WHERE u.username LIKE CONCAT(:username, '%')" +
            " GROUP BY u.id, u.username, u.imageUrl")
    List<UserDTO> findByUsernameLike(@Param("username") String username);
}


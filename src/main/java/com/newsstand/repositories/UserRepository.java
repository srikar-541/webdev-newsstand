package com.newsstand.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.Param;

import com.newsstand.models.User;

import java.util.Set;

public interface UserRepository extends CrudRepository<User, Integer> {
	
	@Query("SELECT user FROM User user WHERE "
			+ "user.username=:username and user.password =:password") User 	
	authenticateUser(@Param("username") String username, @Param("password") String password);

	@Query(nativeQuery = true, value = "SELECT * FROM user WHERE id=(SELECT user_id FROM likes WHERE article_id=:id)")
	Set<User> getLikedUsers(@Param("id") int id);
	
}

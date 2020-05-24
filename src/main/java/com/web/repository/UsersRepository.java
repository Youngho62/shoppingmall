package com.web.repository;

import com.web.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<User, Long> {
    public User findUserByUserId(String userId);

    @Query("select count(*) from User u where u.userId=?1")
    public int findCountByUserId(String userId);
}

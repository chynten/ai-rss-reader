package ai.feed.reader.repository;

import org.springframework.data.repository.CrudRepository;

import ai.feed.reader.dto.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User getUserByUsername(String username);

}

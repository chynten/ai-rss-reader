package ai.feed.reader.repository;

import org.springframework.data.repository.CrudRepository;

import ai.feed.reader.dto.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String name);
} 
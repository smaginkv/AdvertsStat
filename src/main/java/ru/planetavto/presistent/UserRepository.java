package ru.planetavto.presistent;

import org.springframework.data.repository.CrudRepository;

import ru.planetavto.security.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);
}

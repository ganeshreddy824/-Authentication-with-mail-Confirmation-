package com.email.verify.userrepo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.email.verify.model.User;

public interface UserRepo extends JpaRepository<User, Long>{

	Optional<User> findByEmail(String email);

	void deleteByEmail(String email);

}

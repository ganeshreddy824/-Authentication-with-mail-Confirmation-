package com.email.verify.model;

import java.util.List;
import java.util.Optional;

import com.email.verify.registration.RegistrationRequest;

public interface IUserService {
	
	List<User> getUsers();
	
	
	User registerUser(RegistrationRequest request);
	
	
	//User findByEmail(String email);
	Optional<User> findByEmail(String email);
	
	
	void saveUserVerificationToken(User theUser, String verificationToken);
	
	String validateToken(String theToken);
	
	void deleteUser(String emial);

}

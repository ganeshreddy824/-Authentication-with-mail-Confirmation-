package com.email.verify.model;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.email.verify.exception.UserAlreadyExistsException;
import com.email.verify.registration.RegistrationRequest;
import com.email.verify.registration.token.VerificationToken;
import com.email.verify.registration.token.VerificationTokenRepository;
import com.email.verify.userrepo.UserRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
	
	private final UserRepo repo;
	
	private final PasswordEncoder passwordEncoder;
	
	private final VerificationTokenRepository tokenRepository;

	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return repo.findAll();
	}

	@Override
	public User registerUser(RegistrationRequest request) {
		// TODO Auto-generated method stub
		Optional<User> user=this.findByEmail(request.email());
		if(user.isPresent()) {
			throw new UserAlreadyExistsException(
					"User with email "+request.email() +" already exists");
		}
		var newUser =new User();
		newUser.setFirstName(request.firstName());
		newUser.setLastName(request.lastName());
		newUser.setEmail(request.email());
		newUser.setPassword(passwordEncoder.encode(request.password()));
		newUser.setRole(request.role());
		
		return repo.save(newUser);
	}

	@Override
	public Optional<User> findByEmail(String email){
	//public User findByEmail(String email) {
		// TODO Auto-generated method stub
		return repo.findByEmail(email);
	}

	@Override
	public void saveUserVerificationToken(User theUser, String token) {
		// TODO Auto-generated method stub
		var verifiactionToken=new VerificationToken(token, theUser);
		tokenRepository.save(verifiactionToken);
		
	}

	@Override
	public String validateToken(String theToken) {
		// TODO Auto-generated method stub
		VerificationToken token=tokenRepository.findByToken(theToken);
		if(token == null){
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            tokenRepository.delete(token);
            return "Token already expired";
        }
        user.setEnabled(true);
        repo.save(user);
        return "valid";
    }
	
	@Transactional
	@Override
	public void deleteUser(String email) {
		repo.deleteByEmail(email);
		// TODO Auto-generated method stub
		
	}
}

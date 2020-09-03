package com.thechinterview.ldaprestapi;

import com.thechinterview.ldaprestapi.model.User;
import com.thechinterview.ldaprestapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.ldap.query.LdapQueryBuilder.*;

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
class UserRepositoryTest {

	@Autowired
	LdapTemplate ldapTemplate;

	@Autowired
	UserRepository userRepo;

	static User [] users = {
			new User(null, "john", "John", "Doe"),
			new User(null, "jane", "Jane", "Doe"),
			new User(null, "fulano", "Fulano", "Sicrano"),
			new User(null, "mary", "Mary", "Poppins")
	};



	@Value("${application.isTestProfile}")
	boolean isTestProfile;

	static int initialSize;
	static UserRepository staticInstance;

	@Test
	void contextLoads() {
		staticInstance = userRepo;
		assertEquals(isTestProfile, true);
	}

	@Test
	void addUsers() {

		for (User u: users) {
			userRepo.addUser(u);
			User repoU = (User) ldapTemplate.findOne(
					query().base(userRepo.BASE_DN).where("uid").is(u.getUid()),
					User.class
				);

			assertEquals(u.compareTo(repoU), 0,
					"Testing user at memory against user at repo, u.uid: " + u.getUid());
		}
	}

	@Test
	void addExistingUser() {
		assertThrows(ResponseStatusException.class, () -> {
			try {
				User uniqueNewUser = new User(null, new Date().toString(), "F", "S");
				userRepo.addUser(uniqueNewUser);
				userRepo.addUser(uniqueNewUser);
			}
			catch (ResponseStatusException e) {
				assertEquals(
						e.getStatus(),
						HttpStatus.CONFLICT
						, "HttpStatus should be 409 CONFLICT"
				);
				throw e;
			}
		});
	}

	@Test
	void findAll() {
		List l = userRepo.getUsers();
		assertEquals(l.size(), 2 + users.length,
				"Testing size from findAll");
	}

	@Test
	void findOne() {
		for (User u: users) {
			User repoU = userRepo.getUser(u.getUid());
			assertEquals(u.compareTo(repoU), 0,
					"Testing user at memory against user at repo, u.uid: " + u.getUid());
		}
	}

	@Test
	void findNonExistingUser() {
		assertThrows(ResponseStatusException.class, () -> {
			try {
				userRepo.delete("nonExistingUid");
			}
			catch (ResponseStatusException e) {
				assertEquals(
						e.getStatus(),
						HttpStatus.NOT_FOUND
						, "HttpStatus should be NOT_FOUND"
				);
				throw e;
			}
		});
	}

	@Test
	void delete() {
		User u = new User(
				null,
				"userToBeDeleted",
				"Born",
				"To Die"
					);
		userRepo.addUser(u);
		userRepo.delete(u.getUid());
		int size = ldapTemplate.find(
				query().base(UserRepository.BASE_DN).where("uid").is(u.getUid()), User.class)
				.size();
		assertEquals(0, size);
	}

	@Test
	void deleteNonExistingUser() {
		assertThrows(ResponseStatusException.class, () -> {
			try {
				userRepo.delete("nonExistingUid");
			}
			catch (ResponseStatusException e) {
				assertEquals(
						e.getStatus(),
						HttpStatus.NOT_FOUND
						, "HttpStatus should be NOT_FOUND"
							);
				throw e;
			}
		});
	}
}

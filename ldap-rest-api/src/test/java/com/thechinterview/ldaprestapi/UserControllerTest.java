package com.thechinterview.ldaprestapi;

import com.thechinterview.ldaprestapi.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
@ActiveProfiles("test")
class UserControllerTest {

	@Autowired
	private LdapTemplate ldapTemplate;

	@Autowired
	private MockMvc mockMvc;

	final String BASE_URL = "http://localhost:8080/Users";

	@Test
	void addUser() throws Exception {
		mockMvc.perform(post(BASE_URL)
			.contentType(MediaType.APPLICATION_JSON)
				.content(new User(null, "new", "New", "User").toString()))
				.andExpect(status().isOk());
	}

	@Test
	void addAlreadyExistingUser() {
		try {
			User existingUser = new User(null, new Date().toString(), "First", "User");
			mockMvc.perform(post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(existingUser.toString()))
					.andExpect(status().isOk());
			mockMvc.perform(post(BASE_URL)
					.contentType(MediaType.APPLICATION_JSON)
					.content(existingUser.toString()))
					.andExpect(status().is(409));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void findAll() throws Exception {
		mockMvc.perform(get(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
						.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
	}

	@Test
	void findOne() throws Exception {
		mockMvc.perform(get(BASE_URL + "/firstuser")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasEntry("uid", "firstuser")));
	}

	@Test
	void findNonExistingUser() throws Exception {
		mockMvc.perform(get(BASE_URL + "/nonExistingUser")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(404));
	}

	@Test
	void deleteEntry() throws Exception {
		User userToBeDeleted = new User(null, "usertobedeleted", "Born", "To Die");
		// Add user
		mockMvc.perform(post(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
					.content(userToBeDeleted.toString()))
						.andExpect(status().isOk());
		// Delete user
		mockMvc.perform(delete(BASE_URL + "/" + userToBeDeleted.getUid()))
				.andExpect(status().isOk());
	}

	@Test
	void deleteNonExistingUser() throws Exception {
		mockMvc.perform(delete(BASE_URL + "/nonExistingUser")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is(404));
	}
}

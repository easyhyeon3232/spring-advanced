package org.example.expert.data;

import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.test.util.ReflectionTestUtils;

public class UserFixture {

	public static final String DEFAULT_EMAIL = "test@email.com";

	public static final String DEFAULT_PASSWORD = "1234";
	public static final String DEFAULT_ROLE = "USER";
	public static final UserRole DEFAULT_Enum_ROLE = UserRole.USER;


	public static User createUser(String email, String password, UserRole role) {
		User user = new User(email, password, role);
		ReflectionTestUtils.setField(user, "id", 1L);
		return user;
	}

	public static SignupRequest signupRequest() {
		return new SignupRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD, DEFAULT_ROLE);
	}

	public static SigninRequest signinRequest() {
		return new SigninRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
	}




}

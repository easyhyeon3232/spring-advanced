package org.example.expert.domain.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtUtil jwtUtil;

	@InjectMocks
	private AuthService authService;

	@Test
	public void 회원가입_성공() {
		// given
		SignupRequest request = new SignupRequest("test@email.com", "password123", "USER");
		String encodedPassword = "encodedPassword123";
		String token = "token";

		User mockUser = new User(request.getEmail(), encodedPassword, UserRole.USER);
		ReflectionTestUtils.setField(mockUser, "id", 1L);

		given(userRepository.existsByEmail(request.getEmail())).willReturn(false);
		given(passwordEncoder.encode(request.getPassword())).willReturn(encodedPassword);
		given(userRepository.save(any(User.class))).willReturn(mockUser);
		given(jwtUtil.createToken(1L, "test@email.com", UserRole.USER)).willReturn(token);

		// when
		SignupResponse response = authService.signup(request);

		//then
		assertNotNull(request);
		assertEquals(token, response.getBearerToken());
	}

	@Test
	void 회원가입_실패_이미_존재하는_이메일() {
		// given
		SignupRequest request = new SignupRequest("aaa@email.com", "1234", "USER");

		given(userRepository.existsByEmail(request.getEmail())).willReturn(true);

		// when
		AuthException authException = assertThrows(AuthException.class, () -> authService.signup(request));

		// then
		assertEquals("이미 존재하는 이메일입니다." , authException.getMessage());
	}

}
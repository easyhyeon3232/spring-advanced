package org.example.expert.domain.auth.service;

import static org.example.expert.data.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.example.expert.config.JwtUtil;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.auth.dto.request.SigninRequest;
import org.example.expert.domain.auth.dto.request.SignupRequest;
import org.example.expert.domain.auth.dto.response.SigninResponse;
import org.example.expert.domain.auth.dto.response.SignupResponse;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
		SignupRequest request = signupRequest();
		String encodedPassword = "encodedPassword123";
		String token = "token";

		// 이메일이 "?"인 가짜 유저 생성
		User mockUser = createUser(request.getEmail(), encodedPassword, DEFAULT_Enum_ROLE);


		given(userRepository.existsByEmail(request.getEmail())).willReturn(false);
		given(passwordEncoder.encode(request.getPassword())).willReturn(encodedPassword);
		given(userRepository.save(any(User.class))).willReturn(mockUser);
		given(jwtUtil.createToken(1L, DEFAULT_EMAIL, DEFAULT_Enum_ROLE)).willReturn(token);

		// when
		SignupResponse response = authService.signup(request);

		//then
		assertNotNull(request);
		assertEquals(token, response.getBearerToken());
	}

	@Test
	void 회원가입_실패_이미_존재하는_이메일() {
		// given
		SignupRequest request = signupRequest();

		given(userRepository.existsByEmail(request.getEmail())).willReturn(true);

		// when
		AuthException authException = assertThrows(AuthException.class, () -> authService.signup(request));

		// then
		assertEquals("이미 존재하는 이메일입니다." , authException.getMessage());
	}

	@Test
	void 로그인_성공() {
		// given
		SigninRequest request = signinRequest();
		String encodedPassword = "encodedPW";
		String token = "token";

		User mockUser = createUser(request.getEmail(), encodedPassword, DEFAULT_Enum_ROLE);

		given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(mockUser));
		given(passwordEncoder.matches(request.getPassword(), mockUser.getPassword())).willReturn(true);
		given(jwtUtil.createToken(1L, DEFAULT_EMAIL, DEFAULT_Enum_ROLE)).willReturn(token);

		// when
		SigninResponse response = authService.signin(request);

		// then
		assertNotNull(response);
		assertEquals(token, response.getBearerToken());
	}

	@Test
	void 로그인_실패_가입되지_않은_유저() {

		// given
		SigninRequest request = signinRequest();

		given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());

		// When
		AuthException authException = assertThrows(AuthException.class, () -> authService.signin(request));

		// Then
		assertEquals("가입되지 않은 유저입니다.", authException.getMessage());

	}

	@Test
	void 로그인_실패_비번이_일치하지_않음() {
		// given
		SigninRequest request = signinRequest();
		String encodedPW = "encodedPW";

		User mockUser = createUser(request.getEmail(), request.getPassword(), DEFAULT_Enum_ROLE);

		given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(mockUser));
		given(passwordEncoder.matches(request.getPassword(), mockUser.getPassword())).willReturn(false);

		// When
		AuthException authException = assertThrows(AuthException.class, () ->  {
			authService.signin(request);
		});

		// Then
		assertEquals("잘못된 이메일과 비밀번호입니다.", authException.getMessage());
	}
}
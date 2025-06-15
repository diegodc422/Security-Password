package br.com.security_password.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.security_password.model.UsersModel;
import br.com.security_password.repository.SecurityPasswordRepository;
import io.micrometer.common.util.StringUtils;

@Service
public class PasswordService {

	private final SecurityPasswordRepository securityPasswordRepository;
	private final PasswordEncoder encoder;

	public PasswordService(SecurityPasswordRepository securityPasswordRepository, PasswordEncoder encoder) {
		this.securityPasswordRepository = securityPasswordRepository;
		this.encoder = encoder;
	}

	public List<UsersModel> listAllUsers() {
		return securityPasswordRepository.findAll();
	}

	public ResponseEntity<?> validateAndSaveUsers(UsersModel user) {
		ResponseEntity<?> validationError = validateUser(user);
		if (validationError != null)
			return validationError;

		String originalPassword = user.getPassword();
		
		UsersModel saveUser = saveUserWithEncryptedPassword(user);

		return buildResponseSuccessfully(saveUser, originalPassword);
	}

	private ResponseEntity<?> validateUser(UsersModel user) {
		if (isDuplicateLogin(user.getLogin())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Este login já está cadastrado");
		}

		List<String> errorPassword = validatePassword(user.getPassword());
		return errorPassword.isEmpty() ? null : ResponseEntity.badRequest().body(errorPassword);
	}

	private boolean isDuplicateLogin(String login) {
		return securityPasswordRepository.existsByLogin(login);
	}

	private UsersModel saveUserWithEncryptedPassword(UsersModel user) {
		user.setPassword(encoder.encode(user.getPassword()));
		return securityPasswordRepository.save(user);
	}

	private ResponseEntity<Map<String, Object>> buildResponseSuccessfully(UsersModel saveUser,
			String originalPassword) {
		return ResponseEntity.ok(Map.of("mensagem", "Usuário cadastrado com sucesso", "login", saveUser.getLogin(),
				"senha_original", originalPassword, 
				"senha_criptografada", saveUser.getPassword() 
		));
	}

	private static void validatePasswordLength(String pass, List<String> failures) {
		if (StringUtils.isBlank(pass) || pass.length() < 8)
			failures.add("A senha deve conter pelo menos 8 caracteres");
	}

	private static void validateUpperCase(String pass, List<String> failures) {
		long upperCaseCount = pass.chars().filter(Character::isUpperCase).count();
		if (upperCaseCount < 3) {
			failures.add("A senha deve conter pelo menos três letras maiúsculas");
		}
	}

	private static void validateLowerCase(String pass, List<String> failures) {
		long lowerCaseCount = pass.chars().filter(Character::isLowerCase).count();
		if (lowerCaseCount < 2) {
			failures.add("A senha deve ter pelo menos duas letras minúsculas");
		}
	}

	private static void validateNumber(String pass, List<String> failures) {
		if (!Pattern.matches(".*[0-9].*", pass)) {
			failures.add("A senha deve possuir pelo menos um número");
		}
	}

	private static void validateSpecialCharacters(String pass, List<String> failures) {
		if (!Pattern.matches(".*[\\W].*", pass)) {
			failures.add("A senha deve possuir pelo menos um caractere especial");
		}
	}

	public List<String> validatePassword(String pass) {
		List<String> failures = new ArrayList<>();

		validatePasswordLength(pass, failures);
		validateUpperCase(pass, failures);
		validateLowerCase(pass, failures);
		validateNumber(pass, failures);
		validateSpecialCharacters(pass, failures);

		return failures;
	}

	public String encryptedPassword(String rawPassword) {
		return encoder.encode(rawPassword);
	}
}
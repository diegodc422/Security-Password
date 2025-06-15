package br.com.security_password.controler;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.security_password.model.UsersModel;
import br.com.security_password.service.PasswordService;

@RestController
@RequestMapping("/senha-segura")
public class UserController {

	private final PasswordService passwordService;
	
	public UserController(PasswordService passwordService) {
		this.passwordService = passwordService;
	}
	
	@PostMapping("/registrar")
	public ResponseEntity<?> cadastrarUsuario(@RequestBody UsersModel user){
		
		return passwordService.validateAndSaveUsers(user);
	}	
	
	@GetMapping("/usuarios")
	public ResponseEntity<List<UsersModel>> listarTodosUsuarios () {
		return ResponseEntity.ok(passwordService.listAllUsers());
	}
}

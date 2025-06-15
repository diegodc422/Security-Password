package br.com.security_password.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.security_password.model.UsersModel;

public interface SecurityPasswordRepository extends JpaRepository<UsersModel, Integer>{
	
	Optional<UsersModel> findByLogin (String login);
	public boolean existsByLogin(String login);
}

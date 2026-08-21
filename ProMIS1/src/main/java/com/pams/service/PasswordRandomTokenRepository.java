package com.pams.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pams.entity.PasswordRandomToken;



@Repository
public interface PasswordRandomTokenRepository extends JpaRepository<PasswordRandomToken,Long> {

	List<PasswordRandomToken> findAllByPasswordKeyAndRandomToken(String password, String base64EncodedKey);

	

}

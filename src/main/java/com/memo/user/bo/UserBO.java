package com.memo.user.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memo.common.EncryptUtils;
import com.memo.user.entity.UserEntity;
import com.memo.user.repository.UserRepository;

@Service
public class UserBO {
	@Autowired
	private UserRepository userRepository;
	
	public UserEntity getUserEntityByLoginId(String loginId) {
		return userRepository.findByLoginId(loginId).orElse(null);
	}
	
	
	public UserEntity addUser(String loginId, String password, String name, String email  ) { 
		return userRepository.save(
				UserEntity.builder()
				.loginId(loginId)
				.password(password)
				.name(name)
				.email(email)
				.build());
	}
	
	public UserEntity getUserEntityByLoginIdAndPassword(
			String loginId, String password) {
		String hashedPassword = EncryptUtils.md5(password);
		return userRepository.findByLoginIdAndPassword(loginId, hashedPassword).orElse(null);
	}
}

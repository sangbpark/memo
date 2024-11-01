package com.memo.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;
import com.memo.user.entity.UserEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
public class UserRestController {
	@Autowired
	private UserBO userBO;

	/**
	 * 아이디 중복확인
	 * @param loginId
	 * @return
	 */
	@GetMapping("/is-duplicate-id")
	public Map<String, Object> isDuplicatedId(
			@RequestParam("loginId") String loginId) {
		UserEntity user = userBO.getUserEntityByLoginId(loginId);
		boolean isDuplicateId = false;
		if (user != null) {
			isDuplicateId = true;
		}
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		result.put("is_duplicate_id", isDuplicateId);
		return result;
	}
	
	
	/**
	 * 회원가입 API
	 * @param loginId
	 * @param password
	 * @param name
	 * @param email
	 * @return
	 */
	@PostMapping("/sign-up")
	public Map<String, Object> signUp(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			@RequestParam("name") String name,
			@RequestParam("email") String email) {
		
		// md5 알고리즘 - hashing(복호화는 못하고 만들기만 함) 늘 똑같음
		// aaaa -> 74b8733745420d4d33f80c4663dc5e5
		// aaaa -> qqqqqqqqqqqq
		String hashedPassword = EncryptUtils.md5(password);
		
		// db insert
		UserEntity user = userBO.addUser(loginId, hashedPassword, name, email);
		
		// 응답값
		Map<String, Object> result = new HashMap<>();
		if (user != null) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("error_message", "회원가입에 실패했습니다.");
		}
		return result;
	}
	
	@PostMapping("/sign-in")
	public Map<String, Object> signIn(
			@RequestParam("loginId") String loginId,
			@RequestParam("password") String password,
			HttpServletRequest request) {
		// db select
		UserEntity user = userBO.getUserEntityByLoginIdAndPassword(loginId, password);
		Map<String, Object> result = new HashMap<>();
		if (user != null) {
			// 세션에 사용자 정보를 담는다.(사용자 각각을)
			HttpSession session =  request.getSession();
			session.setAttribute("userId", user.getId());
			session.setAttribute("userLoginId", user.getLoginId());
			session.setAttribute("userName", user.getName());
			result.put("code", 200);
			result.put("result", "성공");
			
		} else {
			result.put("code", 300); // 권한이 없으면 300
			result.put("error_message", "존재하지 않는 사용자 입니다.");
		}
		return result;
	}
}

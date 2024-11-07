package com.memo.post;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.bo.PostBO;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/post")
public class PostRestController {
	@Autowired
	private PostBO postBO;

	@PostMapping("/create")
	public Map<String, Object> create(
			@RequestParam("subject") String subject,
			@RequestParam("content") String content,
			@RequestParam(value= "file", required = false) MultipartFile file,
			HttpSession session
			) {
		
		
		
		boolean isSuccess = postBO.addPost((int)session.getAttribute("userId")
				, subject, (String)session.getAttribute("userLoginId"), content, file);
		Map<String, Object> result = new HashMap<>();
		if (isSuccess) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("error_message", "글을 저장하는데 실패했습니다. 관리자에게 문의해주세요.");
		}
		return result;
	}
}

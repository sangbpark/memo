package com.memo.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.memo.post.mapper.PostMapper;

@Controller
public class TestController {
	@Autowired
	private PostMapper postMapper;
	
	@ResponseBody
	@GetMapping("/test1")
	public String text1() {
		return "<h2>스프링 테스트</h2>";
	}
	
	@ResponseBody
	@GetMapping("/test2")
	public Map<String, Object> text2() {
		Map<String, Object> map = new HashMap<>();
		map.put("dfd", 123);
		return map;
	}
	
	@GetMapping("/test3")
	public String text3() {

		return "test/test";
	}
	
	@ResponseBody
	@GetMapping("/test4")
	public List<Map<String, Object>> test4() {
		return postMapper.selectPostList();
	}
	
}

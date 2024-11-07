package com.memo.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.memo.post.bo.PostBO;
import com.memo.post.domain.Post;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@Controller
public class PostController {
	@Autowired
	private PostBO postBO;

	@GetMapping("/post-list-view")
	public String postListView(Model model, HttpSession session) {
		Integer userId = (Integer)session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/user/sign-in-view";
		}
		
		List<Post> postList = postBO.getPostListByUserId(userId);
				
		model.addAttribute("postList", postList);
		return "post/postList";
	}
	
	/**
	 * 글쓰기 화면
	 * @return
	 */
	@GetMapping("/post-create-view")
	public String postCreteView() {
		return "post/postCreate";
	}
	
	@GetMapping("/post-detail-view")
	public String postDetailView(
			@RequestParam("postId") int postId,
			HttpSession session,
			Model model) {
		// db select - postId, userId
		int userId = (int)session.getAttribute("userId");
		Post post = postBO.getPostByPostIdAndUserId(postId, userId);
		
		// model 담기
		model.addAttribute("post", post);
		return "post/postDetail";
	}
}

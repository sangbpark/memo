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
	public String postListView(
			@RequestParam(value = "prevId", required = false) Integer prevIdParam,
			@RequestParam(value = "nextId", required = false) Integer nextIdParam,
			Model model, HttpSession session) {
		Integer userId = (Integer)session.getAttribute("userId");
		if (userId == null) {
			return "redirect:/user/sign-in-view";
		}
		
		List<Post> postList = postBO.getPostListByUserId(userId, prevIdParam, nextIdParam);
		int prevId = 0;
		int nextId = 0;
		
		if (postList.isEmpty() == false) {
			prevId = postList.get(0).getId();
			nextId = postList.get(postList.size() - 1).getId();
			
			// 이전이 없는가? => 그렇다면 0
			// 유저가 쓴 글들 중 제일 큰 숫자 하나가 prevId와 같을 때 없음
			if (postBO.isPrevLastPageByUserId(userId, prevId)) {
				prevId = 0;
			}
			
			// 다음이 없는가? => 그렇다면 0
			// 유저가 쓴 글들 중 제일 작은 숫자 하나가 nextId와 같을 때 없음
			if (postBO.isNextLastPageByUserId(userId, nextId)) {
				nextId = 0;
			}
		}
		model.addAttribute("postList", postList);
		model.addAttribute("prevId", prevId);
		model.addAttribute("nextId", nextId);
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
	
	
	/**
	 * 글 상세 화면
	 * @param postId
	 * @param session
	 * @param model
	 * @return
	 */
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

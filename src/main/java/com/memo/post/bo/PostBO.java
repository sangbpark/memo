package com.memo.post.bo;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.domain.Post;
import com.memo.post.mapper.PostMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostBO {
//	private Logger log = LoggerFactory.getLogger(PostBO.class);
//	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private FileManagerService fileManager;
	
	// 페이징 정보 필드(limit)
	private static final int POST_MAX_SIZE = 3;
	
	public List<Post> getPostListByUserId(int userId, Integer prevId, Integer nextId) {
		// 게시글 번호: 10 9 8 | 7 6 5 | 4 3 2 | 1
		// 만약 4 3 2 페이지
		// 1) 다음(nextId가 있음): 2보다 작은 3개 가져오기 desc
		// 2) 이전(prevId가 있음): 4보다 큰 3개 asc => 5 6 7 => list reverse
		// 3) 페이징 없음(next, prevId 모두 없음): 최신순 3개 desc
		
		// xml에서 하나의 쿼리로 만들기 위해 변수를 정제해본다.
		
		Integer standardId = null; // 기준 id(prev or next)
		String direction = null; // 방향
		
		if (prevId != null) {
			standardId = prevId;
			direction = "prev";
			// 예)  5 6 7
			List<Post> postList = postMapper.selectPostListByUserId(userId, standardId, direction, POST_MAX_SIZE);
			Collections.reverse(postList);
			return postList;
		} else if (nextId != null) {
			standardId = nextId;
			direction = "next";
		} 
		
		// 3) 페이징 없음 or 1) 다음
		return postMapper.selectPostListByUserId(userId, standardId, direction, POST_MAX_SIZE);
	}
	
	public boolean addPost(int userId, String subject, String userLoginId, String content, MultipartFile file) {
		String imagePath = null;
		if (file != null) {
			imagePath = fileManager.uploadFile(file, userLoginId);
		}
		if (postMapper.insertPost(userId, subject, content, imagePath) > 0) {
			return true;
		}
		return false;
	}
	
	public Post getPostByPostIdAndUserId(int postId, int userId) {
		return postMapper.selectPostByPostIdAndUserId(postId, userId);
	};
	
	public void updatePostByPostIdUserId(int postId, int userId, String subject, String content
			, MultipartFile file, String userLoginId) {
		Post post = postMapper.selectPostByPostIdAndUserId(postId, userId);
		if (post == null) {
			log.info("[####글 수정] post is null. postId:{}, userId:{}", postId, userId);
			return;
		}
		
		String imagePath = null;
		if (file != null) {
			imagePath = fileManager.uploadFile(file, userLoginId);
			if (imagePath != null && post.getImagePath() != null) {
				// 폴더, 이미지 제거
				fileManager.deleteFile(post.getImagePath());
			}
		}
		
		postMapper.updatePostByPostId(postId, subject, content, imagePath);
	}
	
	public void deletePostByPostIdUserId(int postId, int userId) {
		Post post = postMapper.selectPostByPostIdAndUserId(postId, userId);
		if (post == null) { 
			log.info("[####글 삭제] post is null. postId:{}, userId:{}", postId, userId);
			return;
		}
		int rowCount = postMapper.deletePostByPostId(postId);
		if (rowCount > 0 && post.getImagePath() != null){
			fileManager.deleteFile(post.getImagePath());
		}
	}
	
	public boolean isPrevLastPageByUserId(int userId, int prevId) {
		int maxPostId = postMapper.selectIdByUserIdAsSort(userId, "desc");
		return maxPostId == prevId;
	}
	
	public boolean isNextLastPageByUserId(int userId, int nextId) {
		int maxPostId = postMapper.selectIdByUserIdAsSort(userId, "asc");
		return maxPostId == nextId;
	}
	
}

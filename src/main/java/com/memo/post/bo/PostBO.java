package com.memo.post.bo;

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
	
	public List<Post> getPostListByUserId(int userId) {
		return postMapper.selectPostListByUserId(userId);
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
	
}

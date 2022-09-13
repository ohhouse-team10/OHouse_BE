package com.sparta.todayhouse.service;

import com.sparta.todayhouse.dto.ResponseMessage;
import com.sparta.todayhouse.dto.request.PostRequestDto;
import com.sparta.todayhouse.dto.response.PostResponseDto;
import com.sparta.todayhouse.entity.Member;
import com.sparta.todayhouse.entity.Post;
import com.sparta.todayhouse.repository.PostRepository;
import com.sparta.todayhouse.shared.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sparta.todayhouse.shared.ErrorCode.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final MemberService memberService;

    @Transactional(readOnly = true)
    public ResponseMessage<?> isPresentPost(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if(null == post) return ResponseMessage.fail(POST_NOT_FOUND);

        return ResponseMessage.success(post);
    }

    @Transactional
    public ResponseMessage<?> createPost(PostRequestDto requestDto, UserDetailsImpl userDetails) {
        UserDetailsImpl user = userDetails;

        Member member = userDetails.getMember();
        //memberService.isPresentMember(member.getEmail());

        Post post = postRepository.save(Post.builder()
                .content(requestDto.getContent())
                .thumbnail("")
                .member(member)
                .build());
        return ResponseMessage.success(post);
    }

    @Transactional(readOnly = true)
    public ResponseMessage<?> getAllPost(){
        List<Post> posts = postRepository.findAll();

        return ResponseMessage.success(posts);
    }

    @Transactional(readOnly = true)
    public ResponseMessage<?> getPostPerPage(Pageable pageable){
        Page<Post> postPage = postRepository.findAll(pageable);

        List<Post> postList = postPage.getContent();
        List<PostResponseDto> responseList = new ArrayList<>();

        for(Post post : postList){
            Member member = post.getMember();
            responseList.add(PostResponseDto.builder()
                    .post_id(post.getId())
                    .profile_image(member.getProfile_image())
                    .nickname(member.getNickname())
                    .isFollow(false)
                    .statusMessage(member.getStatus_message())
                    .thumbnail(post.getThumbnail())
                    .isLike(false)
                    .like_num(0)
                    .comment_num(0)
                    .content(post.getContent())
                    .build());
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("content", responseList);
        responseMap.put("last", postPage.isLast());
        responseMap.put("totalPages", postPage.getTotalPages());

        return ResponseMessage.success(responseMap);
    }

   // @Transactional(readOnly = true)
    public ResponseMessage<?> getPost(Long id) {
        return isPresentPost(id);
    }

    @Transactional
    public ResponseMessage<?> updatePost(Long id, PostRequestDto requestDto) {
        ResponseMessage<?> response = isPresentPost(id);
        Post post = (Post)response.getData();
        if (null == post) return response;

        post.update(requestDto);

        return ResponseMessage.success(post);
    }

    @Transactional
    public ResponseMessage<?> deletePost(Long id) {
        ResponseMessage<?> response = isPresentPost(id);
        Post post = (Post)response.getData();
        if (null == post) return response;

        postRepository.delete(post);

        return ResponseMessage.success("success");
    }
}

package com.sparta.todayhouse.service;

import com.sparta.todayhouse.dto.ResponseMessage;
import com.sparta.todayhouse.dto.request.PostRequestDto;
import com.sparta.todayhouse.dto.response.PostDetailResponseDto;
import com.sparta.todayhouse.dto.response.PostMainResponseDto;
import com.sparta.todayhouse.dto.response.PostResponseDto;
import com.sparta.todayhouse.entity.Likes;
import com.sparta.todayhouse.entity.Member;
import com.sparta.todayhouse.entity.Post;
import com.sparta.todayhouse.repository.PostRepository;
import com.sparta.todayhouse.shared.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sparta.todayhouse.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final AwsS3Service imageUploader;

    @Transactional(readOnly = true)
    public ResponseMessage<?> isPresentPost(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if(null == post) return ResponseMessage.fail(POST_NOT_FOUND);

        return ResponseMessage.success(post);
    }

    @Transactional
    public ResponseMessage<?> createPost(PostRequestDto requestDto, MultipartFile multipartFile,
                                         UserDetailsImpl userDetails) {
        if(null == multipartFile) return ResponseMessage.fail(FILE_NOT_FOUND);
        ResponseMessage<?> image_data = imageUploader.uploadFile(multipartFile);
        if(!image_data.getIsSuccess()) return image_data;

        Member member = userDetails.getMember();
        String imageUrl = (String) image_data.getData();

        Post post = Post.builder()
                .content(requestDto.getContent())
                .thumbnail(imageUrl)
                .views(0)
                .style(requestDto.getStyle())
                .type(requestDto.getType())
                .member(member)
                .build();
        postRepository.save(post);

        return ResponseMessage.success("post success");
    }

    @Transactional(readOnly = true)
    public ResponseMessage<?> getPostMain(){
        ResponseMessage<?> post_data = isPresentPost(1L);
        if(!post_data.getIsSuccess()) return post_data;

        Post post = (Post) post_data.getData();
        Member member = post.getMember();
        return ResponseMessage.success(PostMainResponseDto.builder()
                .post_id(post.getId())
                .profile_image(member.getProfile_image())
                .nickname(member.getNickname())
                .thumbnail(post.getThumbnail())
                .build());
    }

    @Transactional(readOnly = true)
    public ResponseMessage<?> getPostPerPage(Pageable pageable){
        Page<Post> postPage = postRepository.findAll(pageable);

        List<Post> postList = postPage.getContent();
        List<PostResponseDto> responseList = new ArrayList<>();

        for(Post post : postList){
            Member postMember = post.getMember();

            responseList.add(PostResponseDto.builder()
                    .post_id(post.getId())
                    .profile_image(postMember.getProfile_image())
                    .nickname(postMember.getNickname())
                    .isFollow(false)
                    .statusMessage(postMember.getStatus_message())
                    .thumbnail(post.getThumbnail())
                    .isLike(false)
                    .like_num(post.getLikes().size())
                    .comment_num(post.getComments().size())
                    .content(post.getContent())
                    .build());
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("content", responseList);
        responseMap.put("last", postPage.isLast());
        responseMap.put("totalPages", postPage.getTotalPages());

        return ResponseMessage.success(responseMap);
    }

    @Transactional(readOnly = true)
    public ResponseMessage<?> getPostPerPage(Pageable pageable, UserDetailsImpl userDetails){
        Member member = userDetails.getMember();
        Page<Post> postPage = postRepository.findAll(pageable);

        List<Post> postList = postPage.getContent();
        List<PostResponseDto> responseList = new ArrayList<>();

        for(Post post : postList){
            Member postMember = post.getMember();
            Boolean isLike = false;
            List<Likes> likes = post.getLikes();
            for (Likes like : likes) {
                if (member.equals(like.getMember())) {
                    isLike = true;
                    break;
                }
            }

            responseList.add(PostResponseDto.builder()
                    .post_id(post.getId())
                    .profile_image(postMember.getProfile_image())
                    .nickname(postMember.getNickname())
                    .isFollow(false)
                    .statusMessage(postMember.getStatus_message())
                    .thumbnail(post.getThumbnail())
                    .isLike(isLike)
                    .like_num(post.getLikes().size())
                    .comment_num(post.getComments().size())
                    .content(post.getContent())
                    .build());
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("content", responseList);
        responseMap.put("last", postPage.isLast());
        responseMap.put("totalPages", postPage.getTotalPages());

        return ResponseMessage.success(responseMap);
    }

    @Transactional(readOnly = true)
    public ResponseMessage<?> getPostDetail(Long id) {
        ResponseMessage<?> post_data = isPresentPost(id);
        if (!post_data.getIsSuccess()) return post_data;

        Post post = (Post) post_data.getData();
        Member member = post.getMember();
        post.plusViews();

        return ResponseMessage.success(PostDetailResponseDto.builder()
                .style(post.getStyle())
                .type(post.getType())
                .thumbnail(post.getThumbnail())
                .content(post.getContent())
                .profile_image(member.getProfile_image())
                .nickname(member.getNickname())
                .isFollow(false)
                .isLike(false)
                .like_num(post.getLikes().size())
                .isEditable(false)
                .build());
    }

    @Transactional(readOnly = true)
    public ResponseMessage<?> getPostDetail(Long id, UserDetailsImpl userDetails) {
        ResponseMessage<?> post_data = isPresentPost(id);
        if (!post_data.getIsSuccess()) return post_data;

        Post post = (Post) post_data.getData();
        Member post_member = post.getMember();
        Member member = userDetails.getMember();

        List<Likes> likesList = post.getLikes();
        Boolean isLike = false;
        for(Likes likes : likesList){
            if(member.equals(likes.getMember())){
                isLike = true;
                break;
            }
        }

        return ResponseMessage.success(PostDetailResponseDto.builder()
                .style(post.getStyle())
                .type(post.getType())
                .thumbnail(post.getThumbnail())
                .content(post.getContent())
                .profile_image(post_member.getProfile_image())
                .nickname(post_member.getNickname())
                .isFollow(false)
                .isLike(isLike)
                .like_num(post.getLikes().size())
                .isEditable(member.equals(post_member))
                .build());
    }

    @Transactional
    public ResponseMessage<?> updatePost(Long id, PostRequestDto requestDto,
                                         MultipartFile multipartFile,
                                         UserDetailsImpl userDetails) {
        ResponseMessage<?> data = isPresentPost(id);
        if(!data.getIsSuccess()) return data;

        Post post = (Post)data.getData();
        Member member = userDetails.getMember();
        if(!member.equals(post.getMember())) return ResponseMessage.fail(NOT_AUTHORIZED);

        if(null == multipartFile) post.update(requestDto);
        else{
            //이미지 삭제 기능도 추가
            ResponseMessage<?> image_data = imageUploader.uploadFile(multipartFile);
            if(!image_data.getIsSuccess()) return image_data;

            post.update(requestDto, (String) image_data.getData());
        }

        return ResponseMessage.success("update success");
    }

    @Transactional
    public ResponseMessage<?> deletePost(Long id, UserDetailsImpl userDetails) {
        ResponseMessage<?> response = isPresentPost(id);
        if(!response.getIsSuccess()) return response;

        Post post = (Post)response.getData();
        Member member = userDetails.getMember();
        if(!member.equals(post.getMember())) return ResponseMessage.fail(NOT_AUTHORIZED);

        postRepository.delete(post);
        return ResponseMessage.success("delete success");
    }
}

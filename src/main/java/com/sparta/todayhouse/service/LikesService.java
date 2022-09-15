package com.sparta.todayhouse.service;

import com.sparta.todayhouse.dto.ResponseMessage;
import com.sparta.todayhouse.entity.Likes;
import com.sparta.todayhouse.entity.Member;
import com.sparta.todayhouse.entity.Post;
import com.sparta.todayhouse.repository.LikesRepository;
import com.sparta.todayhouse.shared.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.todayhouse.error.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class LikesService {
    private final LikesRepository likesRepository;

    private final PostService postService;

    @Transactional(readOnly = true)
    public ResponseMessage isPresentLikes(Member member, Post post){
        Likes likes = likesRepository.findByMemberAndPost(member, post).orElse(null);
        if(null == likes) return ResponseMessage.fail(LIKES_NOT_FOUND);

        return ResponseMessage.success(likes);
    }

    @Transactional
    public ResponseMessage<?> createLikes(Long id, UserDetailsImpl userDetails){
        ResponseMessage<?> post_data = postService.isPresentPost(id);
        if(!post_data.getIsSuccess()) return post_data;

        Post post = (Post) post_data.getData();
        Member member = userDetails.getMember();

        ResponseMessage<?> likes_data = isPresentLikes(member, post);
        if(likes_data.getIsSuccess()) return ResponseMessage.fail(DUPLICATE_LIKES);

        Likes likes = Likes.builder()
                .member(member)
                .post(post)
                .build();
        likesRepository.save(likes);

        return ResponseMessage.success("register like");
    }

    @Transactional
    public ResponseMessage<?> deleteLikes(Long id, UserDetailsImpl userDetails){
        ResponseMessage<?> post_data = postService.isPresentPost(id);
        if(!post_data.getIsSuccess()) return post_data;

        Post post = (Post) post_data.getData();
        Member member = userDetails.getMember();

        ResponseMessage<?> likes_data = isPresentLikes(member, post);
        if(!likes_data.getIsSuccess()) return likes_data;

        Likes likes = (Likes) likes_data.getData();
        likesRepository.delete(likes);

        return ResponseMessage.success("delete like");
    }
}

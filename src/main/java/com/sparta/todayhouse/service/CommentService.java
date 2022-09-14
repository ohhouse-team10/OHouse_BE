package com.sparta.todayhouse.service;

import com.sparta.todayhouse.dto.ResponseMessage;
import com.sparta.todayhouse.dto.request.CommentRequestDto;
import com.sparta.todayhouse.dto.response.CommentResponseDto;
import com.sparta.todayhouse.entity.Comment;
import com.sparta.todayhouse.entity.Member;
import com.sparta.todayhouse.entity.Post;
import com.sparta.todayhouse.repository.CommentRepository;
import com.sparta.todayhouse.shared.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.sparta.todayhouse.shared.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    private final PostService postService;

    @Transactional(readOnly = true)
    public ResponseMessage<?> isPresentComment(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (null == comment) return ResponseMessage.fail(COMMENT_NOT_FOUND);

        return ResponseMessage.success(comment);
    }

    @Transactional
    public ResponseMessage<?> createComment(Long post_id, CommentRequestDto requestDto,
                                            UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        ResponseMessage<?> post_data = postService.isPresentPost(post_id);
        if (!post_data.getIsSuccess()) return ResponseMessage.fail(POST_NOT_FOUND);

        Comment comment = Comment.builder()
                .member(member)
                .post((Post) post_data.getData())
                .content(requestDto.getContent())
                .build();
        commentRepository.save(comment);

        return ResponseMessage.success("comment success");
    }

    @Transactional(readOnly = true)
    public ResponseMessage<?> getCommentByPost(Long post_id) {
        ResponseMessage<?> post_data = postService.isPresentPost(post_id);
        if (!post_data.getIsSuccess()) return ResponseMessage.fail(POST_NOT_FOUND);
        Post post = (Post) post_data.getData();

        List<CommentResponseDto> responseList = new ArrayList<>();
        List<Comment> comments = post.getComments();

        for (Comment comment : comments) {
            Member member = comment.getMember();
            responseList.add(CommentResponseDto.builder()
                    .comment_id(comment.getId())
                    .profile_image(member.getProfile_image())
                    .nickname(member.getNickname())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .isEditable(false)
                    .build());
        }

        return ResponseMessage.success(responseList);
    }

    @Transactional
    public ResponseMessage<?> updateComment(Long id, CommentRequestDto requestDto,
                                            UserDetailsImpl userDetails){
        ResponseMessage<?> data = isPresentComment(id);
        if(!data.getIsSuccess()) return data;

        Comment comment = (Comment)data.getData();
        Member member = userDetails.getMember();
        if(!member.equals(comment.getMember())) return ResponseMessage.fail(NOT_AUTHORIZED);

        comment.update(requestDto);
        return ResponseMessage.success("update success");
    }

    @Transactional
    public ResponseMessage<?> deleteComment(Long id, UserDetailsImpl userDetails){
        ResponseMessage<?> data = isPresentComment(id);
        if(!data.getIsSuccess()) return data;

        Comment comment = (Comment)data.getData();
        Member member = userDetails.getMember();
        if(!member.equals(comment.getMember())) return ResponseMessage.fail(NOT_AUTHORIZED);

        commentRepository.delete(comment);
        return ResponseMessage.success("delete success");
    }
}

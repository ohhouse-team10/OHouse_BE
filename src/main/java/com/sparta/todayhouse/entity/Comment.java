package com.sparta.todayhouse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.todayhouse.dto.request.CommentRequestDto;
import com.sparta.todayhouse.shared.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends Timestamp{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Post post;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Member member;

    public void update(CommentRequestDto requestDto) { this.content = requestDto.getContent(); }
}

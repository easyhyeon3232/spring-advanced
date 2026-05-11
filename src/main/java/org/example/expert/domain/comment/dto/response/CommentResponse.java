package org.example.expert.domain.comment.dto.response;

import org.example.expert.domain.user.dto.response.UserResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentResponse {

    private final Long id;
    private final String contents;
    private final UserResponse user;

}

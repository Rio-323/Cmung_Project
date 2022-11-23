package com.sparta.cmung_project.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NotFoundMember(HttpStatus.NOT_FOUND.value(), "M001", "유저가 존재하지 않습니다."),
    DuplicatedEmail(HttpStatus.BAD_REQUEST.value(), "M002", "e-mail을 사용하는 유저가 존재합니다"),
    DuplicatedNickname(HttpStatus.BAD_REQUEST.value (), "M003", "Nickname을 사용하는 유저가 존재합니다." ),
    NoContainsBlank(HttpStatus.BAD_REQUEST.value (), "M004", "Nickname은 공백을 포함할 수 없습니다."),
    BadWordsException(HttpStatus.BAD_REQUEST.value (), "M005", "비속어가 포함된 닉네임은 사용할 수 없습니다."),
    WrongPassword(HttpStatus.BAD_REQUEST.value (), "M006", "비밀번호가 틀렸습니다."),

    // post
    MustHaveTitle(HttpStatus.BAD_REQUEST.value (), "P001", "제목은 반드시 입력해야 합니다."),
    NotFoundPost(HttpStatus.NOT_FOUND.value (), "P002", "게시물이 존재하지 않습니다."),

    NoPermissionToChange(HttpStatus.BAD_REQUEST.value (), "P003", "자신이 작성한 포스트만 수정가능합니다."),

    NoPermissionToDelete(HttpStatus.BAD_REQUEST.value (), "P004", "자신이 작성한 포스트만 삭제가능합니다."),
    SearchKeywordLengthError(HttpStatus.BAD_REQUEST.value(), "P005", "검색 단어의 길이는 두 글자 이상입니다."),

    // 필요하신 에러코드 더 작성해주세요!
    NotFoundPet(HttpStatus.NOT_FOUND.value(), "PE001", "애완동물이 존재하지 않습니다."),
    SameUser(HttpStatus.BAD_REQUEST.value(), "C001", "올바르지 않은 요청입니다."),
    NotfoundRoom(HttpStatus.NOT_FOUND.value(), "R001", "채팅방이 없습니다.");


    private final int status;
    private final String code;
    private final String message;
}

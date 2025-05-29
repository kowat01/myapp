package kr.it.academy.myapp.user.vo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class User {

    // ✅ 회원가입/요청용 Request 클래스
    @Setter
    @Getter
    public static class Request {
        private String userId;
        private String passwd;
        private String userName;
        private String nickname;
        private String email;
        private String userAuth;
    }

    // ✅ 응답용 Response 클래스 (상속 제거하고 필드 직접 선언)
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private String userId;
        private String passwd;
        private String userName;
        private String nickname;
        private String email;
        private String userAuth;
        private String useYn;
        private String delYn;
        private List<UserAuth> authList;
        private LocalDateTime createDate;
        private LocalDateTime updateDate;
    }

    // ✅ 권한 객체
    @Builder
    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class UserAuth {
        private String authId;
        private String authName;
    }

    // ✅ 권한 매핑 객체
    @Builder
    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class UserAuthMapping {
        private int seq;
        private String userId;
        private String authId;
    }
}

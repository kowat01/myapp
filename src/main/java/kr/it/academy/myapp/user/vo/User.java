package kr.it.academy.myapp.user.vo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class User {
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

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response extends Request {

        private String useYn;
        private String delYn;
        private List<UserAuth> authList;
        LocalDateTime createDate;
        LocalDateTime updateDate;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class UserAuth{
        private String authId;
        private String authName;

    }

    @Builder
    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class UserAuthMapping{
        private int seq;
        private String userId;
        private String authId;

    }

}

package kr.it.academy.myapp.login.vo;

import lombok.*;

import java.util.List;

public class LoginUser {


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class LoginRequest{
        private String userId;
        private String passwd;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserInfo{
        private String userId;
        private String userName;
        private String nickname;
        private String passwd;
        private List<UserAuth> authList;
        private boolean isAuth;

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UserAuth {
        private String authId;
        private String authName;
    }
}

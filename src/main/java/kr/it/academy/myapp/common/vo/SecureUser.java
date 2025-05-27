package kr.it.academy.myapp.common.vo;

import kr.it.academy.myapp.login.vo.LoginUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

public class SecureUser extends User {
    private static final long serialVersionUID = 1L;
    private static final String ROLE_PREFIX = "ROLE_";

    @Getter
    private String userId;
    @Getter
    private String userName;
    @Getter
    private String userAuth;
    @Getter
    private boolean isAuth;
    @Getter
    private String nickname; // ✅ 닉네임 추가

    public SecureUser(LoginUser.UserInfo user) {
        super(user.getUserName(), user.getPasswd(), makeAuthList(user.getAuthList()));
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userAuth = user.getAuthList().get(0).getAuthId();
        this.isAuth = userAuth.equals("ADMIN");
        this.nickname = user.getNickname(); // ✅ 닉네임 할당
    }

    private static List<GrantedAuthority> makeAuthList(List<LoginUser.UserAuth> authList){
        List<GrantedAuthority> list = new ArrayList<>();
        for(LoginUser.UserAuth auth : authList){
            list.add(new SimpleGrantedAuthority(ROLE_PREFIX + auth.getAuthId()));
        }
        return list;
    }
}

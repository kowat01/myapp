package kr.it.academy.myapp.user.service;

import kr.it.academy.myapp.common.vo.PagingVO;
import kr.it.academy.myapp.user.mapper.UserMapper;
import kr.it.academy.myapp.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PagingVO pagingVO;

    @Transactional
    public Map<String, Object> addUser(User.Request userRequest) throws Exception {
        Map<String, Object> result = new HashMap<>();

        // 권한 누락 시 기본값(BASIC) 자동 부여
        if (userRequest.getUserAuth() == null || userRequest.getUserAuth().isBlank()) {
            userRequest.setUserAuth("BASIC");
        }

        String encodePasswd = passwordEncoder.encode(userRequest.getPasswd());

        // 암호화된 패스워드로 변경
        userRequest.setPasswd(encodePasswd);
        int resultCode = userMapper.addUser(userRequest);

        if (resultCode < 1) {
            throw new Exception("회원가입 실패");
        }

        // 권한 매핑 등록
        User.UserAuthMapping userAuthMapping = User.UserAuthMapping.builder()
                .authId(userRequest.getUserAuth())
                .userId(userRequest.getUserId())
                .build();

        userMapper.addUserAuthMapping(userAuthMapping);

        result.put("resultCode", 200);
        return result;
    }

    public List<User.UserAuth> getUserAuthList() throws Exception {
        return userMapper.getUserAuthList();
    }

    public boolean isUserIdExists(String userId) throws Exception {
        return userMapper.countUserId(userId) > 0;
    }

    // ✅ 이메일 중복 확인 추가
    public boolean isEmailExists(String email) throws Exception {
        return userMapper.countEmail(email) > 0;
    }

    public boolean isNicknameExists(String nickname) throws Exception {
        return userMapper.countNickname(nickname) > 0;
    }
}



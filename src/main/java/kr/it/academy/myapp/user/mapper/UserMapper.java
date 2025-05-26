package kr.it.academy.myapp.user.mapper;

import kr.it.academy.myapp.user.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;
import java.util.List;

@Mapper
public interface UserMapper {

    int addUser(User.Request userRequest);

    // 권한리스트
    List<User.UserAuth> getUserAuthList() throws SQLException;

    // 권한 멥핑 추가
    int addUserAuthMapping(User.UserAuthMapping userAuthMapping) throws SQLException;

    // 아이디 중복 확인
    int countUserId(String userId);

    // ✅ 이메일 중복 확인
    int countEmail(String email);
}

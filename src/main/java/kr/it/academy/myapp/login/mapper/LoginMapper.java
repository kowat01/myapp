package kr.it.academy.myapp.login.mapper;

import kr.it.academy.myapp.login.vo.LoginUser;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLException;

@Mapper
public interface LoginMapper {

    LoginUser.UserInfo login(LoginUser.LoginRequest loginRequest) throws SQLException;

}

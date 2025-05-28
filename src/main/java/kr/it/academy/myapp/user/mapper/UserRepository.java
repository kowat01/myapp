package kr.it.academy.myapp.user.mapper;

import kr.it.academy.myapp.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    // ✅ userId로 nickname 조회
    @Query("SELECT u.nickname FROM UserEntity u WHERE u.userId = :userId")
    String findNicknameByUserId(@Param("userId") String userId);
}

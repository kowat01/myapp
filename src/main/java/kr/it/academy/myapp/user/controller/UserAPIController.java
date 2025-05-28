package kr.it.academy.myapp.user.controller;

import kr.it.academy.myapp.user.service.UserService;
import kr.it.academy.myapp.user.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserAPIController {

    private final UserService userService;

    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> addUser(@RequestBody User.Request userRequest) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            // ✅ ID 중복 확인
            if (userService.isUserIdExists(userRequest.getUserId())) {
                resultMap.put("resultCode", 409);
                resultMap.put("message", "이미 사용 중인 ID입니다.");
                return new ResponseEntity<>(resultMap, HttpStatus.OK);
            }

            // ✅ 이메일 중복 확인
            if (userService.isEmailExists(userRequest.getEmail())) {
                resultMap.put("resultCode", 410);
                resultMap.put("message", "이미 사용 중인 이메일입니다.");
                return new ResponseEntity<>(resultMap, HttpStatus.OK);
            }

            if (userService.isNicknameExists(userRequest.getNickname())) {
                resultMap.put("resultCode", 411);
                resultMap.put("message", "이미 사용 중인 닉네임입니다.");
                return new ResponseEntity<>(resultMap, HttpStatus.OK);
            }

            resultMap = userService.addUser(userRequest);
        } catch (Exception e) {
            resultMap.put("resultCode", 500);
            resultMap.put("message", "회원가입 처리 중 오류 발생");
            e.printStackTrace();
        }
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @GetMapping("/check-id")
    public ResponseEntity<Map<String, Object>> checkDuplicateId(@RequestParam String userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean exists = userService.isUserIdExists(userId);
            result.put("exists", exists);
        } catch (Exception e) {
            result.put("exists", true); // 에러 발생 시 사용 불가 처리
            e.printStackTrace();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // ✅ 이메일 중복 확인 API
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, Object>> checkDuplicateEmail(@RequestParam String email) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean exists = userService.isEmailExists(email);
            result.put("exists", exists);
        } catch (Exception e) {
            result.put("exists", true); // 에러 발생 시 사용 불가 처리
            e.printStackTrace();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Object>> checkDuplicateNickname(@RequestParam String nickname) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean exists = userService.isNicknameExists(nickname);
            result.put("exists", exists);
        } catch (Exception e) {
            result.put("exists", true); // 오류 발생 시 중복 있다고 처리
            e.printStackTrace();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

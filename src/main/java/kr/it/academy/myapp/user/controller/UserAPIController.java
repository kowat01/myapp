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

            resultMap = userService.addUser(userRequest);
        } catch (Exception e) {
            resultMap.put("resultCode", 500);
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

    }


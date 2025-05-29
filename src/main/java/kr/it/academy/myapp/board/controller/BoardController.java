package kr.it.academy.myapp.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.it.academy.myapp.board.service.BoardJPAService;
import kr.it.academy.myapp.board.vo.BoardDTO;
import kr.it.academy.myapp.board.vo.BoardData;
import kr.it.academy.myapp.common.vo.SecureUser;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardJPAService boardJPAService;

    @Value("${upload.image.path:C:/upload/warhammer/uploads/}")
    private String uploadPath;

    private static final List<String> VALID_BOARD_TYPES = List.of("free", "paint", "suggest", "guide", "rule", "wiki");

    private String normalizeBoardType(String input) {
        if (input == null) return "free";
        String bt = input.toLowerCase();
        return VALID_BOARD_TYPES.contains(bt) ? bt : "free";
    }

    @GetMapping("/list")
    public ModelAndView getBoardList(@RequestParam(name = "boardType", defaultValue = "free") String boardType,
                                     @RequestParam(name = "currentPage", defaultValue = "0") int currentPage,
                                     @AuthenticationPrincipal SecureUser user) {
        boardType = normalizeBoardType(boardType);
        if (!"wiki".equals(boardType) && user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        ModelAndView view = new ModelAndView();
        try {
            Map<String, Object> resultData = boardJPAService.getBoardList(currentPage, boardType);
            view.addObject("data", resultData);
            view.addObject("boardType", boardType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.setViewName("views/board/boardList");
        return view;
    }

    @GetMapping("/search")
    public ModelAndView getBoardSearch(@RequestParam(name = "boardType", defaultValue = "free") String boardType,
                                       @RequestParam(name = "searchType") String searchType,
                                       @RequestParam(name = "searchText", defaultValue = "") String searchText,
                                       @RequestParam(name = "currentPage", defaultValue = "0") int currentPage,
                                       @AuthenticationPrincipal SecureUser user) {
        boardType = normalizeBoardType(boardType);
        if (!"wiki".equals(boardType) && user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        ModelAndView view = new ModelAndView();
        try {
            Map<String, Object> resultData = boardJPAService.searchBoardList(currentPage, boardType, searchType, searchText);
            view.addObject("data", resultData);
            view.addObject("boardType", boardType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.addObject("searchType", searchType);
        view.addObject("searchText", searchText);
        view.setViewName("views/board/boardList");
        return view;
    }

    @GetMapping("/detail/view")
    public ModelAndView getBoardDetail(@RequestParam(name = "boardType", defaultValue = "free") String boardType,
                                       @RequestParam(name = "currentPage", defaultValue = "0") int currentPage,
                                       @RequestParam int seq,
                                       @AuthenticationPrincipal SecureUser user,
                                       HttpServletRequest request, HttpServletResponse response) {
        boardType = normalizeBoardType(boardType);
        if (!"wiki".equals(boardType) && user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        ModelAndView view = new ModelAndView();
        try {
            BoardDTO detail = boardJPAService.getBoardDetail(request, response, seq);
            view.addObject("board", detail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        view.addObject("boardType", boardType);
        view.addObject("currentPage", currentPage);
        view.setViewName("views/board/boardDetail");
        return view;
    }

    @GetMapping("/form/view")
    public ModelAndView getBoardForm(@RequestParam(name = "boardType", defaultValue = "free") String boardType,
                                     @RequestParam(name = "currentPage", defaultValue = "0") int currentPage,
                                     @AuthenticationPrincipal SecureUser user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        boardType = normalizeBoardType(boardType);
        ModelAndView view = new ModelAndView();
        view.addObject("boardType", boardType);
        view.addObject("currentPage", currentPage);
        view.setViewName("views/board/boardWrite");
        return view;
    }

    @GetMapping("/update/view")
    public ModelAndView getBoardUpdateForm(@RequestParam(name = "boardType", defaultValue = "free") String boardType,
                                           @RequestParam(name = "currentPage", defaultValue = "0") int currentPage,
                                           @RequestParam int seq,
                                           @AuthenticationPrincipal SecureUser user,
                                           HttpServletRequest request, HttpServletResponse response) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        boardType = normalizeBoardType(boardType);
        ModelAndView view = new ModelAndView();
        try {
            BoardDTO detail = boardJPAService.getBoardDetail(request, response, seq);
            view.addObject("board", detail);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "게시글을 불러오는데 실패했습니다.");
        }
        view.addObject("boardType", boardType);
        view.addObject("currentPage", currentPage);
        view.setViewName("views/board/boardUpdate");
        return view;
    }

    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addBoard(BoardData.Request boardData,
                                        @AuthenticationPrincipal SecureUser user) {
        Map<String, Object> resultMap = new HashMap<>();
        if (user == null) {
            resultMap.put("resultCode", 401);
            resultMap.put("resultMsg", "로그인이 필요합니다.");
            return resultMap;
        }
        try {
            String boardType = normalizeBoardType(boardData.getBoardType());
            boardData.setBoardType(boardType);

            if (Boolean.TRUE.equals(boardData.getIsNotice()) && !user.isAuth()) {
                resultMap.put("resultCode", 403);
                resultMap.put("resultMsg", "공지글 작성 권한이 없습니다.");
                return resultMap;
            }

            resultMap = boardJPAService.addBoard(boardData, user);

            // ✅ boardType을 응답에 명시적으로 포함시켜 리디렉션 문제 해결
            resultMap.put("boardType", boardType);

        } catch (Exception e) {
            resultMap.put("resultCode", 500);
            resultMap.put("resultMsg", "게시글 등록에 실패하였습니다");
            e.printStackTrace();
        }
        return resultMap;
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public Map<String, Object> deleteBoard(@RequestParam int seq,
                                           @AuthenticationPrincipal SecureUser user) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = boardJPAService.deleteBoard(seq, user);
        } catch (Exception e) {
            resultMap.put("resultCode", 500);
            resultMap.put("resultMsg", e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }

    @PostMapping("/upload/image")
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                Files.createDirectories(uploadDir.toPath());
            }
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File dest = new File(uploadDir, fileName);
            file.transferTo(dest);
            return "/uploads/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드 실패");
        }
    }
}

package kr.it.academy.myapp.board.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.it.academy.myapp.board.entity.BoardEntity;
import kr.it.academy.myapp.board.entity.BoardFileEntity;
import kr.it.academy.myapp.board.mapper.BoardFileRepository;
import kr.it.academy.myapp.board.mapper.BoardRepository;
import kr.it.academy.myapp.board.mapper.specification.BoardSpecification;
import kr.it.academy.myapp.board.vo.BoardDTO;
import kr.it.academy.myapp.board.vo.BoardData;
import kr.it.academy.myapp.common.utils.CommonUtils;
import kr.it.academy.myapp.common.utils.CookieUtils;
import kr.it.academy.myapp.common.vo.PagingVO;
import kr.it.academy.myapp.common.vo.SecureUser;
import kr.it.academy.myapp.user.mapper.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardJPAService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final UserRepository userRepository;

    @Value("${server.file.path}")
    private String filePath;

    public Map<String, Object> getBoardList(int currentPage, String boardType) throws Exception {
        Sort sort = Sort.by(Sort.Direction.DESC, "seq");
        PageRequest pageRequest = PageRequest.of(currentPage, 10, sort);
        Page<BoardEntity> pages = boardRepository.findByBoardType(boardType, pageRequest);

        List<BoardDTO> dtoList = pages.getContent().stream().map(entity -> {
            String nickname = userRepository.findNicknameByUserId(entity.getWriter());
            return BoardDTO.of(entity, nickname);
        }).collect(Collectors.toList());

        PagingVO pageVO = new PagingVO();
        pageVO.setData(currentPage, (int) pages.getTotalElements());

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("pageHtml", pageVO.pageHtml());
        resultMap.put("currentPage", currentPage);
        resultMap.put("list", dtoList);
        resultMap.put("totalRows", pages.getTotalElements());

        return resultMap;
    }

    public Map<String, Object> searchBoardList(int currentPage, String boardType, String searchType, String searchText) throws Exception {
        PageRequest pageRequest = PageRequest.of(currentPage, 10);
        BoardSpecification specification = new BoardSpecification(searchType, searchText, boardType);
        Page<BoardEntity> pages = boardRepository.findAll(specification, pageRequest);

        List<BoardDTO> dtoList = pages.getContent().stream().map(entity -> {
            String nickname = userRepository.findNicknameByUserId(entity.getWriter());
            return BoardDTO.of(entity, nickname);
        }).collect(Collectors.toList());

        PagingVO pageVO = new PagingVO();
        pageVO.setData(currentPage, (int) pages.getTotalElements());

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("pageHtml", pageVO.pageHtml());
        resultMap.put("currentPage", currentPage);
        resultMap.put("list", dtoList);
        resultMap.put("totalRows", pages.getTotalElements());

        return resultMap;
    }

    @Transactional
    public BoardDTO getBoardDetail(HttpServletRequest request, HttpServletResponse response, Integer seq) throws Exception {
        BoardEntity board = boardRepository.findById(seq)
                .orElseThrow(() -> new Exception("게시글이 존재하지 않습니다."));

        if (!CookieUtils.checkCookie(request, seq.toString())) {
            board.setReadCount(board.getReadCount() + 1);
            boardRepository.save(board);
            CookieUtils.updateCookie(request, response, seq.toString());
        }

        String nickname = userRepository.findNicknameByUserId(board.getWriter());
        return BoardDTO.of(board, nickname);
    }

    public BoardDTO getBoardDetail(int seq) throws Exception {
        BoardEntity board = boardRepository.findById(seq)
                .orElseThrow(() -> new Exception("게시글이 존재하지 않습니다."));
        String nickname = userRepository.findNicknameByUserId(board.getWriter());
        return BoardDTO.of(board, nickname);
    }

    @Transactional
    public Map<String, Object> addBoard(BoardData.Request request, SecureUser user) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        BoardEntity entity = new BoardEntity();
        entity.setTitle(request.getTitle());
        entity.setContents(request.getContents());
        entity.setWriter(user.getUserId()); // ✅ 핵심 수정: userName → userId
        entity.setBoardType(request.getBoardType());
        boardRepository.save(entity);

        List<MultipartFile> files = Collections.singletonList(request.getFile());
        List<Map<String, Object>> fileMap = CommonUtils.uploadFiles(files, filePath, "board");

        for (Map<String, Object> file : fileMap) {
            BoardFileEntity fileEntity = new BoardFileEntity();
            fileEntity.setFileName(file.get("fileName").toString());
            fileEntity.setStoredFileName(file.get("storedFileName").toString());
            fileEntity.setFilePath(file.get("filePath").toString());
            fileEntity.setBoard(entity);
            boardFileRepository.save(fileEntity);
        }

        resultMap.put("resultCode", 200);
        resultMap.put("resultMsg", "게시글이 등록되었습니다.");
        return resultMap;
    }

    @Transactional
    public Map<String, Object> updateBoard(BoardData.Request request, SecureUser user) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        BoardEntity board = boardRepository.findById(request.getSeq())
                .orElseThrow(() -> new Exception("게시글이 존재하지 않습니다."));

        if (!user.isAuth() && !user.getUserId().equals(board.getWriter())) {
            throw new Exception("수정 권한이 없습니다.");
        }

        board.setTitle(request.getTitle());
        board.setContents(request.getContents());
        board.setBoardType(request.getBoardType());
        board.setUpdateDate(LocalDateTime.now());

        if (request.getFile() != null && !request.getFile().isEmpty()) {
            if (!board.getBoardFiles().isEmpty()) {
                deleteBoardFile(board.getBoardFiles());
            }

            List<MultipartFile> files = Collections.singletonList(request.getFile());
            List<Map<String, Object>> fileMap = CommonUtils.uploadFiles(files, filePath, "board");

            for (Map<String, Object> file : fileMap) {
                BoardFileEntity fileEntity = new BoardFileEntity();
                fileEntity.setFileName(file.get("fileName").toString());
                fileEntity.setStoredFileName(file.get("storedFileName").toString());
                fileEntity.setFilePath(file.get("filePath").toString());
                fileEntity.setBoard(board);
                boardFileRepository.save(fileEntity);
            }
        }

        boardRepository.save(board);
        resultMap.put("resultCode", 200);
        resultMap.put("resultMsg", "게시글이 수정되었습니다.");
        return resultMap;
    }

    @Transactional
    public Map<String, Object> deleteBoard(int seq, SecureUser user) throws Exception {
        BoardEntity board = boardRepository.findById(seq)
                .orElseThrow(() -> new Exception("게시글이 존재하지 않습니다."));

        if (!user.isAuth() && !user.getUserId().equals(board.getWriter())) {
            throw new Exception("삭제 권한이 없습니다.");
        }

        if (!board.getBoardFiles().isEmpty()) {
            deleteBoardFile(board.getBoardFiles());
        }

        boardRepository.delete(board);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("resultCode", 200);
        resultMap.put("resultMsg", "게시글이 삭제되었습니다.");
        return resultMap;
    }

    private void deleteBoardFile(Set<BoardFileEntity> boardFiles) throws Exception {
        for (BoardFileEntity file : boardFiles) {
            String path = file.getFilePath() + file.getStoredFileName();
            CommonUtils.deleteFile(path);
            boardFileRepository.delete(file);
        }
    }
}

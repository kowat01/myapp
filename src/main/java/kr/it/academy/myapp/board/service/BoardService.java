package kr.it.academy.myapp.board.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.it.academy.myapp.board.mapper.BoardMapper;
import kr.it.academy.myapp.board.vo.BoardData;
import kr.it.academy.myapp.common.utils.CommonUtils;
import kr.it.academy.myapp.common.utils.CookieUtils;
import kr.it.academy.myapp.common.vo.PagingVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;
    private final PagingVO pages;

    @Value("${server.file.path}")
    private String filePath;

    public BoardData.ResultData getBoardList(int currentPage, String boardType) throws Exception {
        int totalRows = boardMapper.getBoardTotal(boardType);
        List<BoardData.Response> dataList = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();

        if (totalRows > 0) {
            pages.setData(currentPage, totalRows);
            param.put("offSet", pages.getOffSet());
            param.put("count", pages.getCount());
            param.put("boardType", boardType);

            dataList = boardMapper.getBoardList(param);
        }

        return BoardData.ResultData.builder()
                .totalRows(totalRows)
                .list(dataList)
                .currentPage(currentPage)
                .pageHtml(pages.pageHtml())
                .build();
    }

    public BoardData.Detail getBoardDetail(HttpServletRequest request,
                                           HttpServletResponse response, Integer seq) throws Exception {
        if (!CookieUtils.checkCookie(request, seq.toString())) {
            boardMapper.updateReadCount(seq);
            CookieUtils.updateCookie(request, response, seq.toString());
        }

        return boardMapper.getBoardDetail(seq);
    }

    public BoardData.Detail getBoardDetail(int seq) throws Exception {
        return boardMapper.getBoardDetail(seq);
    }

    public BoardData.BoardFiles getBoardFiles(int seq) throws Exception {
        return boardMapper.getBoardFile(seq);
    }

    @Transactional
    public Map<String, Object> updateBoard(BoardData.Request request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        // 기존 게시글 호출
        BoardData.Detail detail = getBoardDetail(request.getSeq());

        int result = boardMapper.updateBoard(request);
        if (result < 1) {
            throw new Exception("게시글 수정 실패");
        }

        // 수정할 파일이 있으면 기존 파일 삭제 후 새로 등록
        if (!request.getFile().isEmpty()) {
            if (!detail.getFiles().isEmpty()) {
                deleteBoardFile(detail.getFiles());
            }

            List<MultipartFile> files = Arrays.asList(request.getFile());
            List<Map<String, Object>> fileMap =
                    CommonUtils.uploadFiles(files, filePath, "board");

            if (!fileMap.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                for (Map<String, Object> file : fileMap) {
                    BoardData.BoardFiles boardFiles = objectMapper.convertValue(file, BoardData.BoardFiles.class);
                    boardFiles.setBoardSeq(request.getSeq());
                    boardMapper.addFile(boardFiles);
                }
            }
        }

        resultMap.put("resultCode", 200);
        resultMap.put("resultMsg", "게시글을 수정하였습니다");

        return resultMap;
    }

    public Map<String, Object> deleteBoard(int seq) throws Exception {
        BoardData.Detail detail = getBoardDetail(seq);

        if (!detail.getFiles().isEmpty()) {
            deleteBoardFile(detail.getFiles());
        }

        boardMapper.deleteBoard(seq);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("resultCode", 200);
        resultMap.put("resultMsg", "게시글을 삭제하였습니다.");
        return resultMap;
    }

    private void deleteBoardFile(List<BoardData.BoardFiles> boardFiles) throws Exception {
        for (BoardData.BoardFiles file : boardFiles) {
            String path = file.getFilePath() + file.getStoredFileName();
            CommonUtils.deleteFile(path);
            boardMapper.deleteFile(file.getSeq());
        }
    }
}

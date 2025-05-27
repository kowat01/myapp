package kr.it.academy.myapp.board.mapper;

import kr.it.academy.myapp.board.vo.BoardData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {

    // 게시판 종류별 총 게시글 수 조회
    int getBoardTotal(@Param("boardType") String boardType) throws SQLException;

    // 게시판 종류별 게시글 목록 조회 (페이징 포함)
    List<BoardData.Response> getBoardList(Map<String, Object> param) throws SQLException;

    // 게시글 등록
    int addBoard(BoardData.Request request) throws SQLException;

    // 첨부파일 등록
    int addFile(BoardData.BoardFiles boardFiles) throws SQLException;

    // 게시글 상세 조회
    BoardData.Detail getBoardDetail(@Param("seq") int seq) throws SQLException;

    // 파일 정보 조회
    BoardData.BoardFiles getBoardFile(@Param("seq") int seq) throws SQLException;

    // 게시글 수정
    int updateBoard(BoardData.Request request) throws SQLException;

    // 게시글 삭제
    int deleteBoard(@Param("seq") int seq) throws SQLException;

    // 파일 삭제
    int deleteFile(@Param("seq") int seq) throws SQLException;

    // 조회수 증가
    void updateReadCount(@Param("seq") int seq) throws SQLException;
}

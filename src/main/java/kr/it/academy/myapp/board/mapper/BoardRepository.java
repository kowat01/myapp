package kr.it.academy.myapp.board.mapper;

import kr.it.academy.myapp.board.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer>, JpaSpecificationExecutor<BoardEntity> {

    // 🔥 게시판 타입(board_type)에 따라 전체 글 페이징 조회
    Page<BoardEntity> findByBoardType(String boardType, Pageable pageable);

    // ✅ 공지글만 조회 (isNotice가 true인 글만, 최신순 정렬용)
    List<BoardEntity> findByBoardTypeAndIsNoticeTrue(String boardType);

    // ✅ 일반글만 조회 (isNotice가 false인 글만, 페이징용)
    Page<BoardEntity> findByBoardTypeAndIsNoticeFalse(String boardType, Pageable pageable);
}

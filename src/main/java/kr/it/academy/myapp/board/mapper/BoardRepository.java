package kr.it.academy.myapp.board.mapper;

import kr.it.academy.myapp.board.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer>, JpaSpecificationExecutor<BoardEntity> {

    // 🔥 게시판 타입(board_type)에 따라 조회
    Page<BoardEntity> findByBoardType(String boardType, Pageable pageable);

}

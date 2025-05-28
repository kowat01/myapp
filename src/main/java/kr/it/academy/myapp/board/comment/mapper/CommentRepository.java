package kr.it.academy.myapp.board.comment.mapper;

import kr.it.academy.myapp.board.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    // 삭제 여부 상관없이 모두 가져오는 메서드
    List<CommentEntity> findByBoardSeqOrderByParentIdAscSeqAsc(Integer boardSeq);

    // 삭제 안된 것만 가져오는 메서드 (필요 시 사용)
    List<CommentEntity> findByBoardSeqAndDeletedFalseOrderByParentIdAscSeqAsc(Integer boardSeq);
}

package kr.it.academy.myapp.board.comment.mapper;

import kr.it.academy.myapp.board.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    List<CommentEntity> findByBoardSeqOrderBySeqAsc(Integer boardSeq);
}

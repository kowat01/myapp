package kr.it.academy.myapp.board.mapper;

import kr.it.academy.myapp.board.entity.BoardFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFileRepository extends JpaRepository<BoardFileEntity, Integer> {
}

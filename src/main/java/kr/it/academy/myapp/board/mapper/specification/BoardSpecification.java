package kr.it.academy.myapp.board.mapper.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import kr.it.academy.myapp.board.entity.BoardEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BoardSpecification implements Specification<BoardEntity> {

    private final String boardType;
    private final String searchType;
    private final String searchText;

    public BoardSpecification(String boardType, String searchType, String searchText) {
        this.boardType = boardType;
        this.searchType = searchType;
        this.searchText = searchText;
    }

    @Override
    public Predicate toPredicate(Root<BoardEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // 게시판 타입 필터 추가
        if (boardType != null && !boardType.isBlank()) {
            predicates.add(cb.equal(root.get("boardType"), boardType));
        }

        if (searchText != null && !searchText.isBlank()) {
            String likeText = "%" + searchText + "%";
            Predicate titleLike = cb.like(root.get("title"), likeText);
            Predicate writerLike = cb.like(root.get("writer"), likeText);

            if (searchType.equalsIgnoreCase("all")) {
                predicates.add(cb.or(titleLike, writerLike));
            } else if (searchType.equalsIgnoreCase("title")) {
                predicates.add(titleLike);
            } else if (searchType.equalsIgnoreCase("writer")) {
                predicates.add(writerLike);
            }
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}

package kr.it.academy.myapp.board.vo;

import kr.it.academy.myapp.board.entity.BoardEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private Integer seq;
    private String title;
    private String writer;
    private String contents;
    private Integer readCount;
    private String nickname; // ✅ writer의 user_id로부터 조회된 nickname
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String boardType;

    private Set<BoardFileDTO> files = new LinkedHashSet<>();

    /**
     * 닉네임 없이 변환하는 기본 of 메서드 (별도 주입 필요)
     */
    public static BoardDTO of(BoardEntity entity) {
        return of(entity, null);
    }

    /**
     * 닉네임까지 포함하여 DTO 변환
     */
    public static BoardDTO of(BoardEntity entity, String nickname) {
        Set<BoardFileDTO> boardFiles = entity.getBoardFiles().stream()
                .map(BoardFileDTO::of)
                .collect(Collectors.toSet());

        return BoardDTO.builder()
                .seq(entity.getSeq())
                .title(entity.getTitle())
                .writer(entity.getWriter())
                .contents(entity.getContents())
                .readCount(entity.getReadCount())
                .createDate(entity.getCreateDate())
                .updateDate(entity.getUpdateDate())
                .boardType(entity.getBoardType())
                .nickname(nickname) // ✅ 외부에서 주입
                .files(boardFiles)
                .build();
    }
}

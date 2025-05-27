package kr.it.academy.myapp.board.comment.vo;

import kr.it.academy.myapp.board.comment.entity.CommentEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Integer seq;
    private Integer boardSeq;
    private String writerId;
    private String nickname;
    private String contents;
    private LocalDateTime createDate;
    private Integer parentId;
    private boolean deleted;

    public static CommentDTO of(CommentEntity entity, String nickname) {
        return CommentDTO.builder()
                .seq(entity.getSeq())
                .boardSeq(entity.getBoardSeq())
                .writerId(entity.getWriterId())
                .nickname(nickname)
                .contents(entity.getContents())
                .createDate(entity.getCreateDate())
                .parentId(entity.getParentId())
                .deleted(entity.isDeleted())
                .build();
    }
}

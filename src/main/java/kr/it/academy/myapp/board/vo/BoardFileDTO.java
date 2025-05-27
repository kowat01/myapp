package kr.it.academy.myapp.board.vo;

import kr.it.academy.myapp.board.entity.BoardFileEntity;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardFileDTO {

    private Integer seq;

    private String fileName;

    private String storedFileName;

    private String filePath;

    private LocalDateTime createDate;

    public static BoardFileDTO of(BoardFileEntity entity) {
        return BoardFileDTO
                .builder()
                .seq(entity.getSeq())
                .fileName(entity.getFileName())
                .storedFileName(entity.getStoredFileName())
                .filePath(entity.getFilePath())
                .createDate(entity.getCreateDate())
                .build();
    }
}

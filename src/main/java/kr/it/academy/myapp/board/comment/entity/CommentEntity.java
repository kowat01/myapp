package kr.it.academy.myapp.board.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq;

    @Column(name = "board_seq", nullable = false)
    private Integer boardSeq;

    @Column(name = "writer_id", nullable = false)
    private String writerId;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "create_date", insertable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", insertable = false, updatable = false)
    private LocalDateTime updateDate;
}

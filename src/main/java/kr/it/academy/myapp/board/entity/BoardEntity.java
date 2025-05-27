package kr.it.academy.myapp.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "board")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false)
    private Integer seq;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "writer", nullable = false, length = 50)
    private String writer;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "read_count", insertable = false)
    private Integer readCount;

    @Column(name = "create_date", insertable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date", insertable = false, updatable = false)
    private LocalDateTime updateDate;

    @Column(name = "board_type", nullable = false, length = 20) // ✅ 추가
    private String boardType;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BoardFileEntity> boardFiles = new LinkedHashSet<>();
}

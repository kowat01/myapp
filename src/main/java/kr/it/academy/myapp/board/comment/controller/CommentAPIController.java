package kr.it.academy.myapp.board.comment.controller;

import kr.it.academy.myapp.board.comment.service.CommentService;
import kr.it.academy.myapp.board.comment.vo.CommentDTO;
import kr.it.academy.myapp.common.vo.SecureUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentAPIController {

    private final CommentService commentService;

    // 댓글 목록 조회
    @GetMapping("/{boardSeq}")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable Integer boardSeq) {
        List<CommentDTO> comments = commentService.getCommentsByBoardSeq(boardSeq);
        return ResponseEntity.ok(comments);
    }

    // 댓글 등록
    @PostMapping("/add")
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO dto,
                                                 @AuthenticationPrincipal SecureUser user) {
        CommentDTO saved = commentService.addComment(dto, user);
        return ResponseEntity.ok(saved);
    }

    // 댓글 삭제 (논리 삭제)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id,
                                              @AuthenticationPrincipal SecureUser user) {
        boolean deleted = commentService.deleteComment(id, user);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    // 댓글 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Integer id,
                                                    @RequestBody CommentDTO dto,
                                                    @AuthenticationPrincipal SecureUser user) {
        CommentDTO updated = commentService.updateComment(id, dto, user);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.status(403).build();
    }
}

package kr.it.academy.myapp.board.comment.service;

import kr.it.academy.myapp.board.comment.vo.CommentDTO;
import kr.it.academy.myapp.common.vo.SecureUser;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getCommentsByBoardSeq(Integer boardSeq);
    CommentDTO addComment(CommentDTO dto, SecureUser user);
    boolean deleteComment(Integer id, SecureUser user);
    CommentDTO updateComment(Integer id, CommentDTO dto, SecureUser user);
}

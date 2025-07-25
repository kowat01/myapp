package kr.it.academy.myapp.board.comment.service;

import kr.it.academy.myapp.board.comment.entity.CommentEntity;
import kr.it.academy.myapp.board.comment.mapper.CommentRepository;
import kr.it.academy.myapp.board.comment.vo.CommentDTO;
import kr.it.academy.myapp.common.vo.SecureUser;
import kr.it.academy.myapp.user.mapper.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public List<CommentDTO> getCommentsByBoardSeq(Integer boardSeq) {
        // 삭제된 댓글도 모두 가져와서 프론트에서 처리함
        return commentRepository.findByBoardSeqOrderByParentIdAscSeqAsc(boardSeq).stream()
                .map(entity -> {
                    String nickname = userRepository.findNicknameByUserId(entity.getWriterId());
                    return CommentDTO.of(entity, nickname);
                })
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO addComment(CommentDTO dto, SecureUser user) {
        CommentEntity entity = new CommentEntity();
        entity.setBoardSeq(dto.getBoardSeq());
        entity.setContents(dto.getContents());
        entity.setWriterId(user.getUserId());
        entity.setParentId(dto.getParentId());
        entity.setCreateDate(LocalDateTime.now());
        entity.setUpdateDate(LocalDateTime.now());
        entity.setDeleted(false);

        CommentEntity saved = commentRepository.save(entity);
        String nickname = userRepository.findNicknameByUserId(user.getUserId());

        return CommentDTO.of(saved, nickname);
    }

    @Override
    public boolean deleteComment(Integer id, SecureUser user) {
        return commentRepository.findById(id)
                .filter(comment -> user.isAuth() || comment.getWriterId().equals(user.getUserId()))
                .map(comment -> {
                    comment.setDeleted(true);
                    comment.setContents("-삭제된 댓글입니다-");
                    comment.setUpdateDate(LocalDateTime.now());
                    commentRepository.save(comment);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public CommentDTO updateComment(Integer id, CommentDTO dto, SecureUser user) {
        return commentRepository.findById(id)
                .filter(comment -> user.isAuth() || comment.getWriterId().equals(user.getUserId()))
                .filter(comment -> !comment.isDeleted())
                .map(comment -> {
                    comment.setContents(dto.getContents());
                    comment.setUpdateDate(LocalDateTime.now());
                    CommentEntity updated = commentRepository.save(comment);
                    String nickname = userRepository.findNicknameByUserId(comment.getWriterId());
                    return CommentDTO.of(updated, nickname);
                })
                .orElseThrow(() -> new RuntimeException("댓글 수정 권한이 없거나 댓글이 존재하지 않습니다."));
    }
}

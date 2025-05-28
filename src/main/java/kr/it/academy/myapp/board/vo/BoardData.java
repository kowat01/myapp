package kr.it.academy.myapp.board.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public class BoardData {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultData {
        private int currentPage;
        private List<Response> list;
        private String pageHtml;
        private int totalRows;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private int seq;
        private String title;
        private String writer;
        private int readCount;
        private boolean isNotice; // ✅ 공지 여부 추가
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updateDate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {
        private int seq;
        private String title;
        private String writer;
        private int readCount;
        private String contents;
        private String boardType;
        private boolean isNotice; // ✅ 공지 여부 추가
        private List<BoardFiles> files;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createDate;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    public static class Request {
        private int seq;
        private String title;
        private String writer;
        private String contents;
        private String boardType;
        private Boolean isNotice;  // primitive boolean에서 Boolean으로 변경
        private MultipartFile file;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardFiles {
        private int seq;
        @Setter
        private int boardSeq;
        private String fileName;
        private String storedFileName;
        private String filePath;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createDate;
    }
}

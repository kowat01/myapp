package kr.it.academy.myapp.board.controller;

import kr.it.academy.myapp.board.service.BoardService;
import kr.it.academy.myapp.board.vo.BoardData;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardAPIController {
    private final BoardService boardService;
    private final ResourceLoader resourceLoader;

    @GetMapping("/down")
    public ResponseEntity<UrlResource> downFile(@RequestParam int fileId) {
        String fileName = "";
        String storedFileName = "";

        UrlResource resource = null;

        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.OK;

        try{
            BoardData.BoardFiles boardFiles = boardService.getBoardFiles(fileId);
            if(boardFiles != null) {
                fileName = boardFiles.getFileName();
                storedFileName = boardFiles.getStoredFileName();

                String fullPath = boardFiles.getFilePath() + boardFiles.getStoredFileName();

                File file = new File(fullPath);

                if(!file.exists()) {
                    throw new FileNotFoundException("파일이 존재하지 않습니다");
                }
                // 파일 확장타입 찾기 > 없으면 기본적으로 binary 타입
                String mimeType = Files.probeContentType(file.toPath());

                if(mimeType == null) {
                    mimeType = "application/octet-stream";
                }

                // 전송할 객체 담기
                resource = new UrlResource(file.toURI());
                // 한글꺼짐 방지를 위해서 파일명 인코딩
                // * 기호는 경로에서 오류 발생 고지가 있어서 대응하는 바이트 코드로 변경
                String encodeName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");

                // 해더 설정
                // 헤더의 내용에 따라 내용이 달라진다
                headers.setContentDisposition(
                        ContentDisposition
                                .builder("attachment")
                                .filename(fileName, StandardCharsets.UTF_8)
                                .build());
                headers.setCacheControl("no-cache");
                headers.setContentType(MediaType.parseMediaType(mimeType));
            }

        }catch (Exception e){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        }
        return new ResponseEntity<>(resource, headers, status);
    }

}

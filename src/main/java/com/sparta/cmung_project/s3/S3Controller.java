package com.sparta.cmung_project.s3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/file")
public class S3Controller {
    @Autowired
    private S3Service s3Service;

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadFile(@RequestPart MultipartFile image,
                                             @RequestPart String content) {
        log.info("파일 업로드");
        return new ResponseEntity<>(s3Service.uploadFile(image), HttpStatus.OK);
    }
}

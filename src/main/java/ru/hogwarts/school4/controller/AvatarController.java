package ru.hogwarts.school4.controller;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school4.model.Avatar;
import ru.hogwarts.school4.service.AvatarService;

import java.io.FileInputStream;
import java.io.IOException;


@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> save(@RequestParam Long studentId, @RequestBody MultipartFile multipartFile) {
        try {
            avatarService.save(studentId, multipartFile);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/fromDb")
    public ResponseEntity<byte[]> getFromDb(@RequestParam Long studentId) {
        Avatar avatar = avatarService.getAvatar(studentId);
        String mediaType = avatar.getMediaType();
        byte[] data = avatar.getData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(avatar.getFileSize());
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        return ResponseEntity.status(200).headers(headers).body(data);
    }

    @GetMapping(value = "/fromDisk")
    public void getFromDisk(@RequestParam Long studentId, HttpServletResponse response) {
        Avatar avatar = avatarService.getAvatar(studentId);
        response.setStatus(200);
        response.setHeader("content - type", avatar.getMediaType());
        response.setHeader("content - length", String.valueOf(avatar.getFileSize()));
        try {
            ServletOutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(avatar.getFilePath());
            fis.transferTo(os);
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }


}


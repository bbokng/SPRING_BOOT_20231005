package com.example.demo.Controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    @PostMapping("/upload-email")
    public String uploadEmail(@RequestParam String email,
                              @RequestParam String subject,
                              @RequestParam String message,
                              @RequestParam(value = "files", required = false) List<MultipartFile> files,
                              RedirectAttributes redirectAttributes) {
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String sanitizedEmail = email.replaceAll("[^a-zA-Z0-9]", "_");
            String filename = sanitizedEmail + ".txt";
            Path targetFile = uploadPath.resolve(filename);

            if (Files.exists(targetFile)) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                filename = sanitizedEmail + "_" + timestamp + "_" + UUID.randomUUID() + ".txt";
                targetFile = uploadPath.resolve(filename);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(targetFile)) {
                writer.write("메일 제목: " + subject);
                writer.newLine();
                writer.write("요청 메시지:");
                writer.newLine();
                writer.write(message);
            }

            if (files != null) {
                for (MultipartFile file : files) {
                    if (file.isEmpty()) {
                        continue;
                    }
                    String originalName = file.getOriginalFilename() == null ? "file" : file.getOriginalFilename();
                    String sanitizedName = originalName.replaceAll("[^a-zA-Z0-9._-]", "_");
                    Path fileTarget = uploadPath.resolve(sanitizedName);
                    if (Files.exists(fileTarget)) {
                        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                        sanitizedName = sanitizedName + "_" + timestamp + "_" + UUID.randomUUID();
                        fileTarget = uploadPath.resolve(sanitizedName);
                    }
                    Files.copy(file.getInputStream(), fileTarget);
                    log.info("첨부 업로드 완료: {}", fileTarget.toAbsolutePath());
                }
            }

            log.info("메일 업로드 완료: {}", targetFile.toAbsolutePath());
            redirectAttributes.addFlashAttribute("message", "메일 내용이 성공적으로 업로드되었습니다!");
            return "redirect:/upload-success";
        } catch (IOException e) {
            log.error("메일 업로드 실패", e);
            return "redirect:/upload-error";
        }
    }

    @GetMapping("/upload-success")
    public String uploadSuccess() {
        return "upload_end";
    }

    @GetMapping("/upload-error")
    public String uploadError() {
        return "error_page/upload_error";
    }
}

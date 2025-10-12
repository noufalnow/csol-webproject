package com.dms.kalari.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.dms.kalari.core.entity.CoreFile;
import com.dms.kalari.core.repository.CoreFileRepository;
import com.dms.kalari.events.entity.MemberEventItem;
import com.dms.kalari.events.service.MemberEventItemService;
import com.dms.kalari.util.XorMaskHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private CoreFileRepository fileRepository;
    private MemberEventItemService memberEventItemService;
    
    
    public FileController(MemberEventItemService memberEventItemService) {
        this.memberEventItemService = memberEventItemService;
    }


    private static final String DEFAULT_FILE_PATH = "/opt/app/uploads/default.jpg"; // change to your default file path

    @GetMapping("/view/{id}")
    public ResponseEntity<InputStreamResource> viewFile(@PathVariable Long id) throws IOException {
        CoreFile file = fileRepository.findById(id).orElse(null);

        File diskFile = (file != null && file.getFilePath() != null) 
                        ? new File(file.getFilePath()) 
                        : null;

        // Fallback if file not found
        if (diskFile == null || !diskFile.exists()) {
            diskFile = new File(DEFAULT_FILE_PATH);
        }

        MediaType mediaType = detectMimeType(diskFile);

        InputStreamResource resource = new InputStreamResource(new FileInputStream(diskFile));
        long contentLength = (file != null && file.getFileSize() != null) ? file.getFileSize() : diskFile.length();
        String fileName = (file != null && file.getFileActualName() != null) ? file.getFileActualName() : "default.jpg";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + fileName + "\"")
                .contentLength(contentLength)
                .contentType(mediaType)
                .body(resource);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long id) throws IOException {
        CoreFile file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        File diskFile = new File(file.getFilePath());
        if (!diskFile.exists()) {
            diskFile = new File(DEFAULT_FILE_PATH);
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(diskFile));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + file.getFileActualName() + "\"")
                .contentLength(file.getFileSize())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
    
    @GetMapping("/certificate/{maskedId}")
    public ResponseEntity<InputStreamResource> downloadCertificate(@PathVariable("maskedId") Long maskedId) throws IOException {

        // Unmask the ID
        Long meiId = XorMaskHelper.unmask(maskedId);

        // Fetch the MemberEventItem
        MemberEventItem mei = memberEventItemService.findById(meiId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid meiId: " + meiId));

        // Get file path and name
        String filePath = mei.getMeiCertificatePath();
        String fileName = mei.getMeiCertificateFile();
        
        System.out.println("Certificate Path: " + filePath);
        System.out.println("Certificate File: " + fileName);


        File file = new File(filePath, fileName);
        
        System.out.println("Absolute File Path: " + file.getAbsolutePath());
        
        if (!file.exists()) {
            throw new IllegalArgumentException("File not found: " + file.getAbsolutePath());
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        // Set headers to prompt download
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + fileName + "\"")
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    // Utility method to detect mime type safely
    private MediaType detectMimeType(File file) {
        try {
            String mime = Files.probeContentType(file.toPath());
            return (mime != null) ? MediaType.parseMediaType(mime) : MediaType.APPLICATION_OCTET_STREAM;
        } catch (IOException e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}

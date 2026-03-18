package com.dms.kalari.branch.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dms.kalari.branch.service.NodeService;
import com.dms.kalari.core.entity.CoreFile;
import com.dms.kalari.core.repository.CoreFileRepository;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@RestController
@RequestMapping("/")
public class PublicSiteController {

	private final NodeService nodeService;
	private CoreFileRepository fileRepository;
	
	private static final String DEFAULT_FILE_PATH = "/opt/app/uploads/default.jpg"; 

	public PublicSiteController(NodeService nodeService, CoreFileRepository fileRepository) {
		this.nodeService = nodeService;
		this.fileRepository = fileRepository;
	}

	@CrossOrigin(origins = "*")
	@GetMapping("branch_public/{code}")
	public ResponseEntity<?> getBranchByCode(@PathVariable String code) {

		Map<String, Object> branchData = nodeService.getFullBranchDetailsByCode(code);

		if (branchData == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("status", "error", "message", "Branch not found"));
		}

		return ResponseEntity.ok(Map.of("status", "success", "data", branchData));
	}
	
	@CrossOrigin(origins = "*")
    @GetMapping("image_public/{id}")
	public ResponseEntity<InputStreamResource> viewFilePublic(
	        @PathVariable Long id,
	        @RequestParam(required = false, defaultValue = "false") boolean thumbnail
	) throws IOException {

	    CoreFile file = fileRepository.findById(id).orElse(null);

	    File originalFile = (file != null && file.getFilePath() != null)
	            ? new File(file.getFilePath())
	            : new File(DEFAULT_FILE_PATH);

	    if (!originalFile.exists()) {
	        originalFile = new File(DEFAULT_FILE_PATH);
	    }

	    // 🔥 Thumbnail logic
	    if (thumbnail) {

	        File thumbnailDir = new File(DEFAULT_FILE_PATH, "thumbnails");

	        if (!thumbnailDir.exists()) {
	            thumbnailDir.mkdirs();
	        }

	        File thumbnailFile = new File(thumbnailDir, id + ".jpg");

	        // ✅ 1. If exists → return immediately
	        if (thumbnailFile.exists()) {
	            return buildResponse(thumbnailFile, MediaType.IMAGE_JPEG);
	        }

	        // ⚠️ Prevent duplicate generation (basic lock)
	        synchronized (id.toString().intern()) {

	            // Double check after lock
	            if (!thumbnailFile.exists()) {

	                MediaType mediaType = detectMimeType(originalFile);

	                // ❌ Skip non-images
	                if (mediaType == null || !mediaType.toString().startsWith("image")) {
	                    return buildResponse(originalFile, mediaType);
	                }

	                // ✅ Generate thumbnail once
	                Thumbnails.of(originalFile)
	                        .size(200, 200)
	                        .outputFormat("jpg")
	                        .toFile(thumbnailFile);
	            }
	        }

	        return buildResponse(thumbnailFile, MediaType.IMAGE_JPEG);
	    }

	    // 🔥 Return original file
	    MediaType mediaType = detectMimeType(originalFile);
	    return buildResponse(originalFile, mediaType);
	}
	
	private ResponseEntity<InputStreamResource> buildResponse(File file, MediaType mediaType) throws IOException {
	    InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

	    return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + file.getName() + "\"")
	            .contentLength(file.length())
	            .contentType(mediaType)
	            .body(resource);
	}
    
    private MediaType detectMimeType(File file) {
        try {
            String mime = Files.probeContentType(file.toPath());
            return (mime != null) ? MediaType.parseMediaType(mime) : MediaType.APPLICATION_OCTET_STREAM;
        } catch (IOException e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}

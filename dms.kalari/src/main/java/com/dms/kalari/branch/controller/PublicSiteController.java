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
	public ResponseEntity<InputStreamResource> viewThumbnail(@PathVariable Long id) throws IOException {

	    //log.info("Thumbnail request for id={}", id);

	    CoreFile file = fileRepository.findById(id).orElse(null);

	    File originalFile = (file != null && file.getFilePath() != null)
	            ? new File(file.getFilePath())
	            : new File(DEFAULT_FILE_PATH);

	    if (!originalFile.exists()) {
	        originalFile = new File(DEFAULT_FILE_PATH);
	    }

	    // 🔥 Key fix here
	    File parentDir = originalFile.getParentFile();
	    File thumbnailDir = new File(parentDir, "thumbnails");

	    if (!thumbnailDir.exists()) {
	        thumbnailDir.mkdirs();
	    }

	    File thumbnailFile = new File(thumbnailDir, id + ".jpg");

	    // ✅ Return cached
	    if (thumbnailFile.exists()) {
	        //log.info("Returning cached thumbnail");
	        return buildResponse(thumbnailFile, MediaType.IMAGE_JPEG);
	    }

	    synchronized (id.toString().intern()) {

	        if (!thumbnailFile.exists()) {

	            MediaType mediaType = detectMimeType(originalFile);

	            if (mediaType == null || !mediaType.toString().startsWith("image")) {
	                //log.warn("Not an image, returning original");
	                return buildResponse(originalFile, mediaType);
	            }

	            //log.info("Generating thumbnail...");

	            try {
	                Thumbnails.of(originalFile)
	                        .size(200, 200)
	                        .outputFormat("jpg")
	                        .toFile(thumbnailFile);
	            } catch (Exception e) {
	                throw new RuntimeException("Thumbnail generation failed for: " + originalFile.getAbsolutePath(), e);
	            }
	        }
	    }

	    return buildResponse(thumbnailFile, MediaType.IMAGE_JPEG);
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

package com.dms.kalari.branch.controller;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class PublicSiteController {

	private final NodeService nodeService;
	private CoreFileRepository fileRepository;
	
	private static final String DEFAULT_FILE_PATH = "/opt/app/uploads/default.jpg"; 

	@Value("${app.upload.base-path}")
	private String BASE_PATH;
	

	public PublicSiteController(NodeService nodeService, CoreFileRepository fileRepository) {
		this.nodeService = nodeService;
		this.fileRepository = fileRepository;
	}

	@CrossOrigin(origins = "*")
	@GetMapping("branch_public")
	public ResponseEntity<?> getBranch(
	        @RequestParam(required = false) String code,
	        @RequestParam(required = false) UUID id
	) {
		
	    if (id == null && code== null) {
	        return ResponseEntity.badRequest().build();
	    }

	    Map<String, Object> branchData = nodeService.getFullBranchDetails(code, id);

	    return ResponseEntity.ok(Map.of(
	            "status", "success",
	            "data", branchData
	    ));
	}
	

	
	@CrossOrigin(origins = "*")
	@GetMapping("image_public/{id}")
	public ResponseEntity<InputStreamResource> viewThumbnail(@PathVariable Long id) throws IOException {

	    if (id == null) {
	        return ResponseEntity.badRequest().build();
	    }
    
	    CoreFile file = fileRepository.findById(id).orElse(null);

	    File originalFile = (file != null && file.getFilePath() != null)
	            ? new File(BASE_PATH, file.getFilePath())
	            : new File(DEFAULT_FILE_PATH);

	    if (!originalFile.exists()) {
	        originalFile = new File(DEFAULT_FILE_PATH);
	    }

	    File parentDir = originalFile.getParentFile();
	    File thumbnailDir = new File(parentDir, "thumbnails");

	    if (!thumbnailDir.exists()) {
	        thumbnailDir.mkdirs();
	    }

	    String extension = FilenameUtils.getExtension(originalFile.getName());

	    if (extension == null || extension.isBlank()) {
	        extension = "jpg";
	    }

	    extension = extension.toLowerCase();

	    // Thumbnailator may not support all formats
	    if (!List.of("jpg", "jpeg", "png", "gif", "bmp").contains(extension)) {
	        extension = "jpg";
	    }

	    File thumbnailFile = new File(
	            thumbnailDir,
	            id + "." + extension
	    );

	    // Return cached thumbnail
	    if (thumbnailFile.exists()) {
	        MediaType thumbnailType = detectMimeType(thumbnailFile);

	        if (thumbnailType == null) {
	            thumbnailType = MediaType.IMAGE_JPEG;
	        }

	        return buildResponse(thumbnailFile, thumbnailType);
	    }

	    synchronized (id.toString().intern()) {

	        if (!thumbnailFile.exists()) {

	            MediaType mediaType = detectMimeType(originalFile);

	            if (mediaType == null || !mediaType.toString().startsWith("image")) {
	                return buildResponse(originalFile, mediaType);
	            }

	            try {

	                Thumbnails.of(originalFile)
	                        .size(200, 200)
	                        .outputFormat(extension)
	                        .toFile(thumbnailFile);

	            } catch (Exception e) {

	                throw new RuntimeException(
	                        "Thumbnail generation failed for: "
	                                + originalFile.getAbsolutePath(),
	                        e
	                );
	            }
	        }
	    }

	    MediaType thumbnailType = detectMimeType(thumbnailFile);

	    if (thumbnailType == null) {
	        thumbnailType = MediaType.IMAGE_JPEG;
	    }								

	    return buildResponse(thumbnailFile, thumbnailType);
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping("image_public_raw/{id}")
	public ResponseEntity<InputStreamResource> viewRaw(@PathVariable Long id) throws IOException {

	    if (id == null) {
	        return ResponseEntity.notFound().build();
	    }

	    CoreFile file = fileRepository.findById(id).orElse(null);

	    if (file == null || file.getFilePath() == null) {
	        return ResponseEntity.notFound().build();
	    }

	    File originalFile = new File(file.getFilePath()); // ← no BASE_PATH, same as viewFile

	    if (!originalFile.exists() || !originalFile.isFile()) {
	        return ResponseEntity.notFound().build();
	    }

	    MediaType mediaType = detectMimeType(originalFile);

	    return buildResponse(originalFile, mediaType != null ? mediaType : MediaType.APPLICATION_OCTET_STREAM);
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

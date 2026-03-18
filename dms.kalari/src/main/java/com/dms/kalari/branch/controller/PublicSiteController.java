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
    public ResponseEntity<InputStreamResource> viewFilePublic(@PathVariable Long id) throws IOException {
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
    
    private MediaType detectMimeType(File file) {
        try {
            String mime = Files.probeContentType(file.toPath());
            return (mime != null) ? MediaType.parseMediaType(mime) : MediaType.APPLICATION_OCTET_STREAM;
        } catch (IOException e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}

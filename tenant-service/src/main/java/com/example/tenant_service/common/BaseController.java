package com.example.tenant_service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class BaseController<DTO, S extends BaseService<DTO>> {

    protected final S service;
    private final Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected BaseController(S service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DTO>> getAll() {
        logger.debug("Received GET request for all resources");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DTO> getById(@PathVariable Long id) {
        logger.debug("Received GET request for resource with id: {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    /*@PostMapping
    public ResponseEntity<DTO> create(@RequestBody DTO dto) {
        logger.debug("Received POST request to create resource with data: {}", dto);
        return ResponseEntity.ok(service.save(dto));
    }*/
    
    @PostMapping
    public ResponseEntity<DTO> create(@RequestBody DTO dto) {
        logger.debug("POST request received with data: {}", dto);
        try {
        	DTO createdUser = service.save(dto);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            logger.error("Error during POST request: ", e);
            throw e;  // Re-throw to be caught by the global exception handler
        }
    }

    
    
    

    @PutMapping("/{id}")
    public ResponseEntity<DTO> update(@PathVariable Long id, @RequestBody DTO dto) {
        logger.debug("Received PUT request to update resource with id: {} and data: {}", id, dto);
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.debug("Received DELETE request for resource with id: {}", id);
        service.softDeleteById(id);
        return ResponseEntity.noContent().build();
    }
}

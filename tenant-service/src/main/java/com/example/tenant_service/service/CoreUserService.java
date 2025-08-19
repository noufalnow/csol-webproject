package com.example.tenant_service.service;

import com.example.tenant_service.exception.ResourceNotFoundException;
import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.document.UserMemberDocument;
import com.example.tenant_service.dto.users.CoreUserDTO;
import com.example.tenant_service.dto.users.CoreUserPasswordDTO;
import com.example.tenant_service.dto.users.CoreUserUpdateDTO;
import com.example.tenant_service.dto.users.CoreUserUpdateMemberDTO;
import com.example.tenant_service.dto.users.UserMemberDTO;
import com.example.tenant_service.entity.CoreUser;
import com.example.tenant_service.entity.CoreUser.UserType;
import com.example.tenant_service.entity.MisDesignation;
import com.example.tenant_service.entity.Node;
import com.example.tenant_service.mapper.CoreUserMapper;
import com.example.tenant_service.mapper.UserMemberMapper;
import com.example.tenant_service.repository.CoreUserRepository;
import com.example.tenant_service.repository.MisDesignationRepository;
import com.example.tenant_service.repository.NodeRepository;
import com.example.tenant_service.repository.UserMemberElasticsearchRepository;
import org.springframework.data.domain.PageRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHits;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;





@Service
public class CoreUserService implements BaseService<CoreUserDTO> {

    private final CoreUserRepository coreUserRepository;
    private final UserMemberElasticsearchRepository userMemberElasticsearchRepository;
    
    private final CoreUserMapper coreUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final MisDesignationRepository misDesignationRepository;
	private final NodeRepository nodeRepository;
	private final ElasticsearchOperations elasticsearchOperations;
	private static final Logger log = LoggerFactory.getLogger(CoreUserService.class);


    @Autowired
    public CoreUserService(CoreUserRepository coreUserRepository,
                           CoreUserMapper coreUserMapper,
                           PasswordEncoder passwordEncoder,
                           MisDesignationRepository misDesignationRepository,
                           NodeRepository nodeRepository,
                           UserMemberElasticsearchRepository userMemberElasticsearchRepository, ElasticsearchOperations  elasticsearchOperations) {
        this.coreUserRepository = coreUserRepository;
        this.coreUserMapper = coreUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.misDesignationRepository = misDesignationRepository;
        this.nodeRepository = nodeRepository;
        this.userMemberElasticsearchRepository = userMemberElasticsearchRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    
    public Page<CoreUserDTO> searchUsersInElasticsearch(
            String query, 
            int page, 
            int size, 
            String sortField, 
            String sortDir) {
        
        // Create Pageable with sorting
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        // Build the search query
        NativeQuery searchQuery;
        if (query == null || query.trim().isEmpty()) {
            // Return all documents when no search query is provided
            searchQuery = NativeQuery.builder()
                    .withQuery(q -> q.matchAll(m -> m))
                    .withPageable(pageable)
                    .build();
        } else {
            // Use multi-match when there's a search term
            searchQuery = NativeQuery.builder()
                    .withQuery(q -> q
                            .multiMatch(m -> m
                                    .query(query)
                                    .fields("userFname", "userLname", "userEmail")
                            )
                    )
                    .withPageable(pageable)
                    .build();
        }

        // Execute search
        SearchHits<UserMemberDocument> hits = elasticsearchOperations.search(
                searchQuery, 
                UserMemberDocument.class
        );

        // Convert results
        List<CoreUserDTO> results = hits.stream()
                .map(SearchHit::getContent)
                .map(UserMemberMapper::toDTO)
                .toList();

        return new PageImpl<>(results, pageable, hits.getTotalHits());
    }    
    
    
    // Update CoreUser using CoreUserDTO
    @Override
    public CoreUserDTO update(Long userId, CoreUserDTO coreUserDTO) {
        CoreUser existingUser = coreUserRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CoreUser", userId));

        // Manually map fields from CoreUserDTO to CoreUser
        //coreUserMapper.updateCoreUserFromDto(coreUserDTO, existingUser); // If the DTO shares fields with entity
        existingUser.setTModified(LocalDateTime.now()); // Update modified timestamp

        CoreUser updatedUser = coreUserRepository.save(existingUser); // Save the updated user
        return coreUserMapper.toDTO(updatedUser); // Return updated user as DTO
    }

 // Update CoreUser using CoreUserUpdateDTO
    public CoreUserDTO updateUser(Long userId, CoreUserUpdateDTO updateUserDetailsDTO) {
        // Fetch the existing user from the repository
        CoreUser existingUser = coreUserRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CoreUser", userId));

        // If the designation ID is provided, fetch the designation and set it
        if (updateUserDetailsDTO.getUserDesig() != null) {
            MisDesignation designation = misDesignationRepository.findById(updateUserDetailsDTO.getUserDesig())
                    .orElseThrow(() -> new ResourceNotFoundException("MisDesignation", updateUserDetailsDTO.getUserDesig()));
            existingUser.setDesignation(designation);
        }

        // Map other fields from CoreUserUpdateDTO to CoreUser
        coreUserMapper.updateCoreUserFromDto(updateUserDetailsDTO, existingUser);

        // Update the modified timestamp
        existingUser.setTModified(LocalDateTime.now());

        // Save the updated user entity
        CoreUser updatedUser = coreUserRepository.save(existingUser);

        // Return the updated user as a DTO
        return coreUserMapper.toDTO(updatedUser);
    }
    
    
    public CoreUserDTO updateMember(Long userId, CoreUserUpdateMemberDTO updateMemberDTO) {
        // Fetch the existing user from the repository
        CoreUser existingUser = coreUserRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CoreUser", userId));

        // If the designation ID is provided, fetch the designation and set it
        if (updateMemberDTO.getUserDesig() != null) {
            MisDesignation designation = misDesignationRepository.findById(updateMemberDTO.getUserDesig())
                    .orElseThrow(() -> new ResourceNotFoundException("MisDesignation", updateMemberDTO.getUserDesig()));
            existingUser.setDesignation(designation);
        }

        // Map other fields from CoreUserUpdateMemberDTO to CoreUser
        coreUserMapper.updateCoreUserFromMemberDto(updateMemberDTO, existingUser);

        // Update the modified timestamp
        existingUser.setTModified(LocalDateTime.now());

        // Save the updated user entity
        CoreUser updatedUser = coreUserRepository.save(existingUser);

        // Return the updated user as a DTO
        return coreUserMapper.toDTO(updatedUser);
    }



    public CoreUserDTO resetPassword(Long userId, CoreUserPasswordDTO passwordDTO) {
        CoreUser existingUser = coreUserRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CoreUser", userId));

        // Check if new password and confirm password match
        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        // Set the new password and update modification timestamp
        existingUser.setUserPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        existingUser.setTModified(LocalDateTime.now());

        CoreUser updatedUser = coreUserRepository.save(existingUser); // Save the updated user
        return coreUserMapper.toDTO(updatedUser); // Return updated user as DTO
    }

    public boolean toggleUserStatus(Long userId, Short userStatus) {
        CoreUser user = coreUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CoreUser", userId));
        user.setUserStatus(userStatus); // Update the user status
        coreUserRepository.save(user); // Save the status change
        return user.getUserStatus() == 1; // Return true if active, otherwise false
    }
    
    
    @Override
    public List<CoreUserDTO> findAll() {
        // Find all users that are not deleted and convert to DTOs
        return coreUserRepository.findAllNotDeleted().stream()
                .map(coreUserMapper::toDTO)
                .collect(Collectors.toList());
    }
    

    public Page<CoreUserDTO> findAllPaginate(Pageable pageable, String search) {
        // Find paginated and filtered users, then map to DTOs
        return coreUserRepository.findAllNotDeleted(search == null ? "" : search, pageable)
                                 .map(coreUserMapper::toDTO);
    }
    
    public Page<CoreUserDTO> searchUsers(String query, Pageable pageable) {
        return coreUserRepository.findAllNotDeleted(query, pageable)
                                 .map(coreUserMapper::toDTO);
    }

    
    
    public CoreUserDTO findById(Long userId) {
        // Find the user by ID and return as DTO
        return coreUserRepository.findByIdAndNotDeleted(userId)
                .map(coreUserMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("CoreUser", userId));
    }

    @Override
    public CoreUserDTO save(CoreUserDTO coreUserDTO) {
        // Convert DTO to entity and save
        CoreUser coreUser = coreUserMapper.toEntity(coreUserDTO);
        CoreUser savedCoreUser = coreUserRepository.save(coreUser);
        return coreUserMapper.toDTO(savedCoreUser); // Return saved user as DTO
    }
    
    public CoreUserDTO saveMamber(UserMemberDTO userMemberDTO) {
        // Convert DTO to entity and save
        CoreUser coreUser = coreUserMapper.toEntity(userMemberDTO);
        CoreUser savedCoreUser = coreUserRepository.save(coreUser);
        
        try {
            UserMemberDocument document = UserMemberMapper.toDocument(savedCoreUser);
            userMemberElasticsearchRepository.save(document);
        } catch (Exception e) {
            // Optional: log or handle ES sync failure
            log.error("Failed to sync user to Elasticsearch. userId={}", savedCoreUser.getUserId(), e);
            
        }
        
        
        return coreUserMapper.toDTO(savedCoreUser); // Return saved user as DTO
    }

    @Override
    public void softDeleteById(Long userId) {
        CoreUser user = coreUserRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CoreUser", userId));
        
        // Perform a soft delete by setting the deleted flag and timestamp
        user.setDeleted(true);
        user.setTDeleted(LocalDateTime.now());
        coreUserRepository.save(user); // Save the soft-deleted user
    }
    
    public List<CoreUser> listUsersByDesignation(Long desigId) {
        Optional<MisDesignation> designation = misDesignationRepository.findById(desigId);
        return designation.map(coreUserRepository::findByDesignation).orElse(Collections.emptyList());
    }
    
    public CoreUserDTO getUserByEmailAddress(String email) {
        CoreUser user = coreUserRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return coreUserMapper.toDTO(user);
    }
    
    public List<CoreUser> listUsersByNode(Long nodeId) {
        return coreUserRepository.findByUserNodeIdAndNotDeleted(nodeId);
    }
    
    public List<CoreUser> listUsersByNodeAndType(Long nodeId, UserType userType) {
        return coreUserRepository.findByUserNodeIdAndTypeAndNotDeleted(nodeId, userType);
    }
}

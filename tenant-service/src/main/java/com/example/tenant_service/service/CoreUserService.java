package com.example.tenant_service.service;

import com.example.tenant_service.exception.ResourceNotFoundException;
import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.dto.users.CoreUserDTO;
import com.example.tenant_service.dto.users.CoreUserPasswordDTO;
import com.example.tenant_service.dto.users.CoreUserUpdateDTO;
import com.example.tenant_service.entity.CoreUser;
import com.example.tenant_service.entity.MisDesignation;
import com.example.tenant_service.mapper.CoreUserMapper;
import com.example.tenant_service.repository.CoreUserRepository;
import com.example.tenant_service.repository.MisDesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoreUserService implements BaseService<CoreUserDTO> {

    private final CoreUserRepository coreUserRepository;
    private final CoreUserMapper coreUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final MisDesignationRepository misDesignationRepository;

    @Autowired
    public CoreUserService(CoreUserRepository coreUserRepository, CoreUserMapper coreUserMapper, PasswordEncoder passwordEncoder, MisDesignationRepository misDesignationRepository) {
        this.coreUserRepository = coreUserRepository;
        this.coreUserMapper = coreUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.misDesignationRepository = misDesignationRepository;
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
}

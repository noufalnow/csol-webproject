package com.example.tenant_service.service;

import com.example.tenant_service.exception.ResourceNotFoundException;
import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.dto.users.CoreUserDTO;
import com.example.tenant_service.dto.users.CoreUserPasswordDTO;
import com.example.tenant_service.dto.users.CoreUserUpdateDTO;
import com.example.tenant_service.dto.users.CoreUserToggleDTO;
import com.example.tenant_service.entity.CoreUser;
import com.example.tenant_service.mapper.CoreUserMapper;
import com.example.tenant_service.repository.CoreUserRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoreUserService implements BaseService<CoreUserDTO> {

    private final CoreUserRepository coreUserRepository;
    private final CoreUserMapper coreUserMapper;

    @Autowired
    public CoreUserService(CoreUserRepository coreUserRepository, CoreUserMapper coreUserMapper) {
        this.coreUserRepository = coreUserRepository;
        this.coreUserMapper = coreUserMapper;
    }

    @Override
    public CoreUserDTO update(Long userId, CoreUserDTO coreUserDTO) {
        Optional<CoreUser> existingUserOptional = coreUserRepository.findByIdAndNotDeleted(userId);
        if (existingUserOptional.isPresent()) {
            CoreUser existingUser = existingUserOptional.get();

            // Update only the fields that are provided in the DTO
            if (coreUserDTO.getUserFname() != null) {
                existingUser.setUserFname(coreUserDTO.getUserFname());
            }
            if (coreUserDTO.getUserLname() != null) {
                existingUser.setUserLname(coreUserDTO.getUserLname());
            }
            if (coreUserDTO.getUserUname() != null) {
                existingUser.setUserUname(coreUserDTO.getUserUname());
            }
            if (coreUserDTO.getUserPassword() != null) {
                existingUser.setUserPassword(coreUserDTO.getUserPassword());
            }
            if (coreUserDTO.getUserDesig() != null) {
                existingUser.setUserDesig(coreUserDTO.getUserDesig());
            }
            if (coreUserDTO.getUserDept() != null) {
                existingUser.setUserDept(coreUserDTO.getUserDept());
            }
            if (coreUserDTO.getUserEmpId() != null) {
                existingUser.setUserEmpId(coreUserDTO.getUserEmpId());
            }
            if (coreUserDTO.getUserEmail() != null) {
                existingUser.setUserEmail(coreUserDTO.getUserEmail());
            }

            // Update the modified timestamp
            existingUser.setTModified(LocalDateTime.now());

            // Save the updated entity
            CoreUser updatedUser = coreUserRepository.save(existingUser);
            return coreUserMapper.toDTO(updatedUser);
        } else {
            throw new ResourceNotFoundException("CoreUser", userId);
        }
    }
    
    public CoreUserDTO updateUser(Long userId, CoreUserUpdateDTO updateUserDetailsDTO) {
        CoreUser existingUser = coreUserRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CoreUser", userId));

        // Update only the fields provided in the DTO
        if (updateUserDetailsDTO.getUserFname() != null) existingUser.setUserFname(updateUserDetailsDTO.getUserFname());
        if (updateUserDetailsDTO.getUserLname() != null) existingUser.setUserLname(updateUserDetailsDTO.getUserLname());
        if (updateUserDetailsDTO.getUserUname() != null) existingUser.setUserUname(updateUserDetailsDTO.getUserUname());
        if (updateUserDetailsDTO.getUserDesig() != null) existingUser.setUserDesig(updateUserDetailsDTO.getUserDesig());
        if (updateUserDetailsDTO.getUserDept() != null) existingUser.setUserDept(updateUserDetailsDTO.getUserDept());
        if (updateUserDetailsDTO.getUserEmpId() != null) existingUser.setUserEmpId(updateUserDetailsDTO.getUserEmpId());
        if (updateUserDetailsDTO.getUserEmail() != null) existingUser.setUserEmail(updateUserDetailsDTO.getUserEmail());

        // Update the modified timestamp
        existingUser.setTModified(LocalDateTime.now());

        // Save and return updated entity
        CoreUser updatedUser = coreUserRepository.save(existingUser);
        return coreUserMapper.toDTO(updatedUser);
    }
    
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public CoreUserDTO resetPassword(Long userId, CoreUserPasswordDTO passwordDTO) {
        CoreUser existingUser = coreUserRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CoreUser", userId));

        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }

        existingUser.setUserPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));

        existingUser.setTModified(LocalDateTime.now());
        CoreUser updatedUser = coreUserRepository.save(existingUser);

        return coreUserMapper.toDTO(updatedUser);
    }




    // Method to change user password
    public void changePassword(Long userId, CoreUserPasswordDTO changePasswordDTO) {
        CoreUser existingUser = coreUserRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CoreUser", userId));

        // Validate and update password
        if (changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            existingUser.setUserPassword(changePasswordDTO.getNewPassword());
            coreUserRepository.save(existingUser);
        } else {
            throw new IllegalArgumentException("Password and confirmation do not match.");
        }
    }

    
    
    

    @Override
    public List<CoreUserDTO> findAll() {
        return coreUserRepository.findAllNotDeleted().stream()
                .map(coreUserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CoreUserDTO findById(Long userId) {
        return coreUserRepository.findByIdAndNotDeleted(userId)
                .map(coreUserMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("CoreUser", userId));
    }

    @Override
    public CoreUserDTO save(CoreUserDTO coreUserDTO) {
        CoreUser coreUser = coreUserMapper.toEntity(coreUserDTO);
        CoreUser savedCoreUser = coreUserRepository.save(coreUser);
        return coreUserMapper.toDTO(savedCoreUser);
    }

    @Override
    public void softDeleteById(Long userId) {
        Optional<CoreUser> userOptional = coreUserRepository.findByIdAndNotDeleted(userId);
        if (userOptional.isPresent()) {
            CoreUser user = userOptional.get();
            user.setDeleted(true); // Set 'deleted' flag to true
            user.setTDeleted(LocalDateTime.now());
            coreUserRepository.save(user);
        } else {
            throw new ResourceNotFoundException("CoreUser", userId);
        }
    }

    // Toggle User Status (Enable/Disable)
    public boolean toggleUserStatus(Long userId, Short userStatus) {
        // Fetch user from the database using userId
        Optional<CoreUser> userOpt = coreUserRepository.findById(userId);
        if (!userOpt.isPresent()) {
            // Handle user not found scenario
            return false;
        }

        CoreUser user = userOpt.get();
        // Update the user's status based on userStatus
        user.setUserStatus(userStatus);

        // Save the updated user entity
        coreUserRepository.save(user);

        // Return true if the user is active, false if inactive
        return user.getUserStatus() == 1;  // Assuming 1 means active
    }







    // Reset User Password
    public void resetUserPassword(Long id) {
        Optional<CoreUser> userOptional = coreUserRepository.findByIdAndNotDeleted(id);
        if (userOptional.isPresent()) {
            CoreUser user = userOptional.get();
            String newPassword = generateRandomPassword(); // Implement this method to generate a new password
            user.setUserPassword(newPassword);
            coreUserRepository.save(user);
            // Optionally, send the new password to the user via email or another method
        } else {
            throw new ResourceNotFoundException("CoreUser", id);
        }
    }

    // Helper method to generate a random password
    private String generateRandomPassword() {
        // Implement your password generation logic here
        return "newRandomPassword123"; // Example placeholder
    }
}


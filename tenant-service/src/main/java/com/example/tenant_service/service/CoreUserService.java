package com.example.tenant_service.service;

import com.example.tenant_service.exception.ResourceNotFoundException;
import com.example.tenant_service.common.BaseService;
import com.example.tenant_service.dto.CoreUserDTO;
import com.example.tenant_service.entity.CoreUser;
import com.example.tenant_service.mapper.CoreUserMapper;
import com.example.tenant_service.repository.CoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
            user.setDeleted((Boolean) true);  // Assuming '1' means deleted
            user.setTDeleted(LocalDateTime.now());
            coreUserRepository.save(user);
        } else {
            throw new ResourceNotFoundException("CoreUser", userId);
        }
    }

}

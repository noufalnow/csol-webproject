package com.dms.kalari.admin.service;

import com.dms.kalari.admin.dto.MemberAddDTO;
import com.dms.kalari.admin.dto.MemberUpdateDTO;
import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.admin.entity.MisDesignation;
import com.dms.kalari.admin.entity.CoreUser.UserType;
import com.dms.kalari.admin.mapper.CoreUserMapper;
import com.dms.kalari.admin.repository.CoreUserRepository;
import com.dms.kalari.admin.repository.MisDesignationRepository;
import com.dms.kalari.common.BaseService;
import com.dms.kalari.core.entity.CoreFile;
import com.dms.kalari.core.repository.CoreFileRepository;
import com.dms.kalari.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
public class MemberUserService implements BaseService<MemberAddDTO> {

    private final CoreUserRepository coreUserRepository;
    private final CoreUserMapper coreUserMapper;
    private final MisDesignationRepository misDesignationRepository;
    private final CoreFileRepository coreFileRepository;

    @Autowired
    public MemberUserService(CoreUserRepository coreUserRepository,
                             CoreUserMapper coreUserMapper,
                             MisDesignationRepository misDesignationRepository,
                             CoreFileRepository coreFileRepository) {
        this.coreUserRepository = coreUserRepository;
        this.coreUserMapper = coreUserMapper;
        this.misDesignationRepository = misDesignationRepository;
        this.coreFileRepository = coreFileRepository;
    }

    /** BaseService.save — delegates to saveMamber */
    @Override
    public MemberAddDTO save(MemberAddDTO dto) {
        return saveMamber(dto);
    }

    /** BaseService.update — supports updating with MemberAddDTO (if used generically) */
    @Override
    public MemberAddDTO update(Long userId, MemberAddDTO dto) {
        CoreUser existing = coreUserRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CoreUser", userId));

        // allow updating shared fields via Add DTO if provided
        coreUserMapper.updateCoreUserFromMemberAddDto(dto, existing);
        existing.setTModified(LocalDateTime.now());
        CoreUser saved = coreUserRepository.save(existing);
        // keep existing photo if none provided in Add DTO (form-driven uploads handled elsewhere)
        return coreUserMapper.toMemberAddDTO(saved);
    }

    /** BaseService.findAll — returns all (not deleted) mapped to MemberAddDTO */
    @Override
    public List<MemberAddDTO> findAll() {
        return coreUserRepository.findAllNotDeleted().stream()
                .map(coreUserMapper::toMemberAddDTO)
                .collect(Collectors.toList());
    }

    /** BaseService.softDeleteById */
    @Override
    public void softDeleteById(Long userId) {
        CoreUser user = coreUserRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CoreUser", userId));
        user.setDeleted(true);
        user.setTDeleted(LocalDateTime.now());
        coreUserRepository.save(user);
    }

    /** Paginated list of users (kept behavior; controller is specific to members) */
    public Page<MemberAddDTO> findAllPaginate(Pageable pageable, String search) {
        return coreUserRepository.findAllNotDeleted(search == null ? "" : search, pageable)
                .map(coreUserMapper::toMemberAddDTO);
    }

    /** List by node & type (controller passes UserType.MEMBER) */
    public List<CoreUser> listUsersByNodeAndType(Long nodeId, UserType userType) {
        return coreUserRepository.findByUserNodeIdAndTypeAndNotDeleted(nodeId, userType);
    }

    /** Get details for view/profile pages */
    public MemberAddDTO findById(Long id) {
        return coreUserRepository.findByIdAndNotDeleted(id)
                .map(coreUserMapper::toMemberAddDTO)
                .orElseThrow(() -> new ResourceNotFoundException("CoreUser", id));
    }

    /** Add a new member */
    public MemberAddDTO saveMamber(MemberAddDTO dto) {
        CoreUser user = coreUserMapper.toEntity(dto);
        user.setUserType(UserType.MEMBER);

        CoreUser savedUser = coreUserRepository.save(user);

        // photo
        savedUser = handleFileUpload(dto.getPhotoFileId(), savedUser, CoreUser::setPhotoFile);

        // ID proof
        savedUser = handleFileUpload(dto.getIdFileId(), savedUser, CoreUser::setIdFile);

        // Age proof
        savedUser = handleFileUpload(dto.getAgeproofFileId(), savedUser, CoreUser::setAgeproofFile);

        return coreUserMapper.toMemberAddDTO(savedUser);
    }


    /** Update existing member using MemberUpdateDTO */
    public MemberAddDTO updateMember(Long userId, MemberUpdateDTO dto) {
        CoreUser existingUser = coreUserRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new ResourceNotFoundException("CoreUser", userId));

        if (dto.getUserDesig() != null) {
            MisDesignation designation = misDesignationRepository.findById(dto.getUserDesig())
                    .orElseThrow(() -> new ResourceNotFoundException("MisDesignation", dto.getUserDesig()));
            existingUser.setDesignation(designation);
        }

        coreUserMapper.updateCoreUserFromMemberUpdateDTO(dto, existingUser);
        existingUser.setTModified(LocalDateTime.now());

        CoreUser updatedUser = coreUserRepository.save(existingUser);
        //updatedUser = handleFileUpload(dto.getPhotoFileId(), updatedUser);
        
        updatedUser = handleFileUpload(dto.getPhotoFileId(), updatedUser, CoreUser::setPhotoFile);

        // ID proof
        updatedUser = handleFileUpload(dto.getIdFileId(), updatedUser, CoreUser::setIdFile);

        // Age proof
        updatedUser = handleFileUpload(dto.getAgeproofFileId(), updatedUser, CoreUser::setAgeproofFile);
        

        return coreUserMapper.toMemberAddDTO(updatedUser);
    }

    /** Common file upload handler */
    private CoreUser handleFileUpload(
            MultipartFile file, 
            CoreUser user, 
            BiConsumer<CoreUser, Long> fileSetter) {

        if (file == null || file.isEmpty()) return user;

        try {
            CoreFile meta = new CoreFile();
            meta.setFileSrc("users");
            meta.setFileRefId(user.getUserId());
            meta.setFileActualName(file.getOriginalFilename());
            meta.setFileExten(getExtension(file.getOriginalFilename()));
            meta.setFileSize(file.getSize());

            Path uploadPath = Paths.get("uploads/users/");
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            meta.setFilePath(filePath.toString());
            CoreFile savedMeta = coreFileRepository.save(meta);

            // Apply correct setter dynamically
            fileSetter.accept(user, savedMeta.getFileId());
            user = coreUserRepository.save(user);

        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }
        return user;
    }


    private String getExtension(String fileName) {
        return (fileName != null && fileName.contains(".")) ?
                fileName.substring(fileName.lastIndexOf(".") + 1) : null;
    }
}

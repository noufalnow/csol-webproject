package com.example.tenant_service.mapper;

import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.users.CoreUserDTO;
import com.example.tenant_service.dto.users.CoreUserUpdateDTO;
import com.example.tenant_service.entity.CoreUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CoreUserMapper extends BaseMapper<CoreUser, CoreUserDTO> {
    CoreUserMapper INSTANCE = Mappers.getMapper(CoreUserMapper.class);
    
    // Map CoreUser to CoreUserDTO, including designation details
    @Mapping(source = "designation.desigId", target = "userDesig") // Map designation ID
    @Mapping(source = "designation.desigName", target = "designationName") // Map designation name to DTO
    CoreUserDTO toDTO(CoreUser coreUser);

    // Map CoreUserDTO to CoreUser, including designation details
    @Mapping(source = "userDesig", target = "designation.desigId") // Map userDesig to designation ID
    CoreUser toEntity(CoreUserDTO coreUserDTO);

    // Update CoreUser fields from CoreUserUpdateDTO, ignoring null values
    @Mapping(source = "userDesig", target = "designation.desigId") // Map userDesig to designation ID
    void updateCoreUserFromDto(CoreUserUpdateDTO coreUserUpdateDTO, @MappingTarget CoreUser coreUser);
}


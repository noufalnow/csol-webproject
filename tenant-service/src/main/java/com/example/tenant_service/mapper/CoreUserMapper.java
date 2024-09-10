package com.example.tenant_service.mapper;
import com.example.tenant_service.common.BaseMapper;
import com.example.tenant_service.dto.users.CoreUserDTO;
import com.example.tenant_service.dto.users.CoreUserUpdateDTO;
import com.example.tenant_service.entity.CoreUser;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CoreUserMapper extends BaseMapper<CoreUser, CoreUserDTO> {
    CoreUserMapper INSTANCE = Mappers.getMapper(CoreUserMapper.class);

    // Update CoreUser fields from CoreUserUpdateDTO, ignoring null values
    void updateCoreUserFromDto(CoreUserUpdateDTO coreUserUpdateDTO, @MappingTarget CoreUser coreUser);
}


package com.dms.kalari.admin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.admin.dto.CoreUserDTO;
import com.dms.kalari.admin.dto.CoreUserUpdateDTO;
import com.dms.kalari.admin.dto.CoreUserUpdateMemberDTO;
import com.dms.kalari.admin.dto.CoreUserMemberDTO;
import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.common.BaseMapper;
import com.dms.kalari.entity.Node;

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
    
    
    default Node map(Long id) {
        if (id == null) {
            return null;
        }
        Node node = new Node();
        node.setNodeId(id);
        return node;
    }
    // Update CoreUser fields from CoreUserUpdateDTO, ignoring null values
    @Mapping(source = "userDesig", target = "designation.desigId") // Map userDesig to designation ID
    void updateCoreUserFromDto(CoreUserUpdateDTO coreUserUpdateDTO, @MappingTarget CoreUser coreUser);
    
    
    @Mapping(source = "userDesig", target = "designation.desigId")
    @Mapping(source = "userFname", target = "userFname")
    @Mapping(source = "userLname", target = "userLname")
    @Mapping(source = "userEmail", target = "userEmail")
    @Mapping(source = "userStatus", target = "userStatus")
    @Mapping(source = "userNode", target = "userNode")
    CoreUser toEntity(CoreUserMemberDTO CoreUserMemberDTO);
    
    
    @Mapping(source = "userDesig", target = "designation.desigId")
    void updateCoreUserFromMemberDto(CoreUserUpdateMemberDTO updateMemberDTO, @MappingTarget CoreUser coreUser);
    
    
    default Long map(Node value) {
        return value != null ? value.getNodeId() : null;
    }
    
}







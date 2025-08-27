package com.dms.kalari.admin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.dms.kalari.admin.dto.CoreUserDTO;
import com.dms.kalari.admin.dto.CoreUserUpdateDTO;
import com.dms.kalari.admin.dto.OfficialUpdateDTO;
import com.dms.kalari.admin.dto.OfficialAddDTO;
import com.dms.kalari.admin.dto.MemberAddDTO;
import com.dms.kalari.admin.dto.MemberUpdateDTO;
import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.common.BaseMapper;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CoreUserMapper extends BaseMapper<CoreUser, CoreUserDTO> {
    CoreUserMapper INSTANCE = Mappers.getMapper(CoreUserMapper.class);

    // ====== Existing mappings retained (no functionality lost) ======

    @Mapping(source = "designation.desigId", target = "userDesig")
    @Mapping(source = "designation.desigName", target = "designationName")
    CoreUserDTO toDTO(CoreUser coreUser);

    @Mapping(source = "userDesig", target = "designation.desigId")
    CoreUser toEntity(CoreUserDTO coreUserDTO);

    default Node map(Long id) {
        if (id == null) return null;
        Node node = new Node();
        node.setNodeId(id);
        return node;
    }

    @Mapping(source = "userDesig", target = "designation.desigId")
    void updateCoreUserFromDto(CoreUserUpdateDTO coreUserUpdateDTO, @MappingTarget CoreUser coreUser);

    @Mapping(source = "userDesig", target = "designation.desigId")
    @Mapping(source = "userFname", target = "userFname")
    @Mapping(source = "userLname", target = "userLname")
    @Mapping(source = "userEmail", target = "userEmail")
    @Mapping(source = "userStatus", target = "userStatus")
    @Mapping(source = "userNode", target = "userNode")
    @Mapping(target = "photoFile", ignore = true)
    CoreUser toEntity(OfficialAddDTO CoreUserMemberDTO);

    @Mapping(source = "userDesig", target = "designation.desigId")
    void updateCoreUserFromMemberDto(OfficialUpdateDTO updateMemberDTO, @MappingTarget CoreUser coreUser);

    default Long map(Node value) {
        return value != null ? value.getNodeId() : null;
    }

    @Mapping(target = "userDesig", source = "designation.desigId")
    @Mapping(target = "designationName", source = "designation.desigName")
    OfficialUpdateDTO toUpdateMemberDTO(CoreUser coreUser);

    void updateCoreUserFromMemberDto(@MappingTarget CoreUser coreUser, OfficialUpdateDTO dto);

    // ====== New mappings for MemberAddDTO / MemberUpdateDTO ======

    // For views/lists where controller expects MemberAddDTO
    @Mapping(source = "designation.desigId", target = "userDesig")
    @Mapping(source = "designation.desigName", target = "designationName")
    MemberAddDTO toMemberAddDTO(CoreUser coreUser);

    // For creating a member from MemberAddDTO
    @Mapping(source = "userDesig", target = "designation.desigId")
    @Mapping(source = "userFname", target = "userFname")
    @Mapping(source = "userLname", target = "userLname")
    @Mapping(source = "userEmail", target = "userEmail")
    @Mapping(source = "userStatus", target = "userStatus")
    @Mapping(source = "userNode", target = "userNode")
    @Mapping(target = "photoFile", ignore = true)
    CoreUser toEntity(MemberAddDTO dto);

    // For updating a member from MemberUpdateDTO
    @Mapping(source = "userDesig", target = "designation.desigId")
    void updateCoreUserFromMemberUpdateDTO(MemberUpdateDTO dto, @MappingTarget CoreUser coreUser);

    // For pre-filling edit form
    @Mapping(target = "userDesig", source = "designation.desigId")
    @Mapping(target = "designationName", source = "designation.desigName")
    MemberUpdateDTO toMemberUpdateDTO(CoreUser coreUser);

    // Optional: allow BaseService.update(...) with MemberAddDTO to update common fields
    @Mapping(source = "userDesig", target = "designation.desigId")
    void updateCoreUserFromMemberAddDto(MemberAddDTO dto, @MappingTarget CoreUser coreUser);
}

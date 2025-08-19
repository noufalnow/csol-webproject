package com.example.tenant_service.mapper;

import com.example.tenant_service.document.UserMemberDocument;
import com.example.tenant_service.dto.users.CoreUserDTO;
import com.example.tenant_service.dto.users.UserMemberDTO;
import com.example.tenant_service.entity.CoreUser;
import com.example.tenant_service.entity.Node;
import com.example.tenant_service.entity.MisDesignation;

public class UserMemberMapper {

    public static CoreUser toEntity(UserMemberDTO dto) {
        CoreUser u = new CoreUser();
        u.setUserId(dto.getUserId());
        u.setUserFname(dto.getUserFname());
        u.setUserLname(dto.getUserLname());
        u.setUserStatus(dto.getUserStatus());
        u.setUserUname(dto.getUserUname());
        u.setUserEmail(dto.getUserEmail());
        u.setUserType(dto.getUserType());
        u.setUserPassword(dto.getUserPassword());

        // Set Node (only if needed, example placeholder)
        if (dto.getUserNode() != null) {
            Node node = new Node();
            node.setNodeId(dto.getUserNode());
            u.setUserNode(node);
        }

        // Set Designation (only ID known, so map ID into a MisDesignation object)
        if (dto.getUserDesig() != null) {
            MisDesignation desig = new MisDesignation();
            desig.setDesigId(dto.getUserDesig());
            u.setDesignation(desig);
        }

        return u;
    }

    public static UserMemberDTO toDTO(CoreUser entity) {
        UserMemberDTO dto = new UserMemberDTO();
        dto.setUserId(entity.getUserId());
        dto.setUserFname(entity.getUserFname());
        dto.setUserLname(entity.getUserLname());
        dto.setUserStatus(entity.getUserStatus());
        dto.setUserUname(entity.getUserUname());
        dto.setUserEmail(entity.getUserEmail());
        dto.setUserType(entity.getUserType());

        if (entity.getUserNode() != null) {
            dto.setUserNode(entity.getUserNode().getNodeId());
        }

        if (entity.getDesignation() != null) {
            dto.setUserDesig(entity.getDesignation().getDesigId());
            dto.setDesignationName(entity.getDesignation().getDesigName());
        }

        return dto;
    }

    public static UserMemberDocument toDocument(CoreUser entity) {
        UserMemberDocument doc = new UserMemberDocument();
        doc.setUserId(entity.getUserId());
        doc.setUserFname(entity.getUserFname());
        doc.setUserLname(entity.getUserLname());
        doc.setUserStatus(entity.getUserStatus());
        doc.setUserUname(entity.getUserUname());
        doc.setUserEmail(entity.getUserEmail());
        doc.setUserType(entity.getUserType().name());

        if (entity.getUserNode() != null) {
            doc.setUserNode(entity.getUserNode().getNodeId());
        }

        if (entity.getDesignation() != null) {
            doc.setUserDesig(entity.getDesignation().getDesigId());
            doc.setDesignationName(entity.getDesignation().getDesigName());
        }

        return doc;
    }

    // Optional: if you're saving directly from DTO to Elasticsearch
    public static UserMemberDocument toDocument(UserMemberDTO dto) {
        UserMemberDocument doc = new UserMemberDocument();
        doc.setUserId(dto.getUserId());
        doc.setUserFname(dto.getUserFname());
        doc.setUserLname(dto.getUserLname());
        doc.setUserStatus(dto.getUserStatus());
        doc.setUserDesig(dto.getUserDesig());
        doc.setDesignationName(dto.getDesignationName());
        doc.setUserNode(dto.getUserNode());
        doc.setUserUname(dto.getUserUname());
        doc.setUserEmail(dto.getUserEmail());
        doc.setUserType(dto.getUserType().name());
        return doc;
    }
    
    
    public static CoreUserDTO toDTO(UserMemberDocument document) {
        if (document == null) return null;
        
        CoreUserDTO dto = new CoreUserDTO();
        dto.setUserId(document.getUserId());
        dto.setUserFname(document.getUserFname());
        dto.setUserLname(document.getUserLname());
        dto.setUserEmail(document.getUserEmail());
        dto.setUserStatus(document.getUserStatus());
        dto.setUserUname(document.getUserUname());
        
        // Map user type if needed (assuming enum conversion)
        if (document.getUserType() != null) {
            dto.setUserType(CoreUser.UserType.valueOf(document.getUserType()));
        }
        
        // Map node and designation if they exist in CoreUserDTO
        dto.setUserNode(document.getUserNode());
        dto.setUserDesig(document.getUserDesig());
        dto.setDesignationName(document.getDesignationName());
        
        return dto;
    }
    
    public static CoreUserDTO toCoreUserDTO(UserMemberDocument document) {
        if (document == null) return null;
        
        CoreUserDTO dto = new CoreUserDTO();
        dto.setUserId(document.getUserId());
        dto.setUserFname(document.getUserFname());
        dto.setUserLname(document.getUserLname());
        dto.setUserEmail(document.getUserEmail());
        dto.setUserStatus(document.getUserStatus());
        dto.setUserUname(document.getUserUname());
        
        if (document.getUserType() != null) {
            dto.setUserType(CoreUser.UserType.valueOf(document.getUserType()));
        }
        
        dto.setUserNode(document.getUserNode());
        dto.setUserDesig(document.getUserDesig());
        dto.setDesignationName(document.getDesignationName());
        
        return dto;
    }
}

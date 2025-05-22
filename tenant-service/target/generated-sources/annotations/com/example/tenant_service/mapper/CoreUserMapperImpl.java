package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.DesignationDTO;
import com.example.tenant_service.dto.users.CoreUserDTO;
import com.example.tenant_service.dto.users.CoreUserUpdateDTO;
import com.example.tenant_service.entity.CoreUser;
import com.example.tenant_service.entity.MisDesignation;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T20:55:15+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.13 (Ubuntu)"
)
@Component
public class CoreUserMapperImpl implements CoreUserMapper {

    @Override
    public CoreUserDTO toDTO(CoreUser coreUser) {
        if ( coreUser == null ) {
            return null;
        }

        CoreUserDTO coreUserDTO = new CoreUserDTO();

        coreUserDTO.setUserDesig( coreUserDesignationDesigId( coreUser ) );
        coreUserDTO.setDesignationName( coreUserDesignationDesigName( coreUser ) );
        coreUserDTO.setUCreated( coreUser.getUCreated() );
        coreUserDTO.setTCreated( coreUser.getTCreated() );
        coreUserDTO.setTModified( coreUser.getTModified() );
        coreUserDTO.setUModified( coreUser.getUModified() );
        coreUserDTO.setUDeleted( coreUser.getUDeleted() );
        coreUserDTO.setTDeleted( coreUser.getTDeleted() );
        coreUserDTO.setDeleted( map( coreUser.getDeleted() ) );
        coreUserDTO.setActive( coreUser.getActive() );
        coreUserDTO.setUserId( coreUser.getUserId() );
        coreUserDTO.setUserFname( coreUser.getUserFname() );
        coreUserDTO.setUserLname( coreUser.getUserLname() );
        coreUserDTO.setUserUname( coreUser.getUserUname() );
        coreUserDTO.setUserStatus( coreUser.getUserStatus() );
        coreUserDTO.setUserPassword( coreUser.getUserPassword() );
        coreUserDTO.setDesignation( misDesignationToDesignationDTO( coreUser.getDesignation() ) );
        coreUserDTO.setUserDept( coreUser.getUserDept() );
        coreUserDTO.setUserEmpId( coreUser.getUserEmpId() );
        coreUserDTO.setUserEmail( coreUser.getUserEmail() );

        return coreUserDTO;
    }

    @Override
    public CoreUser toEntity(CoreUserDTO coreUserDTO) {
        if ( coreUserDTO == null ) {
            return null;
        }

        CoreUser coreUser = new CoreUser();

        coreUser.setDesignation( coreUserDTOToMisDesignation( coreUserDTO ) );
        coreUser.setUCreated( coreUserDTO.getUCreated() );
        coreUser.setTCreated( coreUserDTO.getTCreated() );
        coreUser.setTModified( coreUserDTO.getTModified() );
        coreUser.setUModified( coreUserDTO.getUModified() );
        coreUser.setUDeleted( coreUserDTO.getUDeleted() );
        coreUser.setTDeleted( coreUserDTO.getTDeleted() );
        coreUser.setDeleted( map( coreUserDTO.getDeleted() ) );
        coreUser.setActive( coreUserDTO.getActive() );
        coreUser.setUserId( coreUserDTO.getUserId() );
        coreUser.setUserFname( coreUserDTO.getUserFname() );
        coreUser.setUserLname( coreUserDTO.getUserLname() );
        coreUser.setUserUname( coreUserDTO.getUserUname() );
        coreUser.setUserStatus( coreUserDTO.getUserStatus() );
        coreUser.setUserPassword( coreUserDTO.getUserPassword() );
        coreUser.setUserDept( coreUserDTO.getUserDept() );
        coreUser.setUserEmpId( coreUserDTO.getUserEmpId() );
        coreUser.setUserEmail( coreUserDTO.getUserEmail() );

        return coreUser;
    }

    @Override
    public void updateCoreUserFromDto(CoreUserUpdateDTO coreUserUpdateDTO, CoreUser coreUser) {
        if ( coreUserUpdateDTO == null ) {
            return;
        }

        if ( coreUser.getDesignation() == null ) {
            coreUser.setDesignation( new MisDesignation() );
        }
        coreUserUpdateDTOToMisDesignation( coreUserUpdateDTO, coreUser.getDesignation() );
        if ( coreUserUpdateDTO.getUCreated() != null ) {
            coreUser.setUCreated( coreUserUpdateDTO.getUCreated() );
        }
        if ( coreUserUpdateDTO.getTCreated() != null ) {
            coreUser.setTCreated( coreUserUpdateDTO.getTCreated() );
        }
        if ( coreUserUpdateDTO.getTModified() != null ) {
            coreUser.setTModified( coreUserUpdateDTO.getTModified() );
        }
        if ( coreUserUpdateDTO.getUModified() != null ) {
            coreUser.setUModified( coreUserUpdateDTO.getUModified() );
        }
        if ( coreUserUpdateDTO.getUDeleted() != null ) {
            coreUser.setUDeleted( coreUserUpdateDTO.getUDeleted() );
        }
        if ( coreUserUpdateDTO.getTDeleted() != null ) {
            coreUser.setTDeleted( coreUserUpdateDTO.getTDeleted() );
        }
        if ( coreUserUpdateDTO.getDeleted() != null ) {
            coreUser.setDeleted( map( coreUserUpdateDTO.getDeleted() ) );
        }
        if ( coreUserUpdateDTO.getActive() != null ) {
            coreUser.setActive( coreUserUpdateDTO.getActive() );
        }
        if ( coreUserUpdateDTO.getUserId() != null ) {
            coreUser.setUserId( coreUserUpdateDTO.getUserId() );
        }
        if ( coreUserUpdateDTO.getUserFname() != null ) {
            coreUser.setUserFname( coreUserUpdateDTO.getUserFname() );
        }
        if ( coreUserUpdateDTO.getUserLname() != null ) {
            coreUser.setUserLname( coreUserUpdateDTO.getUserLname() );
        }
        if ( coreUserUpdateDTO.getUserUname() != null ) {
            coreUser.setUserUname( coreUserUpdateDTO.getUserUname() );
        }
        if ( coreUserUpdateDTO.getUserStatus() != null ) {
            coreUser.setUserStatus( coreUserUpdateDTO.getUserStatus() );
        }
        if ( coreUserUpdateDTO.getUserDept() != null ) {
            coreUser.setUserDept( coreUserUpdateDTO.getUserDept() );
        }
        if ( coreUserUpdateDTO.getUserEmpId() != null ) {
            coreUser.setUserEmpId( coreUserUpdateDTO.getUserEmpId() );
        }
        if ( coreUserUpdateDTO.getUserEmail() != null ) {
            coreUser.setUserEmail( coreUserUpdateDTO.getUserEmail() );
        }
    }

    private Long coreUserDesignationDesigId(CoreUser coreUser) {
        if ( coreUser == null ) {
            return null;
        }
        MisDesignation designation = coreUser.getDesignation();
        if ( designation == null ) {
            return null;
        }
        Long desigId = designation.getDesigId();
        if ( desigId == null ) {
            return null;
        }
        return desigId;
    }

    private String coreUserDesignationDesigName(CoreUser coreUser) {
        if ( coreUser == null ) {
            return null;
        }
        MisDesignation designation = coreUser.getDesignation();
        if ( designation == null ) {
            return null;
        }
        String desigName = designation.getDesigName();
        if ( desigName == null ) {
            return null;
        }
        return desigName;
    }

    protected DesignationDTO misDesignationToDesignationDTO(MisDesignation misDesignation) {
        if ( misDesignation == null ) {
            return null;
        }

        DesignationDTO designationDTO = new DesignationDTO();

        designationDTO.setUCreated( misDesignation.getUCreated() );
        designationDTO.setTCreated( misDesignation.getTCreated() );
        designationDTO.setTModified( misDesignation.getTModified() );
        designationDTO.setUModified( misDesignation.getUModified() );
        designationDTO.setUDeleted( misDesignation.getUDeleted() );
        designationDTO.setTDeleted( misDesignation.getTDeleted() );
        designationDTO.setDeleted( map( misDesignation.getDeleted() ) );
        designationDTO.setActive( misDesignation.getActive() );
        designationDTO.setDesigId( misDesignation.getDesigId() );
        designationDTO.setDesigCode( misDesignation.getDesigCode() );
        designationDTO.setDesigName( misDesignation.getDesigName() );
        designationDTO.setDesigLevel( misDesignation.getDesigLevel() );
        designationDTO.setDesigType( misDesignation.getDesigType() );

        return designationDTO;
    }

    protected MisDesignation coreUserDTOToMisDesignation(CoreUserDTO coreUserDTO) {
        if ( coreUserDTO == null ) {
            return null;
        }

        MisDesignation misDesignation = new MisDesignation();

        misDesignation.setDesigId( coreUserDTO.getUserDesig() );

        return misDesignation;
    }

    protected void coreUserUpdateDTOToMisDesignation(CoreUserUpdateDTO coreUserUpdateDTO, MisDesignation mappingTarget) {
        if ( coreUserUpdateDTO == null ) {
            return;
        }

        if ( coreUserUpdateDTO.getUserDesig() != null ) {
            mappingTarget.setDesigId( coreUserUpdateDTO.getUserDesig() );
        }
    }
}

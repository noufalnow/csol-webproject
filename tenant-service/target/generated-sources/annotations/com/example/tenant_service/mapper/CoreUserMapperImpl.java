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
    date = "2024-09-23T11:36:48+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240215-1558, environment: Java 17.0.12 (Eclipse Adoptium)"
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
        coreUserDTO.setActive( coreUser.getActive() );
        coreUserDTO.setDeleted( map( coreUser.getDeleted() ) );
        coreUserDTO.setTCreated( coreUser.getTCreated() );
        coreUserDTO.setTDeleted( coreUser.getTDeleted() );
        coreUserDTO.setTModified( coreUser.getTModified() );
        coreUserDTO.setUCreated( coreUser.getUCreated() );
        coreUserDTO.setUDeleted( coreUser.getUDeleted() );
        coreUserDTO.setUModified( coreUser.getUModified() );
        coreUserDTO.setDesignation( misDesignationToDesignationDTO( coreUser.getDesignation() ) );
        coreUserDTO.setUserDept( coreUser.getUserDept() );
        coreUserDTO.setUserEmail( coreUser.getUserEmail() );
        coreUserDTO.setUserEmpId( coreUser.getUserEmpId() );
        coreUserDTO.setUserFname( coreUser.getUserFname() );
        coreUserDTO.setUserId( coreUser.getUserId() );
        coreUserDTO.setUserLname( coreUser.getUserLname() );
        coreUserDTO.setUserPassword( coreUser.getUserPassword() );
        coreUserDTO.setUserStatus( coreUser.getUserStatus() );
        coreUserDTO.setUserUname( coreUser.getUserUname() );

        return coreUserDTO;
    }

    @Override
    public CoreUser toEntity(CoreUserDTO coreUserDTO) {
        if ( coreUserDTO == null ) {
            return null;
        }

        CoreUser coreUser = new CoreUser();

        coreUser.setDesignation( coreUserDTOToMisDesignation( coreUserDTO ) );
        coreUser.setActive( coreUserDTO.getActive() );
        coreUser.setDeleted( map( coreUserDTO.getDeleted() ) );
        coreUser.setTCreated( coreUserDTO.getTCreated() );
        coreUser.setTDeleted( coreUserDTO.getTDeleted() );
        coreUser.setTModified( coreUserDTO.getTModified() );
        coreUser.setUCreated( coreUserDTO.getUCreated() );
        coreUser.setUDeleted( coreUserDTO.getUDeleted() );
        coreUser.setUModified( coreUserDTO.getUModified() );
        coreUser.setUserDept( coreUserDTO.getUserDept() );
        coreUser.setUserEmail( coreUserDTO.getUserEmail() );
        coreUser.setUserEmpId( coreUserDTO.getUserEmpId() );
        coreUser.setUserFname( coreUserDTO.getUserFname() );
        coreUser.setUserId( coreUserDTO.getUserId() );
        coreUser.setUserLname( coreUserDTO.getUserLname() );
        coreUser.setUserPassword( coreUserDTO.getUserPassword() );
        coreUser.setUserStatus( coreUserDTO.getUserStatus() );
        coreUser.setUserUname( coreUserDTO.getUserUname() );

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
        if ( coreUserUpdateDTO.getActive() != null ) {
            coreUser.setActive( coreUserUpdateDTO.getActive() );
        }
        if ( coreUserUpdateDTO.getDeleted() != null ) {
            coreUser.setDeleted( map( coreUserUpdateDTO.getDeleted() ) );
        }
        if ( coreUserUpdateDTO.getTCreated() != null ) {
            coreUser.setTCreated( coreUserUpdateDTO.getTCreated() );
        }
        if ( coreUserUpdateDTO.getTDeleted() != null ) {
            coreUser.setTDeleted( coreUserUpdateDTO.getTDeleted() );
        }
        if ( coreUserUpdateDTO.getTModified() != null ) {
            coreUser.setTModified( coreUserUpdateDTO.getTModified() );
        }
        if ( coreUserUpdateDTO.getUCreated() != null ) {
            coreUser.setUCreated( coreUserUpdateDTO.getUCreated() );
        }
        if ( coreUserUpdateDTO.getUDeleted() != null ) {
            coreUser.setUDeleted( coreUserUpdateDTO.getUDeleted() );
        }
        if ( coreUserUpdateDTO.getUModified() != null ) {
            coreUser.setUModified( coreUserUpdateDTO.getUModified() );
        }
        if ( coreUserUpdateDTO.getUserDept() != null ) {
            coreUser.setUserDept( coreUserUpdateDTO.getUserDept() );
        }
        if ( coreUserUpdateDTO.getUserEmail() != null ) {
            coreUser.setUserEmail( coreUserUpdateDTO.getUserEmail() );
        }
        if ( coreUserUpdateDTO.getUserEmpId() != null ) {
            coreUser.setUserEmpId( coreUserUpdateDTO.getUserEmpId() );
        }
        if ( coreUserUpdateDTO.getUserFname() != null ) {
            coreUser.setUserFname( coreUserUpdateDTO.getUserFname() );
        }
        if ( coreUserUpdateDTO.getUserId() != null ) {
            coreUser.setUserId( coreUserUpdateDTO.getUserId() );
        }
        if ( coreUserUpdateDTO.getUserLname() != null ) {
            coreUser.setUserLname( coreUserUpdateDTO.getUserLname() );
        }
        if ( coreUserUpdateDTO.getUserStatus() != null ) {
            coreUser.setUserStatus( coreUserUpdateDTO.getUserStatus() );
        }
        if ( coreUserUpdateDTO.getUserUname() != null ) {
            coreUser.setUserUname( coreUserUpdateDTO.getUserUname() );
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

        designationDTO.setActive( misDesignation.getActive() );
        designationDTO.setDeleted( map( misDesignation.getDeleted() ) );
        designationDTO.setTCreated( misDesignation.getTCreated() );
        designationDTO.setTDeleted( misDesignation.getTDeleted() );
        designationDTO.setTModified( misDesignation.getTModified() );
        designationDTO.setUCreated( misDesignation.getUCreated() );
        designationDTO.setUDeleted( misDesignation.getUDeleted() );
        designationDTO.setUModified( misDesignation.getUModified() );
        designationDTO.setDesigCode( misDesignation.getDesigCode() );
        designationDTO.setDesigId( misDesignation.getDesigId() );
        designationDTO.setDesigLevel( misDesignation.getDesigLevel() );
        designationDTO.setDesigName( misDesignation.getDesigName() );
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

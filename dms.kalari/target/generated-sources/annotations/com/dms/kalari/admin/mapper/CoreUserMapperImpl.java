package com.dms.kalari.admin.mapper;

import com.dms.kalari.admin.dto.CoreUserDTO;
import com.dms.kalari.admin.dto.CoreUserMemberDTO;
import com.dms.kalari.admin.dto.CoreUserUpdateDTO;
import com.dms.kalari.admin.dto.CoreUserUpdateMemberDTO;
import com.dms.kalari.admin.dto.DesignationDTO;
import com.dms.kalari.admin.entity.CoreUser;
import com.dms.kalari.admin.entity.MisDesignation;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-20T20:39:30+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250523-0729, environment: Java 21.0.7 (Eclipse Adoptium)"
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
        coreUserDTO.setAadhaarNumber( coreUser.getAadhaarNumber() );
        coreUserDTO.setAddressLine1( coreUser.getAddressLine1() );
        coreUserDTO.setAddressLine2( coreUser.getAddressLine2() );
        coreUserDTO.setAddressLine3( coreUser.getAddressLine3() );
        coreUserDTO.setAddressPin( coreUser.getAddressPin() );
        coreUserDTO.setAddressState( coreUser.getAddressState() );
        coreUserDTO.setDesignation( misDesignationToDesignationDTO( coreUser.getDesignation() ) );
        coreUserDTO.setFatherName( coreUser.getFatherName() );
        coreUserDTO.setGender( coreUser.getGender() );
        coreUserDTO.setMobileNumber( coreUser.getMobileNumber() );
        coreUserDTO.setMotherName( coreUser.getMotherName() );
        coreUserDTO.setNationality( coreUser.getNationality() );
        coreUserDTO.setState( coreUser.getState() );
        coreUserDTO.setUserDept( coreUser.getUserDept() );
        coreUserDTO.setUserDob( coreUser.getUserDob() );
        coreUserDTO.setUserEmail( coreUser.getUserEmail() );
        coreUserDTO.setUserEmpId( coreUser.getUserEmpId() );
        coreUserDTO.setUserFname( coreUser.getUserFname() );
        coreUserDTO.setUserId( coreUser.getUserId() );
        coreUserDTO.setUserLname( coreUser.getUserLname() );
        coreUserDTO.setUserMemberCategory( coreUser.getUserMemberCategory() );
        coreUserDTO.setUserNode( map( coreUser.getUserNode() ) );
        coreUserDTO.setUserPassword( coreUser.getUserPassword() );
        coreUserDTO.setUserStatus( coreUser.getUserStatus() );
        coreUserDTO.setUserType( coreUser.getUserType() );
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
        coreUser.setAadhaarNumber( coreUserDTO.getAadhaarNumber() );
        coreUser.setAddressLine1( coreUserDTO.getAddressLine1() );
        coreUser.setAddressLine2( coreUserDTO.getAddressLine2() );
        coreUser.setAddressLine3( coreUserDTO.getAddressLine3() );
        coreUser.setAddressPin( coreUserDTO.getAddressPin() );
        coreUser.setAddressState( coreUserDTO.getAddressState() );
        coreUser.setFatherName( coreUserDTO.getFatherName() );
        coreUser.setGender( coreUserDTO.getGender() );
        coreUser.setMobileNumber( coreUserDTO.getMobileNumber() );
        coreUser.setMotherName( coreUserDTO.getMotherName() );
        coreUser.setNationality( coreUserDTO.getNationality() );
        coreUser.setState( coreUserDTO.getState() );
        coreUser.setUserDept( coreUserDTO.getUserDept() );
        coreUser.setUserDob( coreUserDTO.getUserDob() );
        coreUser.setUserEmail( coreUserDTO.getUserEmail() );
        coreUser.setUserEmpId( coreUserDTO.getUserEmpId() );
        coreUser.setUserFname( coreUserDTO.getUserFname() );
        coreUser.setUserId( coreUserDTO.getUserId() );
        coreUser.setUserLname( coreUserDTO.getUserLname() );
        coreUser.setUserMemberCategory( coreUserDTO.getUserMemberCategory() );
        coreUser.setUserNode( map( coreUserDTO.getUserNode() ) );
        coreUser.setUserPassword( coreUserDTO.getUserPassword() );
        coreUser.setUserStatus( coreUserDTO.getUserStatus() );
        coreUser.setUserType( coreUserDTO.getUserType() );
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
        if ( coreUserUpdateDTO.getUserNode() != null ) {
            coreUser.setUserNode( map( coreUserUpdateDTO.getUserNode() ) );
        }
        if ( coreUserUpdateDTO.getUserStatus() != null ) {
            coreUser.setUserStatus( coreUserUpdateDTO.getUserStatus() );
        }
        if ( coreUserUpdateDTO.getUserUname() != null ) {
            coreUser.setUserUname( coreUserUpdateDTO.getUserUname() );
        }
    }

    @Override
    public CoreUser toEntity(CoreUserMemberDTO CoreUserMemberDTO) {
        if ( CoreUserMemberDTO == null ) {
            return null;
        }

        CoreUser coreUser = new CoreUser();

        coreUser.setDesignation( coreUserMemberDTOToMisDesignation( CoreUserMemberDTO ) );
        coreUser.setUserFname( CoreUserMemberDTO.getUserFname() );
        coreUser.setUserLname( CoreUserMemberDTO.getUserLname() );
        coreUser.setUserEmail( CoreUserMemberDTO.getUserEmail() );
        coreUser.setUserStatus( CoreUserMemberDTO.getUserStatus() );
        coreUser.setUserNode( map( CoreUserMemberDTO.getUserNode() ) );
        coreUser.setActive( CoreUserMemberDTO.getActive() );
        coreUser.setDeleted( map( CoreUserMemberDTO.getDeleted() ) );
        coreUser.setTCreated( CoreUserMemberDTO.getTCreated() );
        coreUser.setTDeleted( CoreUserMemberDTO.getTDeleted() );
        coreUser.setTModified( CoreUserMemberDTO.getTModified() );
        coreUser.setUCreated( CoreUserMemberDTO.getUCreated() );
        coreUser.setUDeleted( CoreUserMemberDTO.getUDeleted() );
        coreUser.setUModified( CoreUserMemberDTO.getUModified() );
        coreUser.setUserId( CoreUserMemberDTO.getUserId() );
        coreUser.setUserPassword( CoreUserMemberDTO.getUserPassword() );
        coreUser.setUserType( CoreUserMemberDTO.getUserType() );
        coreUser.setUserUname( CoreUserMemberDTO.getUserUname() );

        return coreUser;
    }

    @Override
    public void updateCoreUserFromMemberDto(CoreUserUpdateMemberDTO updateMemberDTO, CoreUser coreUser) {
        if ( updateMemberDTO == null ) {
            return;
        }

        if ( coreUser.getDesignation() == null ) {
            coreUser.setDesignation( new MisDesignation() );
        }
        coreUserUpdateMemberDTOToMisDesignation( updateMemberDTO, coreUser.getDesignation() );
        if ( updateMemberDTO.getActive() != null ) {
            coreUser.setActive( updateMemberDTO.getActive() );
        }
        if ( updateMemberDTO.getDeleted() != null ) {
            coreUser.setDeleted( map( updateMemberDTO.getDeleted() ) );
        }
        if ( updateMemberDTO.getTCreated() != null ) {
            coreUser.setTCreated( updateMemberDTO.getTCreated() );
        }
        if ( updateMemberDTO.getTDeleted() != null ) {
            coreUser.setTDeleted( updateMemberDTO.getTDeleted() );
        }
        if ( updateMemberDTO.getTModified() != null ) {
            coreUser.setTModified( updateMemberDTO.getTModified() );
        }
        if ( updateMemberDTO.getUCreated() != null ) {
            coreUser.setUCreated( updateMemberDTO.getUCreated() );
        }
        if ( updateMemberDTO.getUDeleted() != null ) {
            coreUser.setUDeleted( updateMemberDTO.getUDeleted() );
        }
        if ( updateMemberDTO.getUModified() != null ) {
            coreUser.setUModified( updateMemberDTO.getUModified() );
        }
        if ( updateMemberDTO.getUserEmail() != null ) {
            coreUser.setUserEmail( updateMemberDTO.getUserEmail() );
        }
        if ( updateMemberDTO.getUserFname() != null ) {
            coreUser.setUserFname( updateMemberDTO.getUserFname() );
        }
        if ( updateMemberDTO.getUserId() != null ) {
            coreUser.setUserId( updateMemberDTO.getUserId() );
        }
        if ( updateMemberDTO.getUserLname() != null ) {
            coreUser.setUserLname( updateMemberDTO.getUserLname() );
        }
        if ( updateMemberDTO.getUserStatus() != null ) {
            coreUser.setUserStatus( updateMemberDTO.getUserStatus() );
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

    protected MisDesignation coreUserMemberDTOToMisDesignation(CoreUserMemberDTO coreUserMemberDTO) {
        if ( coreUserMemberDTO == null ) {
            return null;
        }

        MisDesignation misDesignation = new MisDesignation();

        misDesignation.setDesigId( coreUserMemberDTO.getUserDesig() );

        return misDesignation;
    }

    protected void coreUserUpdateMemberDTOToMisDesignation(CoreUserUpdateMemberDTO coreUserUpdateMemberDTO, MisDesignation mappingTarget) {
        if ( coreUserUpdateMemberDTO == null ) {
            return;
        }

        if ( coreUserUpdateMemberDTO.getUserDesig() != null ) {
            mappingTarget.setDesigId( coreUserUpdateMemberDTO.getUserDesig() );
        }
    }
}

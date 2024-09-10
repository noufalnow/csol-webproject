package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.users.CoreUserDTO;
import com.example.tenant_service.dto.users.CoreUserUpdateDTO;
import com.example.tenant_service.entity.CoreUser;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-10T09:55:57+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240215-1558, environment: Java 17.0.12 (Eclipse Adoptium)"
)
@Component
public class CoreUserMapperImpl implements CoreUserMapper {

    @Override
    public CoreUserDTO toDTO(CoreUser entity) {
        if ( entity == null ) {
            return null;
        }

        CoreUserDTO coreUserDTO = new CoreUserDTO();

        coreUserDTO.setActive( entity.getActive() );
        coreUserDTO.setDeleted( map( entity.getDeleted() ) );
        coreUserDTO.setTCreated( entity.getTCreated() );
        coreUserDTO.setTDeleted( entity.getTDeleted() );
        coreUserDTO.setTModified( entity.getTModified() );
        coreUserDTO.setUCreated( entity.getUCreated() );
        coreUserDTO.setUDeleted( entity.getUDeleted() );
        coreUserDTO.setUModified( entity.getUModified() );
        coreUserDTO.setUserDept( entity.getUserDept() );
        coreUserDTO.setUserDesig( entity.getUserDesig() );
        coreUserDTO.setUserEmail( entity.getUserEmail() );
        coreUserDTO.setUserEmpId( entity.getUserEmpId() );
        coreUserDTO.setUserFname( entity.getUserFname() );
        coreUserDTO.setUserId( entity.getUserId() );
        coreUserDTO.setUserLname( entity.getUserLname() );
        coreUserDTO.setUserPassword( entity.getUserPassword() );
        coreUserDTO.setUserStatus( entity.getUserStatus() );
        coreUserDTO.setUserUname( entity.getUserUname() );

        return coreUserDTO;
    }

    @Override
    public CoreUser toEntity(CoreUserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CoreUser coreUser = new CoreUser();

        coreUser.setActive( dto.getActive() );
        coreUser.setDeleted( map( dto.getDeleted() ) );
        coreUser.setTCreated( dto.getTCreated() );
        coreUser.setTDeleted( dto.getTDeleted() );
        coreUser.setTModified( dto.getTModified() );
        coreUser.setUCreated( dto.getUCreated() );
        coreUser.setUDeleted( dto.getUDeleted() );
        coreUser.setUModified( dto.getUModified() );
        coreUser.setUserDept( dto.getUserDept() );
        coreUser.setUserDesig( dto.getUserDesig() );
        coreUser.setUserEmail( dto.getUserEmail() );
        coreUser.setUserEmpId( dto.getUserEmpId() );
        coreUser.setUserFname( dto.getUserFname() );
        coreUser.setUserId( dto.getUserId() );
        coreUser.setUserLname( dto.getUserLname() );
        coreUser.setUserPassword( dto.getUserPassword() );
        coreUser.setUserStatus( dto.getUserStatus() );
        coreUser.setUserUname( dto.getUserUname() );

        return coreUser;
    }

    @Override
    public void updateCoreUserFromDto(CoreUserUpdateDTO coreUserUpdateDTO, CoreUser coreUser) {
        if ( coreUserUpdateDTO == null ) {
            return;
        }

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
        if ( coreUserUpdateDTO.getUserDesig() != null ) {
            coreUser.setUserDesig( coreUserUpdateDTO.getUserDesig() );
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
}

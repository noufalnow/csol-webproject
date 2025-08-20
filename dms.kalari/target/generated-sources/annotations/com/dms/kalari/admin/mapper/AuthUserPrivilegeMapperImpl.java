package com.dms.kalari.admin.mapper;

import com.dms.kalari.admin.dto.AppPageOperationDTO;
import com.dms.kalari.admin.dto.AuthAppMenuDTO;
import com.dms.kalari.admin.dto.AuthAppPageDTO;
import com.dms.kalari.admin.dto.AuthUserPrivilegeDTO;
import com.dms.kalari.admin.entity.AuthAppMenu;
import com.dms.kalari.admin.entity.AuthAppPage;
import com.dms.kalari.admin.entity.AuthAppPageOperation;
import com.dms.kalari.admin.entity.AuthUserPrivilege;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-20T20:39:30+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250523-0729, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class AuthUserPrivilegeMapperImpl implements AuthUserPrivilegeMapper {

    @Override
    public AuthUserPrivilegeDTO toDTO(AuthUserPrivilege entity) {
        if ( entity == null ) {
            return null;
        }

        AuthUserPrivilegeDTO authUserPrivilegeDTO = new AuthUserPrivilegeDTO();

        authUserPrivilegeDTO.setActive( entity.getActive() );
        authUserPrivilegeDTO.setDeleted( map( entity.getDeleted() ) );
        authUserPrivilegeDTO.setTCreated( entity.getTCreated() );
        authUserPrivilegeDTO.setTDeleted( entity.getTDeleted() );
        authUserPrivilegeDTO.setTModified( entity.getTModified() );
        authUserPrivilegeDTO.setUCreated( entity.getUCreated() );
        authUserPrivilegeDTO.setUDeleted( entity.getUDeleted() );
        authUserPrivilegeDTO.setUModified( entity.getUModified() );
        authUserPrivilegeDTO.setAppPage( authAppPageToAuthAppPageDTO( entity.getAppPage() ) );
        authUserPrivilegeDTO.setInstId( entity.getInstId() );
        authUserPrivilegeDTO.setModuleId( entity.getModuleId() );
        authUserPrivilegeDTO.setOperation( authAppPageOperationToAppPageOperationDTO( entity.getOperation() ) );
        authUserPrivilegeDTO.setRoleId( entity.getRoleId() );
        authUserPrivilegeDTO.setUserPrivilegeId( entity.getUserPrivilegeId() );

        return authUserPrivilegeDTO;
    }

    @Override
    public AuthUserPrivilege toEntity(AuthUserPrivilegeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AuthUserPrivilege authUserPrivilege = new AuthUserPrivilege();

        authUserPrivilege.setActive( dto.getActive() );
        authUserPrivilege.setDeleted( map( dto.getDeleted() ) );
        authUserPrivilege.setTCreated( dto.getTCreated() );
        authUserPrivilege.setTDeleted( dto.getTDeleted() );
        authUserPrivilege.setTModified( dto.getTModified() );
        authUserPrivilege.setUCreated( dto.getUCreated() );
        authUserPrivilege.setUDeleted( dto.getUDeleted() );
        authUserPrivilege.setUModified( dto.getUModified() );
        authUserPrivilege.setAppPage( authAppPageDTOToAuthAppPage( dto.getAppPage() ) );
        authUserPrivilege.setInstId( dto.getInstId() );
        authUserPrivilege.setModuleId( dto.getModuleId() );
        authUserPrivilege.setOperation( appPageOperationDTOToAuthAppPageOperation( dto.getOperation() ) );
        authUserPrivilege.setRoleId( dto.getRoleId() );
        authUserPrivilege.setUserPrivilegeId( dto.getUserPrivilegeId() );

        return authUserPrivilege;
    }

    protected AuthAppMenuDTO authAppMenuToAuthAppMenuDTO(AuthAppMenu authAppMenu) {
        if ( authAppMenu == null ) {
            return null;
        }

        AuthAppMenuDTO authAppMenuDTO = new AuthAppMenuDTO();

        authAppMenuDTO.setActive( authAppMenu.getActive() );
        authAppMenuDTO.setDeleted( map( authAppMenu.getDeleted() ) );
        authAppMenuDTO.setTCreated( authAppMenu.getTCreated() );
        authAppMenuDTO.setTDeleted( authAppMenu.getTDeleted() );
        authAppMenuDTO.setTModified( authAppMenu.getTModified() );
        authAppMenuDTO.setUCreated( authAppMenu.getUCreated() );
        authAppMenuDTO.setUDeleted( authAppMenu.getUDeleted() );
        authAppMenuDTO.setUModified( authAppMenu.getUModified() );
        authAppMenuDTO.setAppMenuId( authAppMenu.getAppMenuId() );
        authAppMenuDTO.setMenuName( authAppMenu.getMenuName() );
        authAppMenuDTO.setOrderNumber( authAppMenu.getOrderNumber() );

        return authAppMenuDTO;
    }

    protected AuthAppPageDTO authAppPageToAuthAppPageDTO(AuthAppPage authAppPage) {
        if ( authAppPage == null ) {
            return null;
        }

        AuthAppPageDTO authAppPageDTO = new AuthAppPageDTO();

        authAppPageDTO.setActive( authAppPage.getActive() );
        authAppPageDTO.setDeleted( map( authAppPage.getDeleted() ) );
        authAppPageDTO.setTCreated( authAppPage.getTCreated() );
        authAppPageDTO.setTDeleted( authAppPage.getTDeleted() );
        authAppPageDTO.setTModified( authAppPage.getTModified() );
        authAppPageDTO.setUCreated( authAppPage.getUCreated() );
        authAppPageDTO.setUDeleted( authAppPage.getUDeleted() );
        authAppPageDTO.setUModified( authAppPage.getUModified() );
        authAppPageDTO.setAppPageId( authAppPage.getAppPageId() );
        authAppPageDTO.setMenu( authAppMenuToAuthAppMenuDTO( authAppPage.getMenu() ) );
        authAppPageDTO.setPageName( authAppPage.getPageName() );

        return authAppPageDTO;
    }

    protected AppPageOperationDTO authAppPageOperationToAppPageOperationDTO(AuthAppPageOperation authAppPageOperation) {
        if ( authAppPageOperation == null ) {
            return null;
        }

        AppPageOperationDTO appPageOperationDTO = new AppPageOperationDTO();

        appPageOperationDTO.setActive( authAppPageOperation.getActive() );
        appPageOperationDTO.setDeleted( map( authAppPageOperation.getDeleted() ) );
        appPageOperationDTO.setTCreated( authAppPageOperation.getTCreated() );
        appPageOperationDTO.setTDeleted( authAppPageOperation.getTDeleted() );
        appPageOperationDTO.setTModified( authAppPageOperation.getTModified() );
        appPageOperationDTO.setUCreated( authAppPageOperation.getUCreated() );
        appPageOperationDTO.setUDeleted( authAppPageOperation.getUDeleted() );
        appPageOperationDTO.setUModified( authAppPageOperation.getUModified() );
        appPageOperationDTO.setAlias( authAppPageOperation.getAlias() );
        appPageOperationDTO.setOperation( authAppPageOperation.getOperation() );
        appPageOperationDTO.setOperationId( authAppPageOperation.getOperationId() );
        appPageOperationDTO.setRealPath( authAppPageOperation.getRealPath() );

        return appPageOperationDTO;
    }

    protected AuthAppMenu authAppMenuDTOToAuthAppMenu(AuthAppMenuDTO authAppMenuDTO) {
        if ( authAppMenuDTO == null ) {
            return null;
        }

        AuthAppMenu authAppMenu = new AuthAppMenu();

        authAppMenu.setActive( authAppMenuDTO.getActive() );
        authAppMenu.setDeleted( map( authAppMenuDTO.getDeleted() ) );
        authAppMenu.setTCreated( authAppMenuDTO.getTCreated() );
        authAppMenu.setTDeleted( authAppMenuDTO.getTDeleted() );
        authAppMenu.setTModified( authAppMenuDTO.getTModified() );
        authAppMenu.setUCreated( authAppMenuDTO.getUCreated() );
        authAppMenu.setUDeleted( authAppMenuDTO.getUDeleted() );
        authAppMenu.setUModified( authAppMenuDTO.getUModified() );
        authAppMenu.setAppMenuId( authAppMenuDTO.getAppMenuId() );
        authAppMenu.setMenuName( authAppMenuDTO.getMenuName() );
        authAppMenu.setOrderNumber( authAppMenuDTO.getOrderNumber() );

        return authAppMenu;
    }

    protected AuthAppPage authAppPageDTOToAuthAppPage(AuthAppPageDTO authAppPageDTO) {
        if ( authAppPageDTO == null ) {
            return null;
        }

        AuthAppPage authAppPage = new AuthAppPage();

        authAppPage.setActive( authAppPageDTO.getActive() );
        authAppPage.setDeleted( map( authAppPageDTO.getDeleted() ) );
        authAppPage.setTCreated( authAppPageDTO.getTCreated() );
        authAppPage.setTDeleted( authAppPageDTO.getTDeleted() );
        authAppPage.setTModified( authAppPageDTO.getTModified() );
        authAppPage.setUCreated( authAppPageDTO.getUCreated() );
        authAppPage.setUDeleted( authAppPageDTO.getUDeleted() );
        authAppPage.setUModified( authAppPageDTO.getUModified() );
        authAppPage.setAppPageId( authAppPageDTO.getAppPageId() );
        authAppPage.setMenu( authAppMenuDTOToAuthAppMenu( authAppPageDTO.getMenu() ) );
        authAppPage.setPageName( authAppPageDTO.getPageName() );

        return authAppPage;
    }

    protected AuthAppPageOperation appPageOperationDTOToAuthAppPageOperation(AppPageOperationDTO appPageOperationDTO) {
        if ( appPageOperationDTO == null ) {
            return null;
        }

        AuthAppPageOperation authAppPageOperation = new AuthAppPageOperation();

        authAppPageOperation.setActive( appPageOperationDTO.getActive() );
        authAppPageOperation.setDeleted( map( appPageOperationDTO.getDeleted() ) );
        authAppPageOperation.setTCreated( appPageOperationDTO.getTCreated() );
        authAppPageOperation.setTDeleted( appPageOperationDTO.getTDeleted() );
        authAppPageOperation.setTModified( appPageOperationDTO.getTModified() );
        authAppPageOperation.setUCreated( appPageOperationDTO.getUCreated() );
        authAppPageOperation.setUDeleted( appPageOperationDTO.getUDeleted() );
        authAppPageOperation.setUModified( appPageOperationDTO.getUModified() );
        authAppPageOperation.setAlias( appPageOperationDTO.getAlias() );
        authAppPageOperation.setOperation( appPageOperationDTO.getOperation() );
        authAppPageOperation.setOperationId( appPageOperationDTO.getOperationId() );
        authAppPageOperation.setRealPath( appPageOperationDTO.getRealPath() );

        return authAppPageOperation;
    }
}

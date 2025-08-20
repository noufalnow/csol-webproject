package com.dms.kalari.admin.mapper;

import com.dms.kalari.admin.dto.AuthAppMenuDTO;
import com.dms.kalari.admin.dto.AuthAppPageDTO;
import com.dms.kalari.admin.entity.AuthAppMenu;
import com.dms.kalari.admin.entity.AuthAppPage;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-20T20:39:29+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250523-0729, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class AuthAppPageMapperImpl implements AuthAppPageMapper {

    @Override
    public AuthAppPageDTO toDTO(AuthAppPage entity) {
        if ( entity == null ) {
            return null;
        }

        AuthAppPageDTO authAppPageDTO = new AuthAppPageDTO();

        authAppPageDTO.setActive( entity.getActive() );
        authAppPageDTO.setDeleted( map( entity.getDeleted() ) );
        authAppPageDTO.setTCreated( entity.getTCreated() );
        authAppPageDTO.setTDeleted( entity.getTDeleted() );
        authAppPageDTO.setTModified( entity.getTModified() );
        authAppPageDTO.setUCreated( entity.getUCreated() );
        authAppPageDTO.setUDeleted( entity.getUDeleted() );
        authAppPageDTO.setUModified( entity.getUModified() );
        authAppPageDTO.setAppPageId( entity.getAppPageId() );
        authAppPageDTO.setMenu( authAppMenuToAuthAppMenuDTO( entity.getMenu() ) );
        authAppPageDTO.setPageName( entity.getPageName() );

        return authAppPageDTO;
    }

    @Override
    public AuthAppPage toEntity(AuthAppPageDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AuthAppPage authAppPage = new AuthAppPage();

        authAppPage.setActive( dto.getActive() );
        authAppPage.setDeleted( map( dto.getDeleted() ) );
        authAppPage.setTCreated( dto.getTCreated() );
        authAppPage.setTDeleted( dto.getTDeleted() );
        authAppPage.setTModified( dto.getTModified() );
        authAppPage.setUCreated( dto.getUCreated() );
        authAppPage.setUDeleted( dto.getUDeleted() );
        authAppPage.setUModified( dto.getUModified() );
        authAppPage.setAppPageId( dto.getAppPageId() );
        authAppPage.setMenu( authAppMenuDTOToAuthAppMenu( dto.getMenu() ) );
        authAppPage.setPageName( dto.getPageName() );

        return authAppPage;
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
}

package com.dms.kalari.admin.mapper;

import com.dms.kalari.admin.dto.AuthAppMenuDTO;
import com.dms.kalari.admin.entity.AuthAppMenu;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-20T20:39:29+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250523-0729, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class AuthAppMenuMapperImpl implements AuthAppMenuMapper {

    @Override
    public AuthAppMenuDTO toDTO(AuthAppMenu entity) {
        if ( entity == null ) {
            return null;
        }

        AuthAppMenuDTO authAppMenuDTO = new AuthAppMenuDTO();

        authAppMenuDTO.setActive( entity.getActive() );
        authAppMenuDTO.setDeleted( map( entity.getDeleted() ) );
        authAppMenuDTO.setTCreated( entity.getTCreated() );
        authAppMenuDTO.setTDeleted( entity.getTDeleted() );
        authAppMenuDTO.setTModified( entity.getTModified() );
        authAppMenuDTO.setUCreated( entity.getUCreated() );
        authAppMenuDTO.setUDeleted( entity.getUDeleted() );
        authAppMenuDTO.setUModified( entity.getUModified() );
        authAppMenuDTO.setAppMenuId( entity.getAppMenuId() );
        authAppMenuDTO.setMenuName( entity.getMenuName() );
        authAppMenuDTO.setOrderNumber( entity.getOrderNumber() );

        return authAppMenuDTO;
    }

    @Override
    public AuthAppMenu toEntity(AuthAppMenuDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AuthAppMenu authAppMenu = new AuthAppMenu();

        authAppMenu.setActive( dto.getActive() );
        authAppMenu.setDeleted( map( dto.getDeleted() ) );
        authAppMenu.setTCreated( dto.getTCreated() );
        authAppMenu.setTDeleted( dto.getTDeleted() );
        authAppMenu.setTModified( dto.getTModified() );
        authAppMenu.setUCreated( dto.getUCreated() );
        authAppMenu.setUDeleted( dto.getUDeleted() );
        authAppMenu.setUModified( dto.getUModified() );
        authAppMenu.setAppMenuId( dto.getAppMenuId() );
        authAppMenu.setMenuName( dto.getMenuName() );
        authAppMenu.setOrderNumber( dto.getOrderNumber() );

        return authAppMenu;
    }
}

package com.dms.kalari.admin.mapper;

import com.dms.kalari.admin.dto.AppPageOperationDTO;
import com.dms.kalari.admin.entity.AuthAppPageOperation;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-20T20:39:30+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250523-0729, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class AuthAppPageOperationMapperImpl implements AuthAppPageOperationMapper {

    @Override
    public AppPageOperationDTO toDTO(AuthAppPageOperation entity) {
        if ( entity == null ) {
            return null;
        }

        AppPageOperationDTO appPageOperationDTO = new AppPageOperationDTO();

        appPageOperationDTO.setActive( entity.getActive() );
        appPageOperationDTO.setDeleted( map( entity.getDeleted() ) );
        appPageOperationDTO.setTCreated( entity.getTCreated() );
        appPageOperationDTO.setTDeleted( entity.getTDeleted() );
        appPageOperationDTO.setTModified( entity.getTModified() );
        appPageOperationDTO.setUCreated( entity.getUCreated() );
        appPageOperationDTO.setUDeleted( entity.getUDeleted() );
        appPageOperationDTO.setUModified( entity.getUModified() );
        appPageOperationDTO.setAlias( entity.getAlias() );
        appPageOperationDTO.setOperation( entity.getOperation() );
        appPageOperationDTO.setOperationId( entity.getOperationId() );
        appPageOperationDTO.setRealPath( entity.getRealPath() );

        return appPageOperationDTO;
    }

    @Override
    public AuthAppPageOperation toEntity(AppPageOperationDTO dto) {
        if ( dto == null ) {
            return null;
        }

        AuthAppPageOperation authAppPageOperation = new AuthAppPageOperation();

        authAppPageOperation.setActive( dto.getActive() );
        authAppPageOperation.setDeleted( map( dto.getDeleted() ) );
        authAppPageOperation.setTCreated( dto.getTCreated() );
        authAppPageOperation.setTDeleted( dto.getTDeleted() );
        authAppPageOperation.setTModified( dto.getTModified() );
        authAppPageOperation.setUCreated( dto.getUCreated() );
        authAppPageOperation.setUDeleted( dto.getUDeleted() );
        authAppPageOperation.setUModified( dto.getUModified() );
        authAppPageOperation.setAlias( dto.getAlias() );
        authAppPageOperation.setOperation( dto.getOperation() );
        authAppPageOperation.setOperationId( dto.getOperationId() );
        authAppPageOperation.setRealPath( dto.getRealPath() );

        return authAppPageOperation;
    }
}

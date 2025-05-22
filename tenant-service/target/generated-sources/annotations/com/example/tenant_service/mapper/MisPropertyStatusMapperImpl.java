package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.PropertyStatusDTO;
import com.example.tenant_service.entity.MisPropertyStatus;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T20:55:15+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.13 (Ubuntu)"
)
@Component
public class MisPropertyStatusMapperImpl implements MisPropertyStatusMapper {

    @Override
    public PropertyStatusDTO toDTO(MisPropertyStatus entity) {
        if ( entity == null ) {
            return null;
        }

        PropertyStatusDTO propertyStatusDTO = new PropertyStatusDTO();

        propertyStatusDTO.setUCreated( entity.getUCreated() );
        propertyStatusDTO.setTCreated( entity.getTCreated() );
        propertyStatusDTO.setTModified( entity.getTModified() );
        propertyStatusDTO.setUModified( entity.getUModified() );
        propertyStatusDTO.setUDeleted( entity.getUDeleted() );
        propertyStatusDTO.setTDeleted( entity.getTDeleted() );
        propertyStatusDTO.setDeleted( map( entity.getDeleted() ) );
        propertyStatusDTO.setActive( entity.getActive() );
        propertyStatusDTO.setPstsId( entity.getPstsId() );
        propertyStatusDTO.setPstsType( entity.getPstsType() );
        propertyStatusDTO.setPstsPropId( entity.getPstsPropId() );
        propertyStatusDTO.setPstsRemarks( entity.getPstsRemarks() );
        propertyStatusDTO.setPstsStatusDate( entity.getPstsStatusDate() );
        propertyStatusDTO.setPstsAttachProp( entity.getPstsAttachProp() );

        return propertyStatusDTO;
    }

    @Override
    public MisPropertyStatus toEntity(PropertyStatusDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MisPropertyStatus misPropertyStatus = new MisPropertyStatus();

        misPropertyStatus.setUCreated( dto.getUCreated() );
        misPropertyStatus.setTCreated( dto.getTCreated() );
        misPropertyStatus.setTModified( dto.getTModified() );
        misPropertyStatus.setUModified( dto.getUModified() );
        misPropertyStatus.setUDeleted( dto.getUDeleted() );
        misPropertyStatus.setTDeleted( dto.getTDeleted() );
        misPropertyStatus.setDeleted( map( dto.getDeleted() ) );
        misPropertyStatus.setActive( dto.getActive() );
        misPropertyStatus.setPstsId( dto.getPstsId() );
        misPropertyStatus.setPstsType( dto.getPstsType() );
        misPropertyStatus.setPstsPropId( dto.getPstsPropId() );
        misPropertyStatus.setPstsRemarks( dto.getPstsRemarks() );
        misPropertyStatus.setPstsStatusDate( dto.getPstsStatusDate() );
        misPropertyStatus.setPstsAttachProp( dto.getPstsAttachProp() );

        return misPropertyStatus;
    }
}

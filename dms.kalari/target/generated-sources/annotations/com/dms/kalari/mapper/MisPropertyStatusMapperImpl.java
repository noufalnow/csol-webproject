package com.dms.kalari.mapper;

import com.dms.kalari.dto.PropertyStatusDTO;
import com.dms.kalari.entity.MisPropertyStatus;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-19T13:35:09+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250523-0729, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class MisPropertyStatusMapperImpl implements MisPropertyStatusMapper {

    @Override
    public PropertyStatusDTO toDTO(MisPropertyStatus entity) {
        if ( entity == null ) {
            return null;
        }

        PropertyStatusDTO propertyStatusDTO = new PropertyStatusDTO();

        propertyStatusDTO.setActive( entity.getActive() );
        propertyStatusDTO.setDeleted( map( entity.getDeleted() ) );
        propertyStatusDTO.setTCreated( entity.getTCreated() );
        propertyStatusDTO.setTDeleted( entity.getTDeleted() );
        propertyStatusDTO.setTModified( entity.getTModified() );
        propertyStatusDTO.setUCreated( entity.getUCreated() );
        propertyStatusDTO.setUDeleted( entity.getUDeleted() );
        propertyStatusDTO.setUModified( entity.getUModified() );
        propertyStatusDTO.setPstsAttachProp( entity.getPstsAttachProp() );
        propertyStatusDTO.setPstsId( entity.getPstsId() );
        propertyStatusDTO.setPstsPropId( entity.getPstsPropId() );
        propertyStatusDTO.setPstsRemarks( entity.getPstsRemarks() );
        propertyStatusDTO.setPstsStatusDate( entity.getPstsStatusDate() );
        propertyStatusDTO.setPstsType( entity.getPstsType() );

        return propertyStatusDTO;
    }

    @Override
    public MisPropertyStatus toEntity(PropertyStatusDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MisPropertyStatus misPropertyStatus = new MisPropertyStatus();

        misPropertyStatus.setActive( dto.getActive() );
        misPropertyStatus.setDeleted( map( dto.getDeleted() ) );
        misPropertyStatus.setTCreated( dto.getTCreated() );
        misPropertyStatus.setTDeleted( dto.getTDeleted() );
        misPropertyStatus.setTModified( dto.getTModified() );
        misPropertyStatus.setUCreated( dto.getUCreated() );
        misPropertyStatus.setUDeleted( dto.getUDeleted() );
        misPropertyStatus.setUModified( dto.getUModified() );
        misPropertyStatus.setPstsAttachProp( dto.getPstsAttachProp() );
        misPropertyStatus.setPstsId( dto.getPstsId() );
        misPropertyStatus.setPstsPropId( dto.getPstsPropId() );
        misPropertyStatus.setPstsRemarks( dto.getPstsRemarks() );
        misPropertyStatus.setPstsStatusDate( dto.getPstsStatusDate() );
        misPropertyStatus.setPstsType( dto.getPstsType() );

        return misPropertyStatus;
    }
}

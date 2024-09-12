package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.PropertyStatusDTO;
import com.example.tenant_service.entity.MisPropertyStatus;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-11T14:19:48+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240215-1558, environment: Java 17.0.12 (Eclipse Adoptium)"
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

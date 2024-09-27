package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.PropertyPayOptionDTO;
import com.example.tenant_service.entity.MisPropertyPayOption;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-27T11:53:03+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240215-1558, environment: Java 17.0.12 (Eclipse Adoptium)"
)
@Component
public class MisPropertyPayOptionMapperImpl implements MisPropertyPayOptionMapper {

    @Override
    public PropertyPayOptionDTO toDTO(MisPropertyPayOption entity) {
        if ( entity == null ) {
            return null;
        }

        PropertyPayOptionDTO propertyPayOptionDTO = new PropertyPayOptionDTO();

        propertyPayOptionDTO.setActive( entity.getActive() );
        propertyPayOptionDTO.setDeleted( map( entity.getDeleted() ) );
        propertyPayOptionDTO.setTCreated( entity.getTCreated() );
        propertyPayOptionDTO.setTDeleted( entity.getTDeleted() );
        propertyPayOptionDTO.setTModified( entity.getTModified() );
        propertyPayOptionDTO.setUCreated( entity.getUCreated() );
        propertyPayOptionDTO.setUDeleted( entity.getUDeleted() );
        propertyPayOptionDTO.setUModified( entity.getUModified() );
        propertyPayOptionDTO.setPoptAmount( entity.getPoptAmount() );
        propertyPayOptionDTO.setPoptBank( entity.getPoptBank() );
        propertyPayOptionDTO.setPoptChqno( entity.getPoptChqno() );
        propertyPayOptionDTO.setPoptDate( entity.getPoptDate() );
        propertyPayOptionDTO.setPoptDocId( entity.getPoptDocId() );
        propertyPayOptionDTO.setPoptId( entity.getPoptId() );
        propertyPayOptionDTO.setPoptPropId( entity.getPoptPropId() );
        propertyPayOptionDTO.setPoptStatus( entity.getPoptStatus() );
        propertyPayOptionDTO.setPoptStatusDate( entity.getPoptStatusDate() );
        propertyPayOptionDTO.setPoptType( entity.getPoptType() );

        return propertyPayOptionDTO;
    }

    @Override
    public MisPropertyPayOption toEntity(PropertyPayOptionDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MisPropertyPayOption misPropertyPayOption = new MisPropertyPayOption();

        misPropertyPayOption.setActive( dto.getActive() );
        misPropertyPayOption.setDeleted( map( dto.getDeleted() ) );
        misPropertyPayOption.setTCreated( dto.getTCreated() );
        misPropertyPayOption.setTDeleted( dto.getTDeleted() );
        misPropertyPayOption.setTModified( dto.getTModified() );
        misPropertyPayOption.setUCreated( dto.getUCreated() );
        misPropertyPayOption.setUDeleted( dto.getUDeleted() );
        misPropertyPayOption.setUModified( dto.getUModified() );
        misPropertyPayOption.setPoptAmount( dto.getPoptAmount() );
        misPropertyPayOption.setPoptBank( dto.getPoptBank() );
        misPropertyPayOption.setPoptChqno( dto.getPoptChqno() );
        misPropertyPayOption.setPoptDate( dto.getPoptDate() );
        misPropertyPayOption.setPoptDocId( dto.getPoptDocId() );
        misPropertyPayOption.setPoptId( dto.getPoptId() );
        misPropertyPayOption.setPoptPropId( dto.getPoptPropId() );
        misPropertyPayOption.setPoptStatus( dto.getPoptStatus() );
        misPropertyPayOption.setPoptStatusDate( dto.getPoptStatusDate() );
        misPropertyPayOption.setPoptType( dto.getPoptType() );

        return misPropertyPayOption;
    }
}

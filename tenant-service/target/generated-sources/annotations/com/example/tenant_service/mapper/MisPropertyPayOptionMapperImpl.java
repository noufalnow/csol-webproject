package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.PropertyPayOptionDTO;
import com.example.tenant_service.entity.MisPropertyPayOption;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T20:55:15+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.13 (Ubuntu)"
)
@Component
public class MisPropertyPayOptionMapperImpl implements MisPropertyPayOptionMapper {

    @Override
    public PropertyPayOptionDTO toDTO(MisPropertyPayOption entity) {
        if ( entity == null ) {
            return null;
        }

        PropertyPayOptionDTO propertyPayOptionDTO = new PropertyPayOptionDTO();

        propertyPayOptionDTO.setUCreated( entity.getUCreated() );
        propertyPayOptionDTO.setTCreated( entity.getTCreated() );
        propertyPayOptionDTO.setTModified( entity.getTModified() );
        propertyPayOptionDTO.setUModified( entity.getUModified() );
        propertyPayOptionDTO.setUDeleted( entity.getUDeleted() );
        propertyPayOptionDTO.setTDeleted( entity.getTDeleted() );
        propertyPayOptionDTO.setDeleted( map( entity.getDeleted() ) );
        propertyPayOptionDTO.setActive( entity.getActive() );
        propertyPayOptionDTO.setPoptId( entity.getPoptId() );
        propertyPayOptionDTO.setPoptPropId( entity.getPoptPropId() );
        propertyPayOptionDTO.setPoptDocId( entity.getPoptDocId() );
        propertyPayOptionDTO.setPoptType( entity.getPoptType() );
        propertyPayOptionDTO.setPoptDate( entity.getPoptDate() );
        propertyPayOptionDTO.setPoptAmount( entity.getPoptAmount() );
        propertyPayOptionDTO.setPoptBank( entity.getPoptBank() );
        propertyPayOptionDTO.setPoptChqno( entity.getPoptChqno() );
        propertyPayOptionDTO.setPoptStatus( entity.getPoptStatus() );
        propertyPayOptionDTO.setPoptStatusDate( entity.getPoptStatusDate() );

        return propertyPayOptionDTO;
    }

    @Override
    public MisPropertyPayOption toEntity(PropertyPayOptionDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MisPropertyPayOption misPropertyPayOption = new MisPropertyPayOption();

        misPropertyPayOption.setUCreated( dto.getUCreated() );
        misPropertyPayOption.setTCreated( dto.getTCreated() );
        misPropertyPayOption.setTModified( dto.getTModified() );
        misPropertyPayOption.setUModified( dto.getUModified() );
        misPropertyPayOption.setUDeleted( dto.getUDeleted() );
        misPropertyPayOption.setTDeleted( dto.getTDeleted() );
        misPropertyPayOption.setDeleted( map( dto.getDeleted() ) );
        misPropertyPayOption.setActive( dto.getActive() );
        misPropertyPayOption.setPoptId( dto.getPoptId() );
        misPropertyPayOption.setPoptPropId( dto.getPoptPropId() );
        misPropertyPayOption.setPoptDocId( dto.getPoptDocId() );
        misPropertyPayOption.setPoptType( dto.getPoptType() );
        misPropertyPayOption.setPoptDate( dto.getPoptDate() );
        misPropertyPayOption.setPoptAmount( dto.getPoptAmount() );
        misPropertyPayOption.setPoptBank( dto.getPoptBank() );
        misPropertyPayOption.setPoptChqno( dto.getPoptChqno() );
        misPropertyPayOption.setPoptStatus( dto.getPoptStatus() );
        misPropertyPayOption.setPoptStatusDate( dto.getPoptStatusDate() );

        return misPropertyPayOption;
    }
}

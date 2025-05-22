package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.PropertyDTO;
import com.example.tenant_service.entity.MisProperty;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T20:55:15+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.13 (Ubuntu)"
)
@Component
public class MisPropertyMapperImpl implements MisPropertyMapper {

    @Override
    public PropertyDTO toDTO(MisProperty entity) {
        if ( entity == null ) {
            return null;
        }

        PropertyDTO propertyDTO = new PropertyDTO();

        propertyDTO.setUCreated( entity.getUCreated() );
        propertyDTO.setTCreated( entity.getTCreated() );
        propertyDTO.setTModified( entity.getTModified() );
        propertyDTO.setUModified( entity.getUModified() );
        propertyDTO.setUDeleted( entity.getUDeleted() );
        propertyDTO.setTDeleted( entity.getTDeleted() );
        propertyDTO.setDeleted( map( entity.getDeleted() ) );
        propertyDTO.setActive( entity.getActive() );
        propertyDTO.setPropId( entity.getPropId() );
        propertyDTO.setPropNo( entity.getPropNo() );
        propertyDTO.setPropName( entity.getPropName() );
        propertyDTO.setPropFileno( entity.getPropFileno() );
        propertyDTO.setPropBuilding( entity.getPropBuilding() );
        propertyDTO.setPropResponsible( entity.getPropResponsible() );
        propertyDTO.setPropRemarks( entity.getPropRemarks() );
        propertyDTO.setPropCat( entity.getPropCat() );
        propertyDTO.setPropType( entity.getPropType() );
        propertyDTO.setPropLevel( entity.getPropLevel() );
        propertyDTO.setPropElecMeter( entity.getPropElecMeter() );
        propertyDTO.setPropWater( entity.getPropWater() );
        propertyDTO.setPropBuildingType( entity.getPropBuildingType() );
        propertyDTO.setPropStatus( entity.getPropStatus() );
        propertyDTO.setPropElecAccount( entity.getPropElecAccount() );
        propertyDTO.setPropElecRecharge( entity.getPropElecRecharge() );
        propertyDTO.setPropAccount( entity.getPropAccount() );

        return propertyDTO;
    }

    @Override
    public MisProperty toEntity(PropertyDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MisProperty misProperty = new MisProperty();

        misProperty.setUCreated( dto.getUCreated() );
        misProperty.setTCreated( dto.getTCreated() );
        misProperty.setTModified( dto.getTModified() );
        misProperty.setUModified( dto.getUModified() );
        misProperty.setUDeleted( dto.getUDeleted() );
        misProperty.setTDeleted( dto.getTDeleted() );
        misProperty.setDeleted( map( dto.getDeleted() ) );
        misProperty.setActive( dto.getActive() );
        misProperty.setPropId( dto.getPropId() );
        misProperty.setPropNo( dto.getPropNo() );
        misProperty.setPropName( dto.getPropName() );
        misProperty.setPropFileno( dto.getPropFileno() );
        misProperty.setPropBuilding( dto.getPropBuilding() );
        misProperty.setPropResponsible( dto.getPropResponsible() );
        misProperty.setPropRemarks( dto.getPropRemarks() );
        misProperty.setPropCat( dto.getPropCat() );
        misProperty.setPropType( dto.getPropType() );
        misProperty.setPropLevel( dto.getPropLevel() );
        misProperty.setPropElecMeter( dto.getPropElecMeter() );
        misProperty.setPropWater( dto.getPropWater() );
        misProperty.setPropBuildingType( dto.getPropBuildingType() );
        misProperty.setPropStatus( dto.getPropStatus() );
        misProperty.setPropElecAccount( dto.getPropElecAccount() );
        misProperty.setPropElecRecharge( dto.getPropElecRecharge() );
        misProperty.setPropAccount( dto.getPropAccount() );

        return misProperty;
    }
}

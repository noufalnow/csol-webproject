package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.PropertyDTO;
import com.example.tenant_service.entity.MisProperty;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-10T09:55:57+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240215-1558, environment: Java 17.0.12 (Eclipse Adoptium)"
)
@Component
public class MisPropertyMapperImpl implements MisPropertyMapper {

    @Override
    public PropertyDTO toDTO(MisProperty entity) {
        if ( entity == null ) {
            return null;
        }

        PropertyDTO propertyDTO = new PropertyDTO();

        propertyDTO.setActive( entity.getActive() );
        propertyDTO.setDeleted( map( entity.getDeleted() ) );
        propertyDTO.setTCreated( entity.getTCreated() );
        propertyDTO.setTDeleted( entity.getTDeleted() );
        propertyDTO.setTModified( entity.getTModified() );
        propertyDTO.setUCreated( entity.getUCreated() );
        propertyDTO.setUDeleted( entity.getUDeleted() );
        propertyDTO.setUModified( entity.getUModified() );
        propertyDTO.setPropAccount( entity.getPropAccount() );
        propertyDTO.setPropBuilding( entity.getPropBuilding() );
        propertyDTO.setPropBuildingType( entity.getPropBuildingType() );
        propertyDTO.setPropCat( entity.getPropCat() );
        propertyDTO.setPropElecAccount( entity.getPropElecAccount() );
        propertyDTO.setPropElecMeter( entity.getPropElecMeter() );
        propertyDTO.setPropElecRecharge( entity.getPropElecRecharge() );
        propertyDTO.setPropFileno( entity.getPropFileno() );
        propertyDTO.setPropId( entity.getPropId() );
        propertyDTO.setPropLevel( entity.getPropLevel() );
        propertyDTO.setPropName( entity.getPropName() );
        propertyDTO.setPropNo( entity.getPropNo() );
        propertyDTO.setPropRemarks( entity.getPropRemarks() );
        propertyDTO.setPropResponsible( entity.getPropResponsible() );
        propertyDTO.setPropStatus( entity.getPropStatus() );
        propertyDTO.setPropType( entity.getPropType() );
        propertyDTO.setPropWater( entity.getPropWater() );

        return propertyDTO;
    }

    @Override
    public MisProperty toEntity(PropertyDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MisProperty misProperty = new MisProperty();

        misProperty.setActive( dto.getActive() );
        misProperty.setDeleted( map( dto.getDeleted() ) );
        misProperty.setTCreated( dto.getTCreated() );
        misProperty.setTDeleted( dto.getTDeleted() );
        misProperty.setTModified( dto.getTModified() );
        misProperty.setUCreated( dto.getUCreated() );
        misProperty.setUDeleted( dto.getUDeleted() );
        misProperty.setUModified( dto.getUModified() );
        misProperty.setPropAccount( dto.getPropAccount() );
        misProperty.setPropBuilding( dto.getPropBuilding() );
        misProperty.setPropBuildingType( dto.getPropBuildingType() );
        misProperty.setPropCat( dto.getPropCat() );
        misProperty.setPropElecAccount( dto.getPropElecAccount() );
        misProperty.setPropElecMeter( dto.getPropElecMeter() );
        misProperty.setPropElecRecharge( dto.getPropElecRecharge() );
        misProperty.setPropFileno( dto.getPropFileno() );
        misProperty.setPropId( dto.getPropId() );
        misProperty.setPropLevel( dto.getPropLevel() );
        misProperty.setPropName( dto.getPropName() );
        misProperty.setPropNo( dto.getPropNo() );
        misProperty.setPropRemarks( dto.getPropRemarks() );
        misProperty.setPropResponsible( dto.getPropResponsible() );
        misProperty.setPropStatus( dto.getPropStatus() );
        misProperty.setPropType( dto.getPropType() );
        misProperty.setPropWater( dto.getPropWater() );

        return misProperty;
    }
}

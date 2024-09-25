package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.BuildingDTO;
import com.example.tenant_service.entity.MisBuilding;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-24T22:29:30+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240215-1558, environment: Java 17.0.12 (Eclipse Adoptium)"
)
@Component
public class MisBuildingMapperImpl implements MisBuildingMapper {

    @Override
    public BuildingDTO toDTO(MisBuilding entity) {
        if ( entity == null ) {
            return null;
        }

        BuildingDTO buildingDTO = new BuildingDTO();

        buildingDTO.setActive( entity.getActive() );
        buildingDTO.setDeleted( map( entity.getDeleted() ) );
        buildingDTO.setTCreated( entity.getTCreated() );
        buildingDTO.setTDeleted( entity.getTDeleted() );
        buildingDTO.setTModified( entity.getTModified() );
        buildingDTO.setUCreated( entity.getUCreated() );
        buildingDTO.setUDeleted( entity.getUDeleted() );
        buildingDTO.setUModified( entity.getUModified() );
        buildingDTO.setBldArea( entity.getBldArea() );
        buildingDTO.setBldBlock( entity.getBldBlock() );
        buildingDTO.setBldBlockNo( entity.getBldBlockNo() );
        buildingDTO.setBldId( entity.getBldId() );
        buildingDTO.setBldName( entity.getBldName() );
        buildingDTO.setBldNo( entity.getBldNo() );
        buildingDTO.setBldPlotNo( entity.getBldPlotNo() );
        buildingDTO.setBldStreet( entity.getBldStreet() );
        buildingDTO.setBldWay( entity.getBldWay() );

        return buildingDTO;
    }

    @Override
    public MisBuilding toEntity(BuildingDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MisBuilding misBuilding = new MisBuilding();

        misBuilding.setActive( dto.getActive() );
        misBuilding.setDeleted( map( dto.getDeleted() ) );
        misBuilding.setTCreated( dto.getTCreated() );
        misBuilding.setTDeleted( dto.getTDeleted() );
        misBuilding.setTModified( dto.getTModified() );
        misBuilding.setUCreated( dto.getUCreated() );
        misBuilding.setUDeleted( dto.getUDeleted() );
        misBuilding.setUModified( dto.getUModified() );
        misBuilding.setBldArea( dto.getBldArea() );
        misBuilding.setBldBlock( dto.getBldBlock() );
        misBuilding.setBldBlockNo( dto.getBldBlockNo() );
        misBuilding.setBldId( dto.getBldId() );
        misBuilding.setBldName( dto.getBldName() );
        misBuilding.setBldNo( dto.getBldNo() );
        misBuilding.setBldPlotNo( dto.getBldPlotNo() );
        misBuilding.setBldStreet( dto.getBldStreet() );
        misBuilding.setBldWay( dto.getBldWay() );

        return misBuilding;
    }
}

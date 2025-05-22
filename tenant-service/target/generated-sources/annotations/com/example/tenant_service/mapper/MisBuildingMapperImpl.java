package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.BuildingDTO;
import com.example.tenant_service.entity.MisBuilding;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T20:55:14+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.13 (Ubuntu)"
)
@Component
public class MisBuildingMapperImpl implements MisBuildingMapper {

    @Override
    public BuildingDTO toDTO(MisBuilding entity) {
        if ( entity == null ) {
            return null;
        }

        BuildingDTO buildingDTO = new BuildingDTO();

        buildingDTO.setUCreated( entity.getUCreated() );
        buildingDTO.setTCreated( entity.getTCreated() );
        buildingDTO.setTModified( entity.getTModified() );
        buildingDTO.setUModified( entity.getUModified() );
        buildingDTO.setUDeleted( entity.getUDeleted() );
        buildingDTO.setTDeleted( entity.getTDeleted() );
        buildingDTO.setDeleted( map( entity.getDeleted() ) );
        buildingDTO.setActive( entity.getActive() );
        buildingDTO.setBldId( entity.getBldId() );
        buildingDTO.setBldName( entity.getBldName() );
        buildingDTO.setBldNo( entity.getBldNo() );
        buildingDTO.setBldArea( entity.getBldArea() );
        buildingDTO.setBldBlockNo( entity.getBldBlockNo() );
        buildingDTO.setBldPlotNo( entity.getBldPlotNo() );
        buildingDTO.setBldWay( entity.getBldWay() );
        buildingDTO.setBldStreet( entity.getBldStreet() );
        buildingDTO.setBldBlock( entity.getBldBlock() );

        return buildingDTO;
    }

    @Override
    public MisBuilding toEntity(BuildingDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MisBuilding misBuilding = new MisBuilding();

        misBuilding.setUCreated( dto.getUCreated() );
        misBuilding.setTCreated( dto.getTCreated() );
        misBuilding.setTModified( dto.getTModified() );
        misBuilding.setUModified( dto.getUModified() );
        misBuilding.setUDeleted( dto.getUDeleted() );
        misBuilding.setTDeleted( dto.getTDeleted() );
        misBuilding.setDeleted( map( dto.getDeleted() ) );
        misBuilding.setActive( dto.getActive() );
        misBuilding.setBldId( dto.getBldId() );
        misBuilding.setBldName( dto.getBldName() );
        misBuilding.setBldNo( dto.getBldNo() );
        misBuilding.setBldArea( dto.getBldArea() );
        misBuilding.setBldBlockNo( dto.getBldBlockNo() );
        misBuilding.setBldPlotNo( dto.getBldPlotNo() );
        misBuilding.setBldWay( dto.getBldWay() );
        misBuilding.setBldStreet( dto.getBldStreet() );
        misBuilding.setBldBlock( dto.getBldBlock() );

        return misBuilding;
    }
}

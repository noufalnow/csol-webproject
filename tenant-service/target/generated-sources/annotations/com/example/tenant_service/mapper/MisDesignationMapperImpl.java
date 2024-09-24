package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.DesignationDTO;
import com.example.tenant_service.entity.MisDesignation;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-23T11:36:47+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240215-1558, environment: Java 17.0.12 (Eclipse Adoptium)"
)
@Component
public class MisDesignationMapperImpl implements MisDesignationMapper {

    @Override
    public DesignationDTO toDTO(MisDesignation entity) {
        if ( entity == null ) {
            return null;
        }

        DesignationDTO designationDTO = new DesignationDTO();

        designationDTO.setActive( entity.getActive() );
        designationDTO.setDeleted( map( entity.getDeleted() ) );
        designationDTO.setTCreated( entity.getTCreated() );
        designationDTO.setTDeleted( entity.getTDeleted() );
        designationDTO.setTModified( entity.getTModified() );
        designationDTO.setUCreated( entity.getUCreated() );
        designationDTO.setUDeleted( entity.getUDeleted() );
        designationDTO.setUModified( entity.getUModified() );
        designationDTO.setDesigCode( entity.getDesigCode() );
        designationDTO.setDesigId( entity.getDesigId() );
        designationDTO.setDesigLevel( entity.getDesigLevel() );
        designationDTO.setDesigName( entity.getDesigName() );
        designationDTO.setDesigType( entity.getDesigType() );

        return designationDTO;
    }

    @Override
    public MisDesignation toEntity(DesignationDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MisDesignation misDesignation = new MisDesignation();

        misDesignation.setActive( dto.getActive() );
        misDesignation.setDeleted( map( dto.getDeleted() ) );
        misDesignation.setTCreated( dto.getTCreated() );
        misDesignation.setTDeleted( dto.getTDeleted() );
        misDesignation.setTModified( dto.getTModified() );
        misDesignation.setUCreated( dto.getUCreated() );
        misDesignation.setUDeleted( dto.getUDeleted() );
        misDesignation.setUModified( dto.getUModified() );
        misDesignation.setDesigCode( dto.getDesigCode() );
        misDesignation.setDesigId( dto.getDesigId() );
        misDesignation.setDesigLevel( dto.getDesigLevel() );
        misDesignation.setDesigName( dto.getDesigName() );
        misDesignation.setDesigType( dto.getDesigType() );

        return misDesignation;
    }
}

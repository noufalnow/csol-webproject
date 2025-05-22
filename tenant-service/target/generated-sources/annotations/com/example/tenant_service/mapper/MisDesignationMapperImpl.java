package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.DesignationDTO;
import com.example.tenant_service.entity.MisDesignation;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T20:55:15+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.13 (Ubuntu)"
)
@Component
public class MisDesignationMapperImpl implements MisDesignationMapper {

    @Override
    public DesignationDTO toDTO(MisDesignation entity) {
        if ( entity == null ) {
            return null;
        }

        DesignationDTO designationDTO = new DesignationDTO();

        designationDTO.setUCreated( entity.getUCreated() );
        designationDTO.setTCreated( entity.getTCreated() );
        designationDTO.setTModified( entity.getTModified() );
        designationDTO.setUModified( entity.getUModified() );
        designationDTO.setUDeleted( entity.getUDeleted() );
        designationDTO.setTDeleted( entity.getTDeleted() );
        designationDTO.setDeleted( map( entity.getDeleted() ) );
        designationDTO.setActive( entity.getActive() );
        designationDTO.setDesigId( entity.getDesigId() );
        designationDTO.setDesigCode( entity.getDesigCode() );
        designationDTO.setDesigName( entity.getDesigName() );
        designationDTO.setDesigLevel( entity.getDesigLevel() );
        designationDTO.setDesigType( entity.getDesigType() );

        return designationDTO;
    }

    @Override
    public MisDesignation toEntity(DesignationDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MisDesignation misDesignation = new MisDesignation();

        misDesignation.setUCreated( dto.getUCreated() );
        misDesignation.setTCreated( dto.getTCreated() );
        misDesignation.setTModified( dto.getTModified() );
        misDesignation.setUModified( dto.getUModified() );
        misDesignation.setUDeleted( dto.getUDeleted() );
        misDesignation.setTDeleted( dto.getTDeleted() );
        misDesignation.setDeleted( map( dto.getDeleted() ) );
        misDesignation.setActive( dto.getActive() );
        misDesignation.setDesigId( dto.getDesigId() );
        misDesignation.setDesigCode( dto.getDesigCode() );
        misDesignation.setDesigName( dto.getDesigName() );
        misDesignation.setDesigLevel( dto.getDesigLevel() );
        misDesignation.setDesigType( dto.getDesigType() );

        return misDesignation;
    }
}

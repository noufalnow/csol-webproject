package com.dms.kalari.admin.mapper;

import com.dms.kalari.admin.dto.DesignationDTO;
import com.dms.kalari.admin.entity.MisDesignation;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-20T20:39:30+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250523-0729, environment: Java 21.0.7 (Eclipse Adoptium)"
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

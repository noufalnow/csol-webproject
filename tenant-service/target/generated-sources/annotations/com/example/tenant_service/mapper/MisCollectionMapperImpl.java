package com.example.tenant_service.mapper;

import com.example.tenant_service.dto.MisCollectionDTO;
import com.example.tenant_service.entity.MisCollection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T20:55:15+0530",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.13 (Ubuntu)"
)
@Component
public class MisCollectionMapperImpl implements MisCollectionMapper {

    @Override
    public MisCollectionDTO toDTO(MisCollection misCollection) {
        if ( misCollection == null ) {
            return null;
        }

        MisCollectionDTO misCollectionDTO = new MisCollectionDTO();

        misCollectionDTO.setUCreated( misCollection.getUCreated() );
        misCollectionDTO.setTCreated( misCollection.getTCreated() );
        misCollectionDTO.setTModified( misCollection.getTModified() );
        misCollectionDTO.setUModified( misCollection.getUModified() );
        misCollectionDTO.setUDeleted( misCollection.getUDeleted() );
        misCollectionDTO.setTDeleted( misCollection.getTDeleted() );
        misCollectionDTO.setDeleted( map( misCollection.getDeleted() ) );
        misCollectionDTO.setActive( misCollection.getActive() );
        misCollectionDTO.setCollId( misCollection.getCollId() );
        misCollectionDTO.setCollType( misCollection.getCollType() );
        misCollectionDTO.setCollCust( misCollection.getCollCust() );
        misCollectionDTO.setCollAmount( misCollection.getCollAmount() );
        misCollectionDTO.setCollCollMode( misCollection.getCollCollMode() );
        misCollectionDTO.setCollChqNo( misCollection.getCollChqNo() );
        misCollectionDTO.setCollRemarks( misCollection.getCollRemarks() );
        misCollectionDTO.setCollPayDate( misCollection.getCollPayDate() );
        misCollectionDTO.setCollRefNo( misCollection.getCollRefNo() );
        misCollectionDTO.setCollFileNo( misCollection.getCollFileNo() );
        misCollectionDTO.setCollAppDate( misCollection.getCollAppDate() );
        misCollectionDTO.setCollAppBy( misCollection.getCollAppBy() );
        misCollectionDTO.setCollAppNote( misCollection.getCollAppNote() );
        misCollectionDTO.setCollAppStatus( misCollection.getCollAppStatus() );
        misCollectionDTO.setCollDiscount( misCollection.getCollDiscount() );
        misCollectionDTO.setMisTenants( misCollection.getMisTenants() );

        return misCollectionDTO;
    }

    @Override
    public MisCollection toEntity(MisCollectionDTO misCollectionDTO) {
        if ( misCollectionDTO == null ) {
            return null;
        }

        MisCollection misCollection = new MisCollection();

        misCollection.setUCreated( misCollectionDTO.getUCreated() );
        misCollection.setTCreated( misCollectionDTO.getTCreated() );
        misCollection.setTModified( misCollectionDTO.getTModified() );
        misCollection.setUModified( misCollectionDTO.getUModified() );
        misCollection.setUDeleted( misCollectionDTO.getUDeleted() );
        misCollection.setTDeleted( misCollectionDTO.getTDeleted() );
        misCollection.setDeleted( map( misCollectionDTO.getDeleted() ) );
        misCollection.setActive( misCollectionDTO.getActive() );
        misCollection.setCollId( misCollectionDTO.getCollId() );
        misCollection.setCollType( misCollectionDTO.getCollType() );
        misCollection.setCollCust( misCollectionDTO.getCollCust() );
        misCollection.setCollAmount( misCollectionDTO.getCollAmount() );
        misCollection.setCollDiscount( misCollectionDTO.getCollDiscount() );
        misCollection.setCollCollMode( misCollectionDTO.getCollCollMode() );
        misCollection.setCollChqNo( misCollectionDTO.getCollChqNo() );
        misCollection.setCollRemarks( misCollectionDTO.getCollRemarks() );
        misCollection.setCollPayDate( misCollectionDTO.getCollPayDate() );
        misCollection.setCollRefNo( misCollectionDTO.getCollRefNo() );
        misCollection.setCollFileNo( misCollectionDTO.getCollFileNo() );
        misCollection.setCollAppDate( misCollectionDTO.getCollAppDate() );
        misCollection.setCollAppBy( misCollectionDTO.getCollAppBy() );
        misCollection.setCollAppNote( misCollectionDTO.getCollAppNote() );
        misCollection.setCollAppStatus( misCollectionDTO.getCollAppStatus() );
        misCollection.setMisTenants( misCollectionDTO.getMisTenants() );

        return misCollection;
    }
}

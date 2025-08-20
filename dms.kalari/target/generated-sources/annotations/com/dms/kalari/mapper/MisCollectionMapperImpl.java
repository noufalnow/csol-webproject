package com.dms.kalari.mapper;

import com.dms.kalari.dto.MisCollectionDTO;
import com.dms.kalari.entity.MisCollection;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-20T20:39:31+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250523-0729, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class MisCollectionMapperImpl implements MisCollectionMapper {

    @Override
    public MisCollectionDTO toDTO(MisCollection misCollection) {
        if ( misCollection == null ) {
            return null;
        }

        MisCollectionDTO misCollectionDTO = new MisCollectionDTO();

        misCollectionDTO.setActive( misCollection.getActive() );
        misCollectionDTO.setDeleted( map( misCollection.getDeleted() ) );
        misCollectionDTO.setTCreated( misCollection.getTCreated() );
        misCollectionDTO.setTDeleted( misCollection.getTDeleted() );
        misCollectionDTO.setTModified( misCollection.getTModified() );
        misCollectionDTO.setUCreated( misCollection.getUCreated() );
        misCollectionDTO.setUDeleted( misCollection.getUDeleted() );
        misCollectionDTO.setUModified( misCollection.getUModified() );
        misCollectionDTO.setCollAmount( misCollection.getCollAmount() );
        misCollectionDTO.setCollAppBy( misCollection.getCollAppBy() );
        misCollectionDTO.setCollAppDate( misCollection.getCollAppDate() );
        misCollectionDTO.setCollAppNote( misCollection.getCollAppNote() );
        misCollectionDTO.setCollAppStatus( misCollection.getCollAppStatus() );
        misCollectionDTO.setCollChqNo( misCollection.getCollChqNo() );
        misCollectionDTO.setCollCollMode( misCollection.getCollCollMode() );
        misCollectionDTO.setCollCust( misCollection.getCollCust() );
        misCollectionDTO.setCollDiscount( misCollection.getCollDiscount() );
        misCollectionDTO.setCollFileNo( misCollection.getCollFileNo() );
        misCollectionDTO.setCollId( misCollection.getCollId() );
        misCollectionDTO.setCollPayDate( misCollection.getCollPayDate() );
        misCollectionDTO.setCollRefNo( misCollection.getCollRefNo() );
        misCollectionDTO.setCollRemarks( misCollection.getCollRemarks() );
        misCollectionDTO.setCollType( misCollection.getCollType() );
        misCollectionDTO.setMisTenants( misCollection.getMisTenants() );

        return misCollectionDTO;
    }

    @Override
    public MisCollection toEntity(MisCollectionDTO misCollectionDTO) {
        if ( misCollectionDTO == null ) {
            return null;
        }

        MisCollection misCollection = new MisCollection();

        misCollection.setActive( misCollectionDTO.getActive() );
        misCollection.setDeleted( map( misCollectionDTO.getDeleted() ) );
        misCollection.setTCreated( misCollectionDTO.getTCreated() );
        misCollection.setTDeleted( misCollectionDTO.getTDeleted() );
        misCollection.setTModified( misCollectionDTO.getTModified() );
        misCollection.setUCreated( misCollectionDTO.getUCreated() );
        misCollection.setUDeleted( misCollectionDTO.getUDeleted() );
        misCollection.setUModified( misCollectionDTO.getUModified() );
        misCollection.setCollAmount( misCollectionDTO.getCollAmount() );
        misCollection.setCollAppBy( misCollectionDTO.getCollAppBy() );
        misCollection.setCollAppDate( misCollectionDTO.getCollAppDate() );
        misCollection.setCollAppNote( misCollectionDTO.getCollAppNote() );
        misCollection.setCollAppStatus( misCollectionDTO.getCollAppStatus() );
        misCollection.setCollChqNo( misCollectionDTO.getCollChqNo() );
        misCollection.setCollCollMode( misCollectionDTO.getCollCollMode() );
        misCollection.setCollCust( misCollectionDTO.getCollCust() );
        misCollection.setCollDiscount( misCollectionDTO.getCollDiscount() );
        misCollection.setCollFileNo( misCollectionDTO.getCollFileNo() );
        misCollection.setCollId( misCollectionDTO.getCollId() );
        misCollection.setCollPayDate( misCollectionDTO.getCollPayDate() );
        misCollection.setCollRefNo( misCollectionDTO.getCollRefNo() );
        misCollection.setCollRemarks( misCollectionDTO.getCollRemarks() );
        misCollection.setCollType( misCollectionDTO.getCollType() );
        misCollection.setMisTenants( misCollectionDTO.getMisTenants() );

        return misCollection;
    }
}

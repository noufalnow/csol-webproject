package com.dms.kalari.mapper;

import com.dms.kalari.dto.CollectionDetDTO;
import com.dms.kalari.entity.MisCollectionDet;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-19T13:35:09+0530",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250523-0729, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class MisCollectionDetMapperImpl implements MisCollectionDetMapper {

    @Override
    public CollectionDetDTO toDTO(MisCollectionDet entity) {
        if ( entity == null ) {
            return null;
        }

        CollectionDetDTO collectionDetDTO = new CollectionDetDTO();

        collectionDetDTO.setCdetAmtPaid( entity.getCdetAmtPaid() );
        collectionDetDTO.setCdetAmtToPay( entity.getCdetAmtToPay() );
        collectionDetDTO.setCdetCollId( entity.getCdetCollId() );
        collectionDetDTO.setCdetId( entity.getCdetId() );
        collectionDetDTO.setCdetPoptId( entity.getCdetPoptId() );

        return collectionDetDTO;
    }

    @Override
    public MisCollectionDet toEntity(CollectionDetDTO dto) {
        if ( dto == null ) {
            return null;
        }

        MisCollectionDet misCollectionDet = new MisCollectionDet();

        misCollectionDet.setCdetAmtPaid( dto.getCdetAmtPaid() );
        misCollectionDet.setCdetAmtToPay( dto.getCdetAmtToPay() );
        misCollectionDet.setCdetCollId( dto.getCdetCollId() );
        misCollectionDet.setCdetId( dto.getCdetId() );
        misCollectionDet.setCdetPoptId( dto.getCdetPoptId() );

        return misCollectionDet;
    }
}
